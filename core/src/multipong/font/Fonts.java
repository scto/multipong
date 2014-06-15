package multipong.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Fonts {
	public static BitmapFont fontSmall;
	public static BitmapFont fontMedium;
	public static BitmapFont fontLarge;

	public static void generateFonts() {
		SmartFontGenerator fontGen = new SmartFontGenerator();
		// FileHandle exoFile = Gdx.files.local("PixelSplitter-Bold.ttf");
		FileHandle exoFile = Gdx.files.local("fonts/Fixedsys500c.ttf");
		Fonts.fontSmall = fontGen.createFont(exoFile, "exo-small", 18);
		Fonts.fontMedium = fontGen.createFont(exoFile, "exo-medium", 32);
		Fonts.fontLarge = fontGen.createFont(exoFile, "exo-large", 64);
	}
}
