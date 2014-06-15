package multipong;

import multipong.font.Fonts;
import multipong.screens.MainScreen;
import multipong.settings.Settings;

import com.badlogic.gdx.Game;

public class Multipong extends Game {

	@Override
	public void create() {
		Fonts.generateFonts();

		setScreen(new MainScreen(this, (int) Settings.appWidth,
				(int) Settings.appHeight));
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		super.render();
	}
}
