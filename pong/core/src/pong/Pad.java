package pong;

import com.badlogic.gdx.math.Vector2;

public class Pad extends BoundedRectangle {

	static final float ACCELERATION = 1000f;
	static final float MAX_VEL = 300f;
	static final float DAMP = 0.1f;

	Vector2 start = new Vector2();
	Vector2 accel = new Vector2();
	Vector2 vel = new Vector2();

	float stateTime = 0;

	public Pad(float x, float y, float width, float height) {
		super(x, y, width, height);
		start.x = x;
		start.y = y;
	}

	public void stop() {
		accel.y = 0;
		vel.y = 0;
	}

	public void up() {
		accel.y = ACCELERATION;
	}

	public void down() {
		accel.y = -ACCELERATION;
	}

	public float getVelocity() {
		return vel.y;
	}

	public void update(float deltaTime) {

		accel.scl(deltaTime);
		vel.y += accel.y;

		vel.scl(deltaTime);

		bounds.y += vel.y;

		vel.scl(1.0f / deltaTime);

		if (vel.y > MAX_VEL) {
			vel.y = MAX_VEL;
		}

		if (vel.y < -MAX_VEL) {
			vel.y = -MAX_VEL;
		}

		stateTime += deltaTime;

	}
}
