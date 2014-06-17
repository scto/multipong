package multipong.screens;

import multipong.font.Fonts;
import multipong.utils.ControllerType;
import multipong.utils.KeyMap;
import multipong.utils.PS2Pad;
import multipong.utils.Xbox360Pad;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainScreen extends AbstractScreen {

	public enum MainMenuItem {
		DROPIN("Drop-in"), OPTIONS("Options"), CREDITS("Credits"), EXIT("Exit");

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

	public MainMenuItem selectedItem = MainMenuItem.values()[0];

	MainMenuItem[] items = MainMenuItem.values();
	int itemsSize = MainMenuItem.values().length;

	String className = this.getClass().getSimpleName();

	// TODO: Should be moved to a renderer class
	BitmapFont font = Fonts.fontSmall;
	protected SpriteBatch batch = new SpriteBatch();
	protected Color foregroundObjectColor = Color.WHITE;
	protected Color backgroundObjectColor = Color.GRAY;
	protected Color backgroundColor = Color.BLACK;

	boolean hasControllerFocus = true;

	float itemsYOffset;
	float itemsXOffset;

	public MainScreen(Game game, int width, int height) {
		super(game, width, height);
		itemsXOffset = ((float) width / 2);
		itemsYOffset = ((float) height / (itemsSize + 2));
		Gdx.app.debug(className, "Created main screen.");
	}

	private void enterSelected() {
		switch (selectedItem) {

		case DROPIN:
			hasControllerFocus = false;
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
				selectedItem = selectedItem.getDown(selectedItem);
				return true;

			} else if (keys.upKey == keycode) {
				selectedItem = selectedItem.getUp(selectedItem);
				return true;

			} else if (keys.enterKey == keycode) {
				enterSelected();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		if (!hasControllerFocus) {
			return false;
		}

		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg1 == PS2Pad.AXIS_ANALOG_LEFT_Y) {
				if (arg2 == -1) {
					selectedItem = selectedItem.getUp(selectedItem);
				} else if (arg2 == 1) {
					selectedItem = selectedItem.getDown(selectedItem);
				}
			}
			return true;

		case XBOX360:
			break;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg1 == PS2Pad.AXIS_ANALOG_LEFT_Y) {
				if (arg2 == -1) {
					selectedItem = selectedItem.getUp(selectedItem);
				} else if (arg2 == 1) {
					selectedItem = selectedItem.getDown(selectedItem);
				}
			}
			break;
		}
		return false;
	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		if (!hasControllerFocus) {
			return false;
		}

		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg2 == PS2Pad.BUTTON_DPAD_UP) {
				selectedItem = selectedItem.getUp(selectedItem);
			} else if (arg2 == PS2Pad.BUTTON_DPAD_DOWN) {
				selectedItem = selectedItem.getDown(selectedItem);
			}
			return true;

		case XBOX360:
			if (arg2 == Xbox360Pad.BUTTON_DPAD_UP) {
				selectedItem = selectedItem.getUp(selectedItem);
			} else if (arg2 == Xbox360Pad.BUTTON_DPAD_DOWN) {
				selectedItem = selectedItem.getDown(selectedItem);
			}
			return true;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg2 == PS2Pad.BUTTON_DPAD_UP) {
				selectedItem = selectedItem.getUp(selectedItem);
			} else if (arg2 == PS2Pad.BUTTON_DPAD_DOWN) {
				selectedItem = selectedItem.getDown(selectedItem);
			}
			break;
		}
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		if (!hasControllerFocus) {
			return false;
		}

		ControllerType type = ControllerType.getControllerType(arg0);

		switch (type) {
		case PS2:
			if (arg1 == PS2Pad.BUTTON_X) {
				enterSelected();
			}
			return true;

		case XBOX360:
			if (arg1 == Xbox360Pad.BUTTON_A) {
				enterSelected();
			}
			return true;

		default:
			// TODO: Use PS2 mapping for now...
			if (arg1 == PS2Pad.BUTTON_X) {
				enterSelected();
			}
			break;
		}

		return false;
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		for (int i = 0; i < itemsSize; i++) {

			MainMenuItem item = MainMenuItem.values()[i];
			if (item == selectedItem) {
				font.setColor(foregroundObjectColor);
			} else {
				font.setColor(backgroundObjectColor);
			}
			float xOffset = itemsXOffset
					- (font.getBounds(item.name()).width / 2);
			float yOffset = height - (i + 1) * itemsYOffset;

			font.draw(batch, item.name(), xOffset, yOffset);
		}
		batch.end();
	}
}
