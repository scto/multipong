package multipong.board.boardobjects;

import multipong.settings.Settings;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Pad extends BoundedRectangle {

	private Vector2 start = new Vector2();
	private Vector2 accel = new Vector2();
	private Vector2 vel = new Vector2();

	float boardWidth, boardHeight;
	float stateTime = 0;

	public Pad(float x, float y, float padWidth, float padHeight,
			float boardWidth, float boardHeight) {
		super(x, y, padWidth, padHeight);
		start.x = x;
		start.y = y;
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
	}

	public void down(float multiple) {
		accel.y = -Settings.padAcceleration
				* (boardHeight / Settings.appHeight) * multiple;
	}

	public float getVelocity() {
		return vel.y;
	}

	public boolean movingDown() {
		return (vel.y < 0);
	}

	public boolean movingUp() {
		return (vel.y > 0);
	}

	public boolean overlaps(Rectangle r) {
		return bounds.overlaps(r);
	}

	public void stop() {
		accel.y = 0;
		vel.y = 0;
	}
	
	public void setUp(float upVelocity) {
		
	}
	
	public void setDown(float downVelocity) {
		
	}

	public void up(float multiple) {
		accel.y = Settings.padAcceleration * (boardHeight / Settings.appHeight)
				* multiple;
	}

	public void update(float deltaTime) {

		accel.scl(deltaTime);
		vel.y += accel.y;

		vel.scl(deltaTime);

		bounds.y += vel.y;

		vel.scl(1.0f / deltaTime);

		float padMaxVelocity = Settings.padMaxVelocity
				* (boardHeight / Settings.appHeight);

		if (vel.y > padMaxVelocity) {
			vel.y = padMaxVelocity;

		} else if (vel.y < -padMaxVelocity) {
			vel.y = -padMaxVelocity;
		}

		stateTime += deltaTime;

	}
}
