package multipong.screens;

import java.util.List;

import multipong.matchhandlers.DropInMatchHandler;
import multipong.rendering.MatchRenderer;
import multipong.settings.Settings;
import multipong.utils.ButtonMap;
import multipong.utils.KeyMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;

public class DropInScreen extends AbstractScreen {

	List<KeyMap> availableKeyMaps;
	List<Controller> availableControllers;
	String className = this.getClass().getSimpleName();
	DropInMatchHandler handler;
	MatchRenderer matchRenderer;

	boolean hasControllerFocus = true;

	public DropInScreen(Game game, int width, int height) {
		super(game, width, height);

		availableKeyMaps = loadKeyMaps();
		availableControllers = loadControllers();

		handler = new DropInMatchHandler(width, height);
		matchRenderer = new MatchRenderer(camera, handler.getVisibleMatches());
	}

	@Override
	public void dispose() {
		super.dispose();
		matchRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		for (KeyMap keys : availableKeyMaps) {
			if (keys.enterKey == keycode) {
				availableKeyMaps.remove(keys);
				handler.addNewPlayer(keys, null);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		if (!availableControllers.contains(arg0)) {
			return true;
		}
		if (arg1 == ButtonMap.enterButton) {
			availableControllers.remove(arg0);
			handler.addNewPlayer(null, arg0);
			return true;
		}
		return false;
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		matchRenderer.render(deltaTime);
		update(deltaTime);
		stateTime += deltaTime;
	}

	private void resetScreen() {
		availableKeyMaps = loadKeyMaps();
		availableControllers = loadControllers();
		handler = new DropInMatchHandler(width, height);
		matchRenderer = new MatchRenderer(camera, handler.getVisibleMatches());
	}

	private void update(float deltaTime) {
		handler.showBoardsInHiddenMatches();
		handler.updateBoardsInVisibleMatches(deltaTime);

		if (handler.matchHasBeenStartedSinceCreation()
				&& handler.allVisibleMatchesAreFinished()
				&& !handler.matchesArePending()) {
			if (handler.timeSinceLastFinishedMatchEnded() >= Settings.timeUntilDropOutAfterAllMatchesFinished) {
				Gdx.app.debug(className, "Restarting");
				resetScreen();
			}
		}
	}

}
