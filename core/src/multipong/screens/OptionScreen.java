package multipong.screens;

import multipong.screens.MainScreen.MainMenuItem;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class OptionScreen extends AbstractScreen {
	
	
	private String name;

	
	public OptionScreen(Game game, int width, int height) {
		super(game, width, height);
		
		itemsXOffset = ((float) width / 2);
		itemsYOffset = ((float) height / (itemsSize + 2));
	}
	
	public enum OptionItem {
		OPTION1("Option 1 name"), OPTION2("Option 2"), OPTION3("option 3"), EXIT("Exit");

		private String name;

		private OptionItem(String name) {
			this.name = name;
		}

		public OptionItem getDown(OptionItem selectedItem) {
			int size = OptionItem.values().length;
			int selectedOrdinal = selectedItem.ordinal();
			if (selectedOrdinal == size - 1) {
				return OptionItem.values()[0];
			} else {
				return OptionItem.values()[selectedOrdinal + 1];
			}
		}

		public OptionItem getUp(OptionItem selectedItem) {
			int size = OptionItem.values().length;
			int selectedOrdinal = selectedItem.ordinal();
			if (selectedOrdinal == 0) {
				return OptionItem.values()[size - 1];
			} else {
				return OptionItem.values()[selectedOrdinal - 1];
			}
		}

		@Override
		public String toString() {
			return name;
		}
	}
	public OptionItem selectedItem = OptionItem.values()[0];

	OptionItem[] items = OptionItem.values();
	int itemsSize = OptionItem.values().length;

	float itemsYOffset;
	float itemsXOffset;
	
	public void render(float deltaTime) {
		super.render(deltaTime);

		update();

		batch.begin();
		for (int i = 0; i < itemsSize; i++) {
		OptionItem item = OptionItem.values()[i];
		if (item == selectedItem) {
			font.setColor(Color.WHITE);
		} else {
			font.setColor(Color.GRAY);
		}
		float xOffset = itemsXOffset
				- (font.getBounds(item.name()).width / 2);
		float yOffset = (i + 1) * itemsYOffset;

		font.draw(batch, item.name(), xOffset, yOffset);
	
	
		}
		batch.end();
	}
	
	public String toString() {
		return name;
	}
	
	private void update() {
		if (playerPressedEnter()) {

			switch (selectedItem) {

			case OPTION1:
				//game.setScreen(new DropInScreen(game, width, height));
				
				break;

			case OPTION2:

				break;

			case OPTION3:

				break;

			case EXIT:
				game.setScreen(new MainScreen(game, width, height));
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
