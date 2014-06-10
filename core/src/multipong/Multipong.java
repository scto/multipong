package multipong;

import multipong.board.GameScreen;

import com.badlogic.gdx.Game;

public class Multipong extends Game {

	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 480;

	@Override
	public void create() {
		setScreen(new GameScreen(this, SCREEN_WIDTH, SCREEN_HEIGHT));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
	}
}
