package multipong.utils;

import com.badlogic.gdx.controllers.PovDirection;

public class PS2Pad {

	/**
	 * Playstation 2 gamepad connected with an aftermarket "Two PS2-pads to USB adapter".
	 */
	public static final int BUTTON_X = 2;
	public static final int BUTTON_SQUARE = 3;
	public static final int BUTTON_TRIANGLE = 0;
	public static final int BUTTON_CIRCLE = 1;
	public static final int BUTTON_SELECT = 8;
	public static final int BUTTON_START = 9;
	public static final int BUTTON_L2 = 4;
	public static final int BUTTON_R2 = 5;
	public static final int BUTTON_L1 = 6;
	public static final int BUTTON_R1 = 7;
	
	public static final int BUTTON_DPAD = 0;
	public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
	public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
	public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
	public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;

	public static final int AXIS_ANALOG_LEFT_X = 0; // -1 is left | +1 is right
	public static final int AXIS_ANALOG_LEFT_Y = 1; // -1 is up | +1 is down
	public static final int AXIS_ANALOG_LEFT_TRIGGER = 10; // value 0 to 1f
	public static final int AXIS_ANALOG_RIGHT_X = 3; // -1 is left | +1 is right
	public static final int AXIS_ANALOG_RIGHT_Y = 2; // -1 is up | +1 is down
	public static final int AXIS_ANALOG_RIGHT_TRIGGER = 11; // value 0 to -1f
}
