package multipong.board.boardobjects;

import com.badlogic.gdx.math.Rectangle;

public abstract class BoundedRectangle {

	protected Rectangle bounds;

	public BoundedRectangle(float x, float y, float width, float height) {
		bounds = new Rectangle(x, y, width, height);
	}

	public float getBottom() {
		return bounds.y;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public float getHeight() {
		return bounds.height;
	}

	public float getLeft() {
		return bounds.x;
	}

	public float getRight() {
		return bounds.x + bounds.width;
	}

	public float getTop() {
		return bounds.y + bounds.height;
	}

	public float getWidth() {
		return bounds.width;
	}

	public float getX() {
		return bounds.x;
	}

	public float getY() {
		return bounds.y;
	}

	public void setX(float x) {
		bounds.x = x;
	}

	public void setY(float y) {
		bounds.y = y;
	}

}
