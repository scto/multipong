package multipong.board.boardobjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball extends BoundedRectangle {

	static final float ACCELERATION = 100f;
	static final float MAX_VEL = 2000f;
	static final float DAMP = 0.90f;

	Vector2 start = new Vector2();
	Vector2 accel = new Vector2();
	public Vector2 vel = new Vector2();

	float startingXDirection, stateTime;

	/**
	 * 
	 * @param x
	 *            Starting coordinate on x-axis,
	 * @param y
	 *            Starting coordinate on y-axis.
	 * @param size
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
	public Ball(float x, float y, float size, float startingXDirection) {
		super(x, y, size, size);
		start.x = x;
		start.y = y;

		stateTime = 0;
		reset(startingXDirection);
	}

	public void reverseX() {
		vel.x = -vel.x;
	}

	public void reverseY() {
		vel.y = -vel.y;
	}

	public void dampen() {
		vel.y *= DAMP;
		vel.x *= DAMP;
	}

	public void resetWithLeftPlayerDirection() {
		reset(-1);
	}

	public void resetWithRightPlayerDirection() {
		reset(1);
	}

	private void reset(float startingXDirection) {
		accel.x = 0;
		accel.y = 0;
		vel.x = 0;
		vel.y = 0;
		bounds.x = start.x;
		bounds.y = start.y;

		float dy = MathUtils.random(-1f, 1f);

		accel.x = ACCELERATION * startingXDirection;
		accel.y = ACCELERATION * dy;
		vel.add(accel.x, accel.y);
	}

	private void checkVelocity() {
		float velTot = vel.len();
		if (velTot > MAX_VEL) {
			float downScale = MAX_VEL / velTot;
			vel.x *= downScale;
			vel.y *= downScale;
		}
	}

	public void update(float deltaTime) {
		accel.scl(deltaTime);
		vel.add(accel.x, accel.y);

		vel.scl(deltaTime);

		bounds.x += vel.x;
		bounds.y += vel.y;

		vel.scl(1.0f / deltaTime);

		checkVelocity();

		stateTime += deltaTime;

	}

}
