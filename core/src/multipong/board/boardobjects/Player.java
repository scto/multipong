package multipong.board.boardobjects;

import multipong.utils.KeyMap;

import com.badlogic.gdx.controllers.Controller;

public class Player {

	public String name;
	public int padGfx;
	public int score = 0;

	public KeyMap keyMap;
	public Controller controller;

	public Player(String name, int padGfx, KeyMap keyMap, Controller controller) {
		this.name = name;
		this.padGfx = padGfx;
		this.keyMap = keyMap;
		this.controller = controller;
	}

	public void incrementScore() {
		score++;
	}

	public void update(float deltaTime) {

	}
}
