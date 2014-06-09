package multipong;

public class Player {

	String name;
	int padGfx;
	int score = 0;

	int upKey;
	int downKey;

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

	public int getScore() {
		return score;
	}

	public void update(float deltaTime) {

	}
}
