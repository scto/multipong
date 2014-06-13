package multipong.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

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

	float itemsYOffset;
	float itemsXOffset;

	public MainScreen(Game game, int width, int height) {
		super(game, width, height);
		itemsXOffset = ((float) width / 2);
		itemsYOffset = ((float) height / (itemsSize + 2));
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		update();

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

	private void update() {
		if (playerPressedEnter()) {

			switch (selectedItem) {

			case DROPIN:
				game.setScreen(new DropInScreen(game, width, height));
				break;

			case OPTIONS:
				game.setScreen(new OptionScreen(game, width, height));
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

	}
}
