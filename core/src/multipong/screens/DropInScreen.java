package multipong.screens;

import java.util.List;

import multipong.matchhandlers.DropInMatchHandler;
import multipong.rendering.MatchRenderer;
import multipong.settings.Settings;
import multipong.utils.ControllerType;
import multipong.utils.KeyMap;
import multipong.utils.PS2Pad;
import multipong.utils.Xbox360Pad;

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

		availableKeyMaps = KeyMap.loadKeyMaps();
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

		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg1 == PS2Pad.BUTTON_X) {
				availableControllers.remove(arg0);
				handler.addNewPlayer(null, arg0);
			}
			return true;

		case XBOX360:
			if (arg1 == Xbox360Pad.BUTTON_A) {
				availableControllers.remove(arg0);
				handler.addNewPlayer(null, arg0);
			}
			return true;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg1 == PS2Pad.BUTTON_X) {
				availableControllers.remove(arg0);
				handler.addNewPlayer(null, arg0);
			}
			break;
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
		availableKeyMaps = KeyMap.loadKeyMaps();
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
