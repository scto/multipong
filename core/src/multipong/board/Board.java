package multipong.board;

public class Board {

	Player leftPlayer;
	Player rightPlayer;

	public BoardState state;

	boolean paused = true;

	boolean pauseWhenRoundWon = false;

	int winScore = 5000;

	public Board() {
		state = new BoardState();
	}

	public Board(float x, float y, float width, float height) {
		recalculateBoardGeometry(x, y, width, height);
	}

	public void recalculateBoardGeometry(float x, float y, float width,
			float height) {
		// TODO: maybe set pads to previous place if applicable...
		state = new BoardState(x, y, width, height);
		state.setPlayers(leftPlayer, rightPlayer);

	}

	public boolean isPlayable() {
		return state != null && leftPlayer != null && rightPlayer != null;
	}

	public boolean hasFirstPlayer() {
		return leftPlayer != null;
	}

	public boolean hasChallanger() {
		return rightPlayer != null;
	}

	public boolean isFinished() {
		return (leftPlayer.score == winScore || rightPlayer.score == winScore);
	}

	public void addFirstPlayer(KeyMap firstPlayerKeyMap) {
		leftPlayer = new Player("player0", 0, firstPlayerKeyMap.upKey,
				firstPlayerKeyMap.downKey);
	}

	public void addChallanger(KeyMap secondPlayerKeyMap) {
		rightPlayer = new Player("player1", 0, secondPlayerKeyMap.upKey,
				secondPlayerKeyMap.downKey);
		state.setPlayers(leftPlayer, rightPlayer);
		paused = true;
	}

	public void pauseWhenRoundWon() {
		pauseWhenRoundWon = true;
	}

	public void resume() {
		pauseWhenRoundWon = false;
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public void update(float deltaTime) {
		if (!paused) {
			boolean roundIsWon = BoardEvaluator.evaluate(state, deltaTime);
			if (roundIsWon && pauseWhenRoundWon) {
				paused = true;
			}
		}
	}

}
