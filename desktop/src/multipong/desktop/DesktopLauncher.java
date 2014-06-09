package multipong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import multipong.Multipong;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Pong";
		cfg.width = 800;
		cfg.height = 480;
		new LwjglApplication(new Multipong(), cfg);
	}
}
