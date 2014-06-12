package multipong.match;

import multipong.board.Board;
import multipong.board.BoardUpdater;
import multipong.board.boardobjects.Player;
import multipong.screens.KeyMap;
import multipong.settings.Settings;

public class Match {

	Player leftPlayer;
	Player rightPlayer;
	Player matchWinner;

	public Board board;

	boolean paused = true;
	boolean pauseWhenRoundWon = false;

	public float stateTime = 0;
	public float timeSinceMatchFinished = 0;

	public Match() {
		board = new Board();
	}

	public Match(float x, float y, float width, float height) {
		recalculateBoardGeometry(x, y, width, height);
	}

	public void addLeftPlayer(KeyMap firstPlayerKeyMap) {
		leftPlayer = new Player("leftPlayer", 0, firstPlayerKeyMap.upKey,
				firstPlayerKeyMap.downKey);
		board.leftPlayer = leftPlayer;
	}

	public void addRightPlayer(KeyMap secondPlayerKeyMap) {
		rightPlayer = new Player("rightPlayer", 0, secondPlayerKeyMap.upKey,
				secondPlayerKeyMap.downKey);
		board.setPlayers(leftPlayer, rightPlayer);
		paused = true;
	}

	private boolean checkForRoundWinner() {

		boolean roundWon = false;
		Player roundWinner = BoardUpdater.getRoundWinner(board);

		if (roundWinner != null) {
			if (roundWinner == leftPlayer) {
				leftPlayer.incrementScore();
				board.ball.resetWithRightPlayerDirection();
			} else {
				rightPlayer.incrementScore();
				board.ball.resetWithLeftPlayerDirection();
			}
			roundWon = true;
		}
		return roundWon;

	}

	public Player getLeftPlayer() {
		return leftPlayer;
	}

	public Player getMatchWinner() {
		return matchWinner;
	}

	public Player getRightPlayer() {
		return rightPlayer;
	}

	public boolean hasLeftPlayer() {
		return leftPlayer != null;
	}

	public boolean hasRightPlayer() {
		return rightPlayer != null;
	}

	public boolean isCountingDown() {
		return stateTime <= Settings.matchStartCountDownFrom;
	}

	public boolean isFinished() {
		if (leftPlayer == null || rightPlayer == null) {
			return false;
		}
		return leftPlayer.score == Settings.scoreToWinMatch
				|| rightPlayer.score == Settings.scoreToWinMatch;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isPlayable() {
		return board != null && leftPlayer != null && rightPlayer != null;
	}

	public void pauseWhenRoundWon() {
		pauseWhenRoundWon = true;
	}

	public void recalculateBoardGeometry(float x, float y, float width,
			float height) {
		// TODO: maybe set pads to previous place if applicable...
		board = new Board(x, y, width, height);
		board.setPlayers(leftPlayer, rightPlayer);

	}

	public void resume() {
		pauseWhenRoundWon = false;
		paused = false;
	}

	public void update(float deltaTime) {
		if (isFinished()) {
			timeSinceMatchFinished += deltaTime;
		}
		if (!paused) {

			stateTime += deltaTime;

			if (isCountingDown()) {
				return;
			}

			BoardUpdater.update(board, deltaTime);

			boolean roundWon = checkForRoundWinner();

			if (roundWon && pauseWhenRoundWon) {
				paused = true;
			}

			if (isFinished()) {
				matchWinner = (leftPlayer.score > rightPlayer.score) ? leftPlayer
						: rightPlayer;
				paused = true;
				return;
			}
		}
	}

}
