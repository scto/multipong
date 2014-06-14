package multipong.screens;

import java.util.List;

import multipong.matchhandlers.DropInMatchHandler;
import multipong.settings.Settings;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class DropInScreen extends AbstractScreen {

	List<KeyMap> availableKeyMaps;
	String className = this.getClass().getSimpleName();
	DropInMatchHandler handler;
	MatchRenderer matchRenderer;

	public DropInScreen(Game game, int width, int height) {
		super(game, width, height);
		availableKeyMaps = loadKeyMaps();
		handler = new DropInMatchHandler(width, height);

		matchRenderer = new MatchRenderer(camera, handler.getVisibleMatches());
	}

	private KeyMap getPressedKeyMap() {
		for (KeyMap keys : availableKeyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey) && stateTime > keyDelay) {
				// TODO: simultaneous press possible, use a list probably
				availableKeyMaps.remove(keys);
				return keys;
			}
		}
		return null;
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		update(deltaTime);

		matchRenderer.render(deltaTime);
		stateTime += deltaTime;

		if (handler.matchHasBeenStartedSinceCreation()
				&& handler.allVisibleMatchesAreFinished()
				&& !handler.matchesArePending()) {
			if (handler.timeSinceLastFinishedMatchEnded() >= Settings.timeUntilDropOutAfterAllMatchesFinished) {
				Gdx.app.debug(className, "Restarting");
				resetScreen();
			}
		}
	}

	private void resetScreen() {
		availableKeyMaps = loadKeyMaps();
		handler = new DropInMatchHandler(width, height);
		matchRenderer = new MatchRenderer(camera, handler.getVisibleMatches());
	}

	private void update(float deltaTime) {
		KeyMap newPlayerKeys = getPressedKeyMap();

		if (newPlayerKeys != null) {
			handler.addNewPlayer(newPlayerKeys);
		}
		handler.showBoardsInHiddenMatches();
		handler.updateBoardsInVisibleMatches(deltaTime);
	}

}
