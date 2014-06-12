package multipong.settings;

public class Settings {

	/**
	 * Max velocity of the ball.
	 */
	public static float ballMaxVelocity = 700f;

	/**
	 * Min velocity of the ball.
	 */
	public static float ballMinVelocity = 100f;

	/**
	 * Starting velocity of ball.
	 */
	public static float ballStartingVelocity = 200f;

	/**
	 * Maximum angle the ball can travel in after being hit by a pad. Angle is
	 * calculated x-axis.
	 */
	public static float ballMaxAngle = 45f;

	/**
	 * If the ball hits a wall, velocity will decrease with this percentage.
	 */
	public static float ballWallDampeningPercentOfVelocity = 10f;

	/**
	 * Percent of pad velocity to transfer to ball velocity on when ball hits
	 * pad.
	 */
	public static float ballAddedVelocityPercentOfPadVelocity = 50f;

	/**
	 * Acceleration of a pad.
	 */
	public static float padAcceleration = 1500f;

	/**
	 * Max velocity of a pad.
	 */
	public static float padMaxVelocity = 1000f;

	/**
	 * Ball size in percent of board height.
	 */
	public static float ballSizePercentOfBoardHeight = 1f;

	/**
	 * Pad height in percent of board height.
	 */
	public static float padHeightPercentOfBoardHeight = 10f;

	/**
	 * Pad width in percent of board width.
	 */
	public static float padWidthPercentOfBoardWidth = 2f;

	/**
	 * Pad offset on x-axis from right/left sides of board, in percent of board
	 * width.
	 */
	public static float padXOffsetPercentOfBoardWidth = 5f;
	
	/**
	 * Countdown before match, in seconds.
	 */
	public static float matchStartCountDownFrom = 3f;
	

}
