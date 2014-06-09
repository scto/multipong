package pong;

import com.badlogic.gdx.math.Rectangle;

public abstract class BoundedRectangle {

	Rectangle bounds;

	public BoundedRectangle(float x, float y, float width, float height) {
		bounds = new Rectangle(x, y, width, height);
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

	public float getLeft() {
		return bounds.x;
	}

	public float getRight() {
		return bounds.x + bounds.width;
	}

	public float getTop() {
		return bounds.y + bounds.height;
	}

	public float getBottom() {
		return bounds.y;
	}

	public float getWidth() {
		return bounds.width;
	}

	public float getHeight() {
		return bounds.height;
	}

}
