package multipong.board.boardobjects;

import com.badlogic.gdx.math.Rectangle;

public interface BallInterface {

	public void addTotalVelocityFromPad(float padYVelocity);

	public void addXVelocityFromPad(float padYVelocity);

	public void addYVelocityFromPad(float padYVelocity);

	public void dampen();

	public float getBottom();

	public float getHeight();

	public float getLeft();

	public float getRight();

	public float getTop();

	public float getWidth();

	public float getX();

	public float getY();

	public void increaseXVelocity(float velocity);

	public void increaseYVelocity(float velocity);

	public boolean overlaps(Rectangle r);

	public void reset(float startingXDirection);

	public void resetWithLeftPlayerDirection();

	public void resetWithRightPlayerDirection();

	public void reverseX();

	public void reverseY();

	public void setX(float x);

	public void setY(float y);

	public void update(float deltaTime);
}
