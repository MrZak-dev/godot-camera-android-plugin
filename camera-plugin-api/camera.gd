# tool
class_name AndroidCamera
extends CanvasLayer

# # docstring

# signals
signal permission_result(granted)
# enums
# constant
# exported variables
# public variables
# private variables
var _camera : Object 
# onready variables
onready var _camera_layout : Panel = get_node("parent/camera_layout")
# optional built-in virtual _init method

# remaining built-in virtual methods

func _enter_tree() -> void:
	if not _initialize():
		printerr("GodotCamera Plugin not found")
		

func _ready() -> void:
	Globals.unity_ads.hide_banner()

# public methods


func request_permission() -> void:
	if _camera != null:
		_camera.requestPermission()


func show_camera():
	var camera_dimensions : Dictionary = { 
	"width" : 0,
	"height" : 0,
	"x" : 0,
	"y" : 0}
	
	camera_dimensions.width = int(_camera_layout.get_global_rect().size.x)
	camera_dimensions.height = int(_camera_layout.get_global_rect().size.y)
	camera_dimensions.x = int(_camera_layout.get_global_rect().position.x)
	camera_dimensions.y = int(_camera_layout.get_global_rect().position.y)
	_resize_layout(camera_dimensions) # fit perfectly the camera to the layout
	
	if _camera != null:
		_camera.showCamera(
			camera_dimensions.width,
			camera_dimensions.height,
			camera_dimensions.x,
			camera_dimensions.y)


func hide_camera():
	if _camera != null:
		_camera.hideCamera()


# private methods


func _initialize() -> bool:
	if Engine.has_singleton("GodotCamera"):
		_camera = Engine.get_singleton("GodotCamera")
		if not _camera.is_connected("on_permission_result",self,"_on_permission_result"):
			_connect_signals()
			return true
	return false


func _connect_signals() -> void:
	if _camera != null:
		_camera.connect("on_permission_result",self,"_on_permission_result")


func _on_permission_result(granted):
	emit_signal("permission_result",granted)


func _resize_layout(dimensions : Dictionary) -> void:
	_camera_layout.rect_size.x = dimensions.width
	_camera_layout.rect_size.y = dimensions.height
	_camera_layout.rect_position.x = dimensions.x
	_camera_layout.rect_position.y = dimensions.y
