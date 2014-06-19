package multipong.screens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multipong.board.boardobjects.Player;
import multipong.matchhandlers.AllVsAllMatchHandler;
import multipong.rendering.ScreenRenderer;
import multipong.utils.ControllerType;
import multipong.utils.KeyMap;
import multipong.utils.PS2Pad;
import multipong.utils.Xbox360Pad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.controllers.Controller;

public class AllVsAllScreen extends AbstractScreen {

	private List<KeyMap> availableKeyMaps;
	private List<Controller> availableControllers;

	private Map<KeyMap, Player> playersKeyMaps = new HashMap<KeyMap, Player>();
	private Map<Controller, Player> playersControllerMaps = new HashMap<Controller, Player>();

	// private String className = this.getClass().getSimpleName();
	private AllVsAllMatchHandler handler;
	private ScreenRenderer screenRenderer;

	public AllVsAllScreen(Game game, int width, int height) {
		super(game, width, height);

		availableKeyMaps = KeyMap.loadKeyMaps();
		availableControllers = loadControllers();

		handler = new AllVsAllMatchHandler(width, height);

		screenRenderer = new ScreenRenderer(camera);

	}

	private void addNewPlayer(KeyMap keyMap, Controller controller) {
		if (handler.gameInProgress()) {
			return;
		}
		if (keyMap != null) {
			if (!availableKeyMaps.contains(keyMap)) {
				Player playerWithKeyMap = playersKeyMaps.get(keyMap);
				handler.addPlayer(playerWithKeyMap);
				return;
			} else {
				availableKeyMaps.remove(keyMap);
			}
		}
		if (controller != null) {
			if (!availableControllers.contains(keyMap)) {
				Player playerWithController = playersControllerMaps
						.get(controller);
				handler.addPlayer(playerWithController);
				return;
			} else {
				availableControllers.remove(controller);
			}
		}
		String name = "Player " + (handler.numberOfTotalPlayers() + 1);
		Player player = new Player(name, 0, keyMap, controller);
		handler.addPlayer(player);
		if (keyMap != null) {
			playersKeyMaps.put(keyMap, player);
		}
		if (controller != null) {
			playersControllerMaps.put(controller, player);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		screenRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		for (KeyMap keys : keyMaps) {
			if (keys.enterKey == keycode) {
				addNewPlayer(keys, null);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		ControllerType type = ControllerType.getControllerType(arg0);

		// Add new player
		switch (type) {
		case PS2:
			if (arg1 == PS2Pad.BUTTON_X) {
				addNewPlayer(null, arg0);
				return true;
			}
			break;

		case XBOX360:
			if (arg1 == Xbox360Pad.BUTTON_A) {
				addNewPlayer(null, arg0);
				return true;
			}
			break;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg1 == PS2Pad.BUTTON_X) {
				addNewPlayer(null, arg0);
				return true;
			}
			break;
		}
		return false;
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		// updateHandler(deltaTime);
		handler.update(deltaTime);
		updateRenderables();
		screenRenderer.render(deltaTime);
		stateTime += deltaTime;
	}

	private void updateRenderables() {
		screenRenderer.setRenderables(handler.getRenderableRectangles(),
				handler.getRenderableStrings());
	}

}
