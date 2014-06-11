package multipong.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class AbstractScreen implements Screen {

	int width, height;
	Game game;
	private List<KeyMap> keyMaps;

	float keyDelay = 0.2f;
	float timeSinceKeyPressed = keyDelay;

	protected BitmapFont font = new BitmapFont(true);
	protected SpriteBatch batch = new SpriteBatch();
	protected ShapeRenderer renderer = new ShapeRenderer();
	protected OrthographicCamera camera;
	protected float stateTime = 0;
	protected Viewport viewport;

	public AbstractScreen(Game game, int width, int height) {
		this.game = game;
		this.height = height;
		this.width = width;
		keyMaps = loadKeyMaps();

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(true);
		camera.position.set(width / 2, height / 2, 0);
		camera.update();

		viewport = new StretchViewport(width, height, camera);
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		updateKeyDelay(deltaTime);
	}

	private void updateKeyDelay(float deltaTime) {
		if (timeSinceKeyPressed < 0) {
			timeSinceKeyPressed = keyDelay;
		}
		timeSinceKeyPressed += deltaTime;
	}

	protected static List<KeyMap> loadKeyMaps() {
		List<KeyMap> availableKeyMaps = new ArrayList<KeyMap>();

		availableKeyMaps.add(new KeyMap(Keys.Q, Keys.A, Keys.NUM_1));
		availableKeyMaps.add(new KeyMap(Keys.W, Keys.S, Keys.NUM_2));

		availableKeyMaps.add(new KeyMap(Keys.E, Keys.D, Keys.NUM_3));
		availableKeyMaps.add(new KeyMap(Keys.R, Keys.F, Keys.NUM_4));

		availableKeyMaps.add(new KeyMap(Keys.T, Keys.G, Keys.NUM_5));
		availableKeyMaps.add(new KeyMap(Keys.Y, Keys.H, Keys.NUM_6));

		availableKeyMaps.add(new KeyMap(Keys.U, Keys.J, Keys.NUM_7));
		availableKeyMaps.add(new KeyMap(Keys.I, Keys.K, Keys.NUM_8));

		availableKeyMaps.add(new KeyMap(Keys.O, Keys.L, Keys.NUM_9));
		availableKeyMaps.add(new KeyMap(Keys.P, Keys.COLON, Keys.NUM_0));

		availableKeyMaps.add(new KeyMap(Keys.LEFT_BRACKET, Keys.APOSTROPHE,
				Keys.MINUS));
		availableKeyMaps.add(new KeyMap(Keys.RIGHT_BRACKET, Keys.ENTER,
				Keys.EQUALS));

		return availableKeyMaps;
	}

	protected boolean playerHoldsKeyDown() {
		for (KeyMap keys : keyMaps) {
			if (Gdx.input.isKeyPressed(keys.downKey)) {
				return true;
			}
		}
		return false;
	}

	protected boolean playerHoldsKeyUp() {
		for (KeyMap keys : keyMaps) {
			if (Gdx.input.isKeyPressed(keys.upKey)) {
				return true;
			}
		}
		return false;
	}

	protected boolean playerHoldsKeyEnter() {
		for (KeyMap keys : keyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey)) {
				return true;
			}
		}
		return false;
	}

	protected boolean playerPressedDown() {
		if (timeSinceKeyPressed > keyDelay && playerHoldsKeyDown()) {
			timeSinceKeyPressed = 0;
			return true;
		}
		return false;
	}

	protected boolean playerPressedUp() {
		if (timeSinceKeyPressed > keyDelay && playerHoldsKeyUp()) {
			timeSinceKeyPressed = 0;
			return true;
		}
		return false;
	}

	protected boolean playerPressedEnter() {
		if (timeSinceKeyPressed > keyDelay && playerHoldsKeyEnter()) {
			timeSinceKeyPressed = 0;
			return true;
		}
		return false;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		game.dispose();
		batch.dispose();
		font.dispose();
		renderer.dispose();
	}

}
