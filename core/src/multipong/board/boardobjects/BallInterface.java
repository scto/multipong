package multipong.board.boardobjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface BallInterface {

	public void addTotalVelocityFromPad(float padYVelocity);

	public void addXVelocityFromPad(float padXVelocity);

	public void addYVelocityFromPad(float padYVelocity);

	public int currentXDirection();

	public int currentYDirection();

	public void dampen();

	public float getBottom();

	public Rectangle getBounds();

	public float getHeight();

	public float getLeft();

	public float getRight();

	public float getTop();

	public float getWidth();

	public float getX();

	public float getY();

	public void increaseXVelocity();

	public void increaseYVelocity();

	public boolean overlaps(Rectangle r);

	public void reset(float startingXDirection, Vector2 ballStartPos);

	public void resetWithLeftPlayerDirection(Vector2 ballStartPos);

	public void resetWithRightPlayerDirection(Vector2 ballStartPos);

	public void reverseX();

	public void reverseY();

	public void setX(float x);

	public void setY(float y);

	public void update(float deltaTime);
}
