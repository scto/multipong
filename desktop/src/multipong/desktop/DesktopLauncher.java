package multipong.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import multipong.Multipong;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Pong";
		cfg.width = 1280;
		cfg.height = 720;
		new LwjglApplication(new Multipong(), cfg);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
