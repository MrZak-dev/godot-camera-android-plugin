package org.godotengine.godot_android_camera;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemGenerationException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class MainActivity extends AppCompatActivity {

    private static final String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "    MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALQqUqel6hUw1eck\n" +
            "    7P9dzGD3Mn39O7hL3ktpSY5AbWphVsBtSXOEW8lxN7qiiC0sApiapXKQFWNtXA8w\n" +
            "    /zRVGhcYI3X3O22dTmtr7FMQCfLSp4qOj4SMBDdTupTn3ngQ4knewPToQqqwSFVk\n" +
            "    WGjvqUGmvIMPv84IP2a1058tD4BFAgMBAAECgYAujodIclwAuNt1vTmPLgZ8AjUm\n" +
            "    kJNnpxddJmqPG4AKzveTh0pFUy1C64JqtMh639YV6FSbu7cDOAXjmRZByEISyT8Z\n" +
            "    +o+2e0MIWcuMTNvlR+rQq6WxE7Skc+w5QX+ChC6g5p3t6l3q66EpVxYoi40AwKAZ\n" +
            "    NgWZr/fCk6sUjvKfiwJBAOt+NZMoXdBAzEL5/dT/A7qobJXrT9rN81ulLB/ZNyYA\n" +
            "    OCuRJOoealsN9njqOe6ymWYP21ow0tnXaA6tdn4ckTcCQQDD2rTbzJrFnaeNUc4l\n" +
            "    dh7i6Db5Vr9Fdeb3P+P/W1j41UeT7dhROP5w2BIcUO6Lk/82zFswdqQWoRhtVbnh\n" +
            "    o2hjAkAozinCYhA/cbrzMBZHvhiLU3vdTnT72Qv+KVTH//+KWidhhTj8SGDtfjHj\n" +
            "    KdL4BYKHKOA9dJBUpvqk8HHD0HdpAkEAm1iIB6c8Q+6OjasLRe9xBC2IHTFPOTsR\n" +
            "    XZ/RqiO8aHJS+eS3pYT9XGglxUv0ScWzsrVYxxHziPdyNpa+f1hrXQJBANPLIB65\n" +
            "    pelv/Zy+i0ktUnRCnab1t8FUCZ4/ep2lKN0VI9Y40nhxvOd2l/RulUmR+dZ0CY0K\n" +
            "    TTJdF+4g2kHmupY=\n" +
            "    -----END PRIVATE KEY-----";

    private static final String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "    MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0KlKnpeoVMNXnJOz/Xcxg9zJ9\n" +
            "    /Tu4S95LaUmOQG1qYVbAbUlzhFvJcTe6oogtLAKYmqVykBVjbVwPMP80VRoXGCN1\n" +
            "    9zttnU5ra+xTEAny0qeKjo+EjAQ3U7qU5954EOJJ3sD06EKqsEhVZFho76lBpryD\n" +
            "    D7/OCD9mtdOfLQ+ARQIDAQAB\n" +
            "    -----END PUBLIC KEY-----";

    private static final String TAG = "Cryptoo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, publicKey);


        try {

            String signature = "ZCyyPRe31Mc30CvzxtkvPA84LROrevKhQrtfDP68mJIlFjMXekdrEYLYu2W9x6GlMtADktw8ucIs7wEjHBxsC65+3D8xBo19nu4pl2s86KGz4N3c/0AJJgb0rUmjQuYMBGdY+aaRlniNiZ4Ab07BQn69BMbHeT72nipO1PePcsU=";

            if(verify("com.prada.pok",signature,publicKey)){
                Log.d(TAG, "onCreate: HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                
            }else {
                Log.d(TAG, "onCreate: HOOOOOOOOOOOOOOOOOOOOOOOO");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void generateKeys(){
        try {
            // Create keyPair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();



            // Convert PrivateKey to PEM format
            StringWriter privateWrite = new StringWriter();
            PemWriter privatePemWriter = new PemWriter(privateWrite);

            // Convert Public to PEM format
            StringWriter publicWrite = new StringWriter();
            PemWriter publicPemWriter = new PemWriter(publicWrite);

            publicPemWriter.writeObject(new PemObject("PUBLIC KEY",keyPair.getPublic().getEncoded()).generate());
            privatePemWriter.writeObject(new PemObject("PRIVATE KEY",keyPair.getPrivate().getEncoded()).generate());

            publicPemWriter.close();
            privatePemWriter.close();

            Log.d(TAG, privateWrite.toString());
            Log.d(TAG, publicWrite.toString());



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (PemGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String privateKeyPem,String plainText) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException {

        // Read PEM Format
        PemReader pemReader = new PemReader(new StringReader(privateKeyPem));
        byte[] content = pemReader.readPemObject().getContent();
        // Get X509EncodedKeySpec format
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return new String(Base64.encode(encryptedBytes));
    }


    public static String decrypt(String publicKeyPem,String encryptedString) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Read PEM Format
        PemReader pemReader = new PemReader(new StringReader(publicKeyPem));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();

        // Get PKCS8EncodedKeySpec for decrypt
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKeySecret = kf.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.DECRYPT_MODE, publicKeySecret);
        return new String(cipher.doFinal(Base64.decode(encryptedString)));

    }


    public static void verify(String publicKey , String text){

    }




    public static boolean verify(String plainText, String signature, String publicPemKey) throws Exception {

        // Read PEM Format
        PemReader pemReader = new PemReader(new StringReader(publicPemKey));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();

        // Get PKCS8EncodedKeySpec for decrypt
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKeySecret = kf.generatePublic(keySpec);

        Signature publicSignature = Signature.getInstance("SHA1withRSA");
        publicSignature.initVerify(publicKeySecret);
        publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = Base64.decode(signature);

        return publicSignature.verify(signatureBytes);
    }






}