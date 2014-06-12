package multipong.board.boardobjects;

import com.badlogic.gdx.math.Rectangle;

public interface BallInterface {

	public void addYVelocityFromPad(float padYVelocity);

	public void addXVelocityFromPad(float padYVelocity);
	
	public void addTotalVelocityFromPad(float padYVelocity);

	public void increaseXVelocity(float velocity);

	public void increaseYVelocity(float velocity);

	public void reverseX();

	public void reverseY();

	public void dampen();

	public void resetWithLeftPlayerDirection();

	public void resetWithRightPlayerDirection();

	public void reset(float startingXDirection);

	public void update(float deltaTime);
	
	public float getX();

	public float getY();

	public void setX(float x);

	public void setY(float y);

	public float getLeft();

	public float getRight();

	public float getTop();

	public float getBottom();

	public float getWidth();

	public float getHeight();
	
	public boolean overlaps(Rectangle r);
}
