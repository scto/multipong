package multipong.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import multipong.Multipong;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800,
				480);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new Multipong();
	}
}