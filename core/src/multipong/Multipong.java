package multipong;

import multipong.font.SmartFontGenerator;
import multipong.screens.Fonts;
import multipong.screens.MainScreen;
import multipong.settings.Settings;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Multipong extends Game {

	@Override
	public void create() {
		SmartFontGenerator fontGen = new SmartFontGenerator();
		FileHandle exoFile = Gdx.files.local("PixelSplitter-Bold.ttf");
		Fonts.fontSmall = fontGen.createFont(exoFile, "exo-small", 16);
		Fonts.fontMedium = fontGen.createFont(exoFile, "exo-medium", 32);
		Fonts.fontLarge = fontGen.createFont(exoFile, "exo-large", 64);

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
