package multipong.utils;

import com.badlogic.gdx.controllers.Controller;

public enum ControllerType {

	XBOX360(), PS2(), UNKNOWN();

	public static ControllerType getControllerType(Controller controller) {
		String name = controller.getName().toLowerCase();

		if (name.contains("box") && name.contains("360")) {
			return XBOX360;
		}

		if (name.contains("twin usb joystick")) {
			return PS2;
		}

		return UNKNOWN;
	}

}
