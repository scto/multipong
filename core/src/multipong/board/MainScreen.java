package multipong.board;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class MainScreen extends AbstractGameScreen {

	int width, height;

	List<KeyMap> availableKeyMaps;

	BoardHandler handler;

	BoardRenderer renderer;

	public MainScreen(Game game, int width, int height) {
		super(game);
		this.width = width;
		this.height = height;

		availableKeyMaps = loadKeyMaps();

		handler = new BoardHandler(width, height);
		renderer = new BoardRenderer(width, height, handler.visibleBoards);

		Board firstBoard = new Board(0, 0, width, height);
		handler.addBoard(firstBoard);

		Gdx.app.debug("First board", firstBoard.toString());
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		deltaTime = Math.min(0.06f, Gdx.graphics.getDeltaTime());

		KeyMap newPlayerKeys = getNewPlayerKeys();

		if (newPlayerKeys != null) {
			handler.addNewPlayer(newPlayerKeys);
		}
		handler.showHiddenBoards();
		handler.updateBoards(deltaTime);

		renderer.render(deltaTime);

	}

	@Override
	public void show() {

	}

	private KeyMap getNewPlayerKeys() {
		for (KeyMap keys : availableKeyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey)) {
				// TODO: simultaneous press possible, use a list probably
				availableKeyMaps.remove(keys);
				return keys;
			}
		}
		return null;
	}

	@Override
	public void hide() {
		renderer.dispose();
	}

	private static List<KeyMap> loadKeyMaps() {
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
}
