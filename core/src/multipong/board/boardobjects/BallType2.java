package multipong.board.boardobjects;

import multipong.settings.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BallType2 extends BoundedRectangle implements BallInterface {

	private Vector2 start = new Vector2();
	private Vector2 vel = new Vector2();

	float boardWidth, boardHeight;
	String className = this.getClass().getSimpleName();

	float ballMaxVel;
	float ballMinVel;
	float ballStartVel;
	float ballVelIncr;
	float boardSizeVelScale;

	float ballMaxXVel;
	float ballMaxYVel;

	public BallType2(float x, float y, float ballSize, float boardWidth,
			float boardHeight, float startingXDirection) {
		super(x, y, ballSize, ballSize);

		start.x = x;
		start.y = y;

		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;

		float aw = Settings.appWidth;
		float ah = Settings.appHeight;
		float bw = boardWidth;
		float bh = boardHeight;

		boardSizeVelScale = (float) Math.sqrt((bw * bw + bh * bh)
				/ (aw * aw + ah * ah));

		ballMaxVel = Settings.ballMaxVelocity * boardSizeVelScale;
		ballMinVel = Settings.ballMinVelocity * boardSizeVelScale;
		ballStartVel = Settings.ballStartingVelocity * boardSizeVelScale;
		ballVelIncr = Settings.ballAddSpeedToEveryHit * boardSizeVelScale;

		ballMaxXVel = Settings.ballMaxXVelocity * boardSizeVelScale;
		ballMaxYVel = Settings.ballMaxYVelocity * boardSizeVelScale;

		reset(startingXDirection);
	}

	@Override
	public void addTotalVelocityFromPad(float padYVelocity) {
		float velocity = padYVelocity
				* Settings.ballAddedVelocityPercentOfPadVelocity / 100;

		float addY = Math.abs(vel.y + velocity);
		float addX = Math.abs(vel.x);

		float resultAngle = (float) Math.toDegrees(Math.atan(addY / addX));

		Gdx.app.debug(className, "Adding y vel would give angle " + resultAngle);

		if (resultAngle <= Settings.ballMaxAngle) {
			vel.y += velocity;
			Gdx.app.debug(className, "Angle (cal)" + resultAngle);
		} else {
			float dirY = (vel.y + velocity) / addY;
			float dirX = vel.x / addX;
			float resultVel = (float) Math.sqrt(addX * addX + addY * addY);
			float newAngle = (float) Math.toRadians(Settings.ballMaxAngle);

			vel.y = (float) (Math.sin(newAngle) * resultVel) * dirY;
			vel.x = (float) (Math.cos(newAngle) * resultVel) * dirX;
			Gdx.app.debug(className, "Angle (max)" + Settings.ballMaxAngle);
		}

		Gdx.app.debug(className, "x Velocity " + vel.x);
		Gdx.app.debug(className, "y Velocity " + vel.y);
		Gdx.app.debug(className, "r Velocity  " + vel.len());

		Gdx.app.debug("Pad", "Velocity " + padYVelocity);

	}

	@Override
	public void addXVelocityFromPad(float padYVelocity) {

		ballVelIncr++;
		vel.x = (vel.x + (ballVelIncr * Settings.ballAddSpeedToEveryHit));

	}

	@Override
	public void addYVelocityFromPad(float padYVelocity) {
		// TODO Auto-generated method stub

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

	@Override
	public void dampen() {
		vel.y -= vel.y * Settings.ballWallDampeningPercentOfVelocity / 100;
		vel.x -= vel.x * Settings.ballWallDampeningPercentOfVelocity / 100;
	}

	@Override
	public void increaseXVelocity(float velocity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void increaseYVelocity(float velocity) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean overlaps(Rectangle r) {
		return bounds.overlaps(r);
	}

	@Override
	public void reset(float startingXDirection) {
		vel.x = 0;
		vel.y = 0;
		bounds.x = start.x;
		bounds.y = start.y;
		ballVelIncr = 0;

		float dy = MathUtils.random(-1f, 1f);

		float dlen = Math.abs(dy);
		float angle = (float) Math.atan(dlen);

		if (Math.toDegrees(angle) <= Settings.ballMaxAngle) {
			vel.x = (float) (Math.cos(angle) * ballStartVel * startingXDirection);
			vel.y = (float) (Math.sin(angle) * ballStartVel * dy / dlen);
		} else {
			float maxAngle = (float) Math.toRadians(Settings.ballMaxAngle);
			vel.x = (float) (Math.cos(maxAngle) * ballStartVel * startingXDirection);
			vel.y = (float) (Math.sin(maxAngle) * ballStartVel * dy / dlen);
		}
	}

	@Override
	public void resetWithLeftPlayerDirection() {
		reset(-1);
	}

	@Override
	public void resetWithRightPlayerDirection() {
		reset(1);
	}

	@Override
	public void reverseX() {
		vel.x = -vel.x;
	}

	@Override
	public void reverseY() {
		vel.y = -vel.y;
	}

	@Override
	public void update(float deltaTime) {
		vel.scl(deltaTime);

		bounds.x += vel.x;
		bounds.y += vel.y;

		vel.scl(1.0f / deltaTime);

		checkVelocity();
	}

}
