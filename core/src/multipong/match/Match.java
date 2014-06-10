package multipong.match;

import multipong.board.Board;
import multipong.board.BoardUpdater;
import multipong.board.boardobjects.Player;
import multipong.screens.KeyMap;

public class Match {

	Player leftPlayer;
	Player rightPlayer;

	public Board board;

	boolean paused = true;
	boolean pauseWhenRoundWon = false;

	int winScore = 5000;

	public Match() {
		board = new Board();
	}

	public Match(float x, float y, float width, float height) {
		recalculateBoardGeometry(x, y, width, height);
	}

	public void recalculateBoardGeometry(float x, float y, float width,
			float height) {
		// TODO: maybe set pads to previous place if applicable...
		board = new Board(x, y, width, height);
		board.setPlayers(leftPlayer, rightPlayer);

	}

	public boolean isPlayable() {
		return board != null && leftPlayer != null && rightPlayer != null;
	}

	public boolean hasLeftPlayer() {
		return leftPlayer != null;
	}

	public boolean hasRightPlayer() {
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
		board.setPlayers(leftPlayer, rightPlayer);
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

			BoardUpdater.update(board, deltaTime);
			Player winner = BoardUpdater.getWinner(board);

			if (winner != null) {
				if (winner == leftPlayer) {
					leftPlayer.incrementScore();
					board.ball.resetWithRightPlayerDirection();
				} else {
					rightPlayer.incrementScore();
					board.ball.resetWithLeftPlayerDirection();
				}
			}
			if (winner != null && pauseWhenRoundWon) {
				paused = true;
			}
		}
	}

}
