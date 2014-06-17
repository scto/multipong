package multipong.screens;

import java.util.ArrayList;
import java.util.List;

import multipong.utils.KeyMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;

public class AbstractScreen implements Screen, InputProcessor,
		ControllerListener {

	protected static List<Controller> loadControllers() {
		List<Controller> availableControllers = new ArrayList<Controller>();
		for (Controller controller : Controllers.getControllers()) {
			if (!availableControllers.contains(controller)) {
				availableControllers.add(controller);
			}
			Gdx.app.log("Found controller", controller.getName() + " "
					+ controller.hashCode());
		}
		return availableControllers;
	}

	int width, height;
	protected float stateTime = 0;

	Game game;
	protected List<KeyMap> keyMaps;
	protected List<Controller> controllers;

	protected OrthographicCamera camera;
	protected Viewport viewport;

	String className = this.getClass().getSimpleName();

	public AbstractScreen(Game game, int width, int height) {
		this.game = game;
		this.height = height;
		this.width = width;

		keyMaps = KeyMap.loadKeyMaps();
		controllers = loadControllers();

		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		camera.update();

		viewport = new StretchViewport(width, height, camera);

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Controllers.addListener(this);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		Controllers.removeListener(this);
		game.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		Controllers.removeListener(this);
	}

	@Override
	public void render(float deltaTime) {
		camera.update();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean accelerometerMoved(Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller arg0, int arg1, float arg2) {
		// Gdx.app.debug(className + " axis", arg0 + " " + arg1 + " " + arg2);
		return false;
	}

	@Override
	public boolean buttonDown(Controller arg0, int arg1) {
		// Gdx.app.debug(className + " button down", arg0 + " " + arg1);
		return false;
	}

	@Override
	public boolean buttonUp(Controller arg0, int arg1) {
		// Gdx.app.debug(className + " button up", arg0 + " " + arg1);
		return false;
	}

	@Override
	public void connected(Controller arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(Controller arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean povMoved(Controller arg0, int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		// Gdx.app.debug(className + " pov ", arg0 + " " + arg1 + " " + arg2);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		// Gdx.app.debug(className + " xslider", arg0 + " " + arg1 + " " +
		// arg2);

		return false;
	}

	@Override
	public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		// Gdx.app.debug(className + " yslider", arg0 + " " + arg1 + " " +
		// arg2);
		return false;
	}

}
