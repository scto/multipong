package multipong;

import multipong.screens.MainScreen;

import com.badlogic.gdx.Game;

public class Multipong extends Game {

	static final int APP_WIDTH = 800;
	static final int APP_HEIGHT = 480;

	@Override
	public void create() {
		setScreen(new MainScreen(this, APP_WIDTH, APP_HEIGHT));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
	}
}
