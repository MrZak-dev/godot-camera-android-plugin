package org.godotengine.godotcameraplugin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import java.util.Set;


public class GodotCamera extends GodotPlugin {

    private static final String TAG = "GodotCamera";
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout layout = null;
    private RelativeLayout previewLayout = null;
    private boolean isCameraVisible = false;

    public GodotCamera(Godot godot) {
        super(godot);
    }

    @Nullable
    @Override
    public View onMainCreate(Activity activity) {
        layout = new FrameLayout(activity);
        return layout;
    }


    @NonNull
    @Override
    public String getPluginName() {
        return "GodotCamera";
    }


    @Override
    public void onMainRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onMainRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionResult = false;
        if(requestCode == 151516){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionResult = true;
            }
            emitSignal("on_permission_result",permissionResult);
        }
    }


    @UsedByGodot
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA},
                    151516);
        }else{
            emitSignal("on_permission_result",true);
        }
    }


    @UsedByGodot
    public void showCamera(int width , int height , int x , int y){
        getActivity().runOnUiThread(() -> {
            try{
                if(isCameraVisible){
                    return;
                }

                if(mCamera != null){
                    mCamera.startPreview();
                    layout.addView(previewLayout);
                    isCameraVisible = true;
                    return;
                }

                setCameraDimensions(width,height,x,y);
                openCamera();
            }catch(Exception e){
                Log.e(TAG, "showCamera: ", e);
            }
        });
    }


    @UsedByGodot
    public void hideCamera(){
        getActivity().runOnUiThread(()->{
            try{
                if(isCameraVisible){
                    mCamera.stopPreview();
                    layout.removeView(previewLayout);
                    isCameraVisible = false;
                }
            }catch(Exception e){
                Log.e(TAG, "hideCamera: ", e);
            }
        });
    }


    private void openCamera(){

        getActivity().runOnUiThread(() -> {
            try {
                // Create an instance of Camera
                mCamera = getCameraInstance();

                // Create our Preview view and set it as the content of our activity.
                mPreview = new CameraPreview(getActivity(), mCamera);
                previewLayout = new RelativeLayout(getActivity());
                previewLayout.addView(mPreview);
                layout.addView(previewLayout);
                isCameraVisible = true;
            }catch (Exception e){
                Log.e(TAG, "openCamera: ", e);
                isCameraVisible = true;
            }
        });
    }



    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;

        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    c = Camera.open(camIdx);
                }
            }
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "getCameraInstance: ", e);
        }
        return c; // returns null if camera is unavailable
    }


    public void setCameraDimensions(int width , int height , int x , int y){
        getActivity().runOnUiThread(() -> {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
            params.leftMargin = x;
            params.topMargin = y;
            layout.setLayoutParams(params);
        });
    }


    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signals = new ArraySet<>();

        // interstitial
        signals.add(new SignalInfo("on_permission_result",Boolean.class));


        return signals;
    }


}
