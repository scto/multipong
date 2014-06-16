package multipong.settings;

public class Settings {

	/**
	 * Application height. If changed, launcher classes must also be updated.
	 */
	public static float appHeight = 720;

	/**
	 * Application width. If changed, launcher classes must also be updated.
	 */
	public static float appWidth = 1280;

	/**
	 * Percent of pad velocity to transfer to ball velocity on when ball hits
	 * pad.
	 */
	public static float ballAddedVelocityPercentOfPadVelocity = 150f;

	/**
	 * Adds a velocity value to the ball for every time the ball is hitted by a
	 * pad.
	 */
	public static float ballAddVelocityToEveryHit = 15f;

	/**
	 * Maximum angle the ball can travel in after being hit by a pad. Angle is
	 * calculated x-axis.
	 */
	public static float ballMaxAngle = 45f;

	/**
	 * Total max velocity of the ball.
	 */
	public static float ballMaxVelocity = 1000f;

	/**
	 * Max velocity of the ball in x direction.
	 */
	public static float ballMaxXVelocity = 700f;

	/**
	 * Total max velocity of the ball in y direction.
	 */
	public static float ballMaxYVelocity = 700f;

	/**
	 * Min velocity of the ball.
	 */
	public static float ballMinVelocity = 100f;

	/**
	 * Min velocity of the ball.
	 */
	public static float ballMinXVelocity = 100f;

	/**
	 * Min velocity of the ball.
	 */
	public static float ballMinYVelocity = 100f;

	/**
	 * Ball size in percent of board height.
	 */
	public static float ballSizePercentOfBoardHeight = 1f;

	/**
	 * Starting velocity of ball.
	 */
	public static float ballStartingVelocity = 400f;

	/**
	 * Set which type of ball to use.
	 */
	public static int ballTypeNum = 2;

	/**
	 * The ball resets in the direction of the winner of the round.
	 */
	public static boolean ballResetsInRoundWinnerDirection = true;

	/**
	 * If the ball hits a wall, velocity will decrease with this percentage.
	 */
	public static float ballWallDampeningPercentOfVelocity = 0f;

	/**
	 * The score needed to win a match.
	 */
	public static int matchScoreToWin = 10;

	/**
	 * Acceleration of a pad.
	 */
	public static float padAcceleration = 1500f;

	/**
	 * Pad height in percent of board height.
	 */
	public static float padHeightPercentOfBoardHeight = 10f;

	/**
	 * Max velocity of a pad.
	 */
	public static float padMaxVelocity = 2000f;

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
	 * Countdown before starting a new round, after the boards have been
	 * redrawn.
	 */
	public static float timeFromRedrawToRoundBegins = 3f;

	/**
	 * Countdown before match, in seconds.
	 */
	public static float timeMatchStartCountDownFrom = 0f;

	/**
	 * How long to show the message "player is the winner!" after all matches
	 * are finished, in drop-in mode. After this time, boards are reset.
	 */
	public static float timeUntilDropOutAfterAllMatchesFinished = 3f;

	/**
	 * Use shaders.
	 */
	public static boolean shadersUseDistortion = true;
	public static boolean shadersUseNoisyPixels = true;
	public static boolean shadersUseVignette = true;
	public static boolean shadersUseColorBleed = true;

}
