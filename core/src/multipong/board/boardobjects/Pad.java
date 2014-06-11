package multipong.board.boardobjects;

import multipong.settings.Settings;

import com.badlogic.gdx.math.Vector2;

public class Pad extends BoundedRectangle {

	private Vector2 start = new Vector2();
	private Vector2 accel = new Vector2();
	private Vector2 vel = new Vector2();

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
		accel.y = Settings.padAcceleration;
	}

	public void down() {
		accel.y = -Settings.padAcceleration;
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

		if (vel.y > Settings.padMaxVelocity) {
			vel.y = Settings.padMaxVelocity;

		} else if (vel.y < -Settings.padMaxVelocity) {
			vel.y = -Settings.padMaxVelocity;
		}

		stateTime += deltaTime;

	}
}
