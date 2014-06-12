package multipong;

import multipong.screens.MainScreen;
import multipong.settings.Settings;

import com.badlogic.gdx.Game;

public class Multipong extends Game {

	@Override
	public void create() {
		setScreen(new MainScreen(this, (int) Settings.appWidth,
				(int) Settings.appHeight));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
	}
}
