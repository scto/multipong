package multipong.renderers;

import com.badlogic.gdx.graphics.Color;

import multipong.screens.MainScreen.MainMenuItem;

public class MainMenuRenderer extends AbstractRenderer {

	MainMenuItem[] items = MainMenuItem.values();
	int itemsSize = MainMenuItem.values().length;
	public MainMenuItem selected = MainMenuItem.values()[0];

	float itemsYOffset;
	float itemsXOffset;

	public MainMenuRenderer(int width, int height) {
		super(width, height);

		itemsXOffset = ((float) width / 2);
		itemsYOffset = ((float) height / (itemsSize + 2));
	}

	@Override
	public void render(float deltaTime) {

		camera.update();
		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		for (int i = 0; i < itemsSize; i++) {

			MainMenuItem item = MainMenuItem.values()[i];
			if (item == selected) {
				font.setColor(Color.WHITE);
			} else {
				font.setColor(Color.GRAY);
			}
			float xOffset = itemsXOffset
					- ((float) font.getBounds(item.name()).width / 2);
			float yOffset = (i + 1) * itemsYOffset;

			font.draw(batch, item.name(), xOffset, yOffset);
		}
		batch.end();
	}
}
