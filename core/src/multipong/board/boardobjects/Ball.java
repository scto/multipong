package multipong.board.boardobjects;

import multipong.settings.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball extends BoundedRectangle {

	private Vector2 start = new Vector2();
	private Vector2 vel = new Vector2();

	float boardWidth, boardHeight;
	String className = this.getClass().getSimpleName();

	float ballMaxVel;
	float ballMinVel;
	float ballStartVel;

	/**
	 * 
	 * @param x
	 *            Starting coordinate on x-axis,
	 * @param y
	 *            Starting coordinate on y-axis.
	 * @param ballSize
	 *            Size of ball in pixels.
	 * @param startingXDirection
	 *            1 or -1 depending on starting direction.
	 * @param fieldBounds
	 *            Bounds of the playing field.
	 * @param leftPadBounds
	 *            Bounds of left player pad.
	 * @param rightPadBounds
	 *            Bounds of right player pad.
	 */
	public Ball(float x, float y, float ballSize, float boardWidth,
			float boardHeight, float startingXDirection) {
		super(x, y, ballSize, ballSize);

		start.x = x;
		start.y = y;

		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;

		reset(startingXDirection);

		float aw = Settings.appWidth;
		float ah = Settings.appHeight;
		float bw = boardWidth;
		float bh = boardHeight;

		float boardSizeVelScale = (float) Math.sqrt((bw * bw + bh * bh)
				/ (aw * aw + ah * ah));

		ballMaxVel = Settings.ballMaxVelocity * boardSizeVelScale;
		ballMinVel = Settings.ballMinVelocity * boardSizeVelScale;
		ballStartVel = Settings.ballStartingVelocity
				* (boardWidth / Settings.appWidth);
	}

	public void addYVelocity(float padYVelocity) {
		float velocity = padYVelocity
				* Settings.ballAddedVelocityPercentOfPadVelocity / 100;
		float addY = Math.abs(vel.y + velocity);
		float addX = Math.abs(vel.x);

		float resultAngle = (float) Math.toDegrees(Math.atan(addY / addX));

		Gdx.app.debug(className, "Adding y vel would give angle " + resultAngle);

		if (resultAngle < Settings.ballMaxAngle) {
			vel.y += velocity;
			Gdx.app.debug(className, "Angle after y added " + resultAngle);
		} else {
			float dirY = (vel.y + velocity) / addY;
			float dirX = vel.x / addX;
			float resultVel = (float) Math.sqrt(addX * addX + addY * addY);

			vel.y = (float) (Math.sin(Settings.ballMaxAngle) * resultVel)
					* dirY;
			vel.x = (float) (Math.cos(Settings.ballMaxAngle) * resultVel)
					* dirX;
			Gdx.app.debug(className, "Using max angle " + Settings.ballMaxAngle);
		}

		Gdx.app.debug(className, "Velocity is " + vel.len());

	}

	public void reverseX() {
		vel.x = -vel.x;
	}

	public void reverseY() {
		vel.y = -vel.y;
	}

	public void dampen() {
		vel.y -= vel.y * Settings.ballWallDampeningPercentOfVelocity / 100;
		vel.x -= vel.x * Settings.ballWallDampeningPercentOfVelocity / 100;
	}

	public void resetWithLeftPlayerDirection() {
		reset(-1);
	}

	public void resetWithRightPlayerDirection() {
		reset(1);
	}

	private void reset(float startingXDirection) {
		vel.x = 0;
		vel.y = 0;
		bounds.x = start.x;
		bounds.y = start.y;

		float dy = MathUtils.random(-1f, 1f);

		// TODO: this is not right...
		vel.x = ballStartVel * startingXDirection;
		vel.y = ballStartVel * dy;
	}

	private void checkVelocity() {

		float velTot = vel.len();

		if (velTot > ballMaxVel) {
			float downScale = ballMaxVel / velTot;
			vel.scl(downScale);

			Gdx.app.debug(className, "Reached max velocity.");

		} else if (velTot < ballMinVel) {
			float upScale = ballMinVel / velTot;
			vel.scl(upScale);

			Gdx.app.debug(className, "Reached min velocity.");
		}
	}

	public void update(float deltaTime) {
		vel.scl(deltaTime);

		bounds.x += vel.x;
		bounds.y += vel.y;

		vel.scl(1.0f / deltaTime);

		checkVelocity();
	}

}
