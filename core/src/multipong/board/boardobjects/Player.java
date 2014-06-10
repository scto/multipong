package multipong.board.boardobjects;

public class Player {

	public String name;
	int padGfx;
	public int score = 0;

	public int upKey;
	public int downKey;

	public Player(String name, int padGfx, int upKey, int downKey) {
		super();
		this.name = name;
		this.padGfx = padGfx;
		this.upKey = upKey;
		this.downKey = downKey;

	}

	public void incrementScore() {
		score++;
	}

	public void update(float deltaTime) {

	}
}
