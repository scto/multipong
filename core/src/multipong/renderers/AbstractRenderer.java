package multipong.renderers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class AbstractRenderer {

	protected BitmapFont font = new BitmapFont(true);
	protected SpriteBatch batch = new SpriteBatch();
	protected ShapeRenderer renderer = new ShapeRenderer();
	protected OrthographicCamera camera;
	protected float stateTime = 0;

	public AbstractRenderer(int width, int height) {
		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(true);
		camera.position.set(width / 2, height / 2, 0);
		camera.update();
	}

	public abstract void render(float deltaTime);

	public void dispose() {
		batch.dispose();
		font.dispose();
		renderer.dispose();
	}
}
