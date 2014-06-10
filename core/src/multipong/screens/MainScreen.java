package multipong.screens;

import java.util.ArrayList;
import java.util.List;

import multipong.board.BoardRenderer;
import multipong.match.Match;
import multipong.matchhandlers.DropInMatchHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class MainScreen implements Screen {

	int appWidth, appHeight;

	List<KeyMap> availableKeyMaps;

	DropInMatchHandler handler;

	BoardRenderer renderer;

	Game game;

	public MainScreen(Game game, int appWidth, int appHeight) {
		this.appWidth = appWidth;
		this.appHeight = appHeight;
		this.game = game;

		availableKeyMaps = loadKeyMaps();

		handler = new DropInMatchHandler(appWidth, appHeight);
		renderer = new BoardRenderer(appWidth, appHeight,
				handler.getVisibleMatches());

		Match firstBoard = new Match(0, 0, appWidth, appHeight);
		handler.addMatch(firstBoard);

		Gdx.app.debug("First board", firstBoard.toString());
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		deltaTime = Math.min(0.06f, Gdx.graphics.getDeltaTime());

		KeyMap newPlayerKeys = getPressedKeyMap();

		if (newPlayerKeys != null) {
			handler.addNewPlayer(newPlayerKeys);
		}
		handler.showBoardsOfHiddenMatches();
		handler.updateBoardsOfVisibleMatches(deltaTime);

		renderer.render(deltaTime);

	}

	private KeyMap getPressedKeyMap() {
		for (KeyMap keys : availableKeyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey)) {
				// TODO: simultaneous press possible, use a list probably
				availableKeyMaps.remove(keys);
				return keys;
			}
		}
		return null;
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

	@Override
	public void hide() {
		renderer.dispose();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		game.dispose();
	}

	@Override
	public void resize(int width, int height) {
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
	public void show() {

	}
}
