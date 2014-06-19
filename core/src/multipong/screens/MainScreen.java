package multipong.screens;

import java.util.ArrayList;
import java.util.List;

import multipong.font.Fonts;
import multipong.rendering.RenderableString;
import multipong.rendering.ScreenRenderer;
import multipong.utils.ControllerType;
import multipong.utils.KeyMap;
import multipong.utils.PS2Pad;
import multipong.utils.Xbox360Pad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class MainScreen extends AbstractScreen {

	public enum MainMenuItem {
		ALL_VS_ALL("All vs. all"), DROPIN("Drop-in"), OPTIONS("Options"), CREDITS(
				"Credits"), EXIT("Exit");

		private String name;

		private MainMenuItem(String name) {
			this.name = name;
		}

		public MainMenuItem getDown(MainMenuItem selectedItem) {
			int size = MainMenuItem.values().length;
			int selectedOrdinal = selectedItem.ordinal();
			if (selectedOrdinal == size - 1) {
				return MainMenuItem.values()[0];
			} else {
				return MainMenuItem.values()[selectedOrdinal + 1];
			}
		}

		public MainMenuItem getUp(MainMenuItem selectedItem) {
			int size = MainMenuItem.values().length;
			int selectedOrdinal = selectedItem.ordinal();
			if (selectedOrdinal == 0) {
				return MainMenuItem.values()[size - 1];
			} else {
				return MainMenuItem.values()[selectedOrdinal - 1];
			}
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private MainMenuItem selectedItem = MainMenuItem.values()[0];
	private int itemsSize = MainMenuItem.values().length;

	private ScreenRenderer screenRenderer;
	private List<RenderableString> allRenderableStrings = new ArrayList<RenderableString>();

	private String className = this.getClass().getSimpleName();

	public MainScreen(Game game, int width, int height) {
		super(game, width, height);

		screenRenderer = new ScreenRenderer(camera);
		updateRenderables();
		Gdx.app.debug(className, "Created main screen.");

	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg1 == PS2Pad.AXIS_ANALOG_LEFT_Y) {
				if (arg2 == -1) {
					up();
					return true;
				} else if (arg2 == 1) {
					down();
					return true;
				}
			}
			break;

		case XBOX360:
			break;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg1 == PS2Pad.AXIS_ANALOG_LEFT_Y) {
				if (arg2 == -1) {
					up();
					return true;
				} else if (arg2 == 1) {
					down();
					return true;
				}
			}
			break;
		}
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg1 == PS2Pad.BUTTON_X) {
				enterSelected();
				return true;
			}
			break;

		case XBOX360:
			if (arg1 == Xbox360Pad.BUTTON_A) {
				enterSelected();
				return true;
			}
			break;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg1 == PS2Pad.BUTTON_X) {
				enterSelected();
				return true;
			}
			break;
		}

		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
		screenRenderer.dispose();
	}

	private void down() {
		selectedItem = selectedItem.getDown(selectedItem);
		updateRenderables();
	}

	private void enterSelected() {
		switch (selectedItem) {

		case ALL_VS_ALL:
			game.setScreen(new AllVsAllScreen(game, width, height));
			break;

		case DROPIN:
			game.setScreen(new DropInScreen(game, width, height));
			break;

		case OPTIONS:
			// game.setScreen(new OptionScreen(game, width, height));
			break;

		case CREDITS:

			break;

		case EXIT:
			Gdx.app.exit();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		for (KeyMap keys : keyMaps) {

			if (keys.downKey == keycode) {
				down();
				return true;

			} else if (keys.upKey == keycode) {
				up();
				return true;

			} else if (keys.enterKey == keycode) {
				enterSelected();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg2 == PS2Pad.BUTTON_DPAD_UP) {
				up();
				return true;
			} else if (arg2 == PS2Pad.BUTTON_DPAD_DOWN) {
				down();
				return true;
			}
			break;

		case XBOX360:
			if (arg2 == Xbox360Pad.BUTTON_DPAD_UP) {
				up();
				return true;
			} else if (arg2 == Xbox360Pad.BUTTON_DPAD_DOWN) {
				down();
				return true;
			}
			break;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg2 == PS2Pad.BUTTON_DPAD_UP) {
				up();
				return true;
			} else if (arg2 == PS2Pad.BUTTON_DPAD_DOWN) {
				down();
				return true;
			}
			break;
		}
		return false;
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		screenRenderer.setRenderables(null, allRenderableStrings);
		screenRenderer.render(deltaTime);
	}

	@Override
	public void show() {
		super.show();
		screenRenderer = new ScreenRenderer(camera);
		updateRenderables();
	}

	private void up() {
		selectedItem = selectedItem.getUp(selectedItem);
		updateRenderables();
	}

	private void updateRenderables() {
		allRenderableStrings.clear();

		float itemsXOffset = ((float) width / 2);
		float itemsYOffset = ((float) height / (itemsSize + 2));

		for (int i = 0; i < itemsSize; i++) {
			RenderableString r = new RenderableString();

			MainMenuItem item = MainMenuItem.values()[i];
			r.text = item.name;
			r.font = Fonts.fontSmall;

			if (item == selectedItem) {
				r.color = Color.WHITE;
			} else {
				r.color = Color.GRAY;
			}
			float xOffset = itemsXOffset - (r.font.getBounds(r.text).width / 2);
			float yOffset = height - (i + 1) * itemsYOffset;

			r.pos = new Vector2(xOffset, yOffset);
			allRenderableStrings.add(r);
		}
	}

}
