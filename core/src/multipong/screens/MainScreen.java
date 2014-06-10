package multipong.screens;

import multipong.renderers.MainMenuRenderer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

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
	MainMenuRenderer renderer;

	public MainScreen(Game game, int width, int height) {
		super(game, width, height);

		renderer = new MainMenuRenderer(width, height);
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		if (playerPressedEnter()) {

			switch (selectedItem) {
			case DROPIN:
				game.setScreen(new DropInScreen(game, width, height));
				break;

			case OPTIONS:

				break;

			case CREDITS:

				break;

			case EXIT:
				Gdx.app.exit();
				break;

			default:
				break;
			}

		} else if (playerPressedUp()) {
			selectedItem = selectedItem.getUp(selectedItem);

		} else if (playerPressedDown()) {
			selectedItem = selectedItem.getDown(selectedItem);
		}

		renderer.selected = selectedItem;
		renderer.render(deltaTime);
	}
}
