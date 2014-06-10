package multipong.screens;

import java.util.List;

import multipong.match.Match;
import multipong.matchhandlers.DropInMatchHandler;
import multipong.renderers.BoardRenderer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class DropInScreen extends AbstractScreen {

	List<KeyMap> availableKeyMaps;

	DropInMatchHandler handler;

	BoardRenderer renderer;

	public DropInScreen(Game game, int width, int height) {
		super(game, width, height);

		availableKeyMaps = loadKeyMaps();

		handler = new DropInMatchHandler(width, height);
		renderer = new BoardRenderer(width, height, handler.getVisibleMatches());

		Match firstBoard = new Match(0, 0, width, height);
		handler.addMatch(firstBoard);

		Gdx.app.debug("First board", firstBoard.toString());
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		KeyMap newPlayerKeys = getPressedKeyMap();

		if (newPlayerKeys != null) {
			handler.addNewPlayer(newPlayerKeys);
		}
		handler.showBoardsInHiddenMatches();
		handler.updateBoardsInVisibleMatches(deltaTime);

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

	@Override
	public void hide() {
		renderer.dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		renderer.dispose();
	}

}
