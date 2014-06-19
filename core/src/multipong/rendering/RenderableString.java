package multipong.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class RenderableString {
	public Vector2 pos;
	public BitmapFont font;
	public String text;
	public Color color;

	public float getHeight() {
		if (text == null) {
			return 0;
		}
		return font.getBounds(text).height;
	}

	public float getWidth() {
		if (text == null) {
			return 0;
		}
		return font.getBounds(text).width;
	}
}