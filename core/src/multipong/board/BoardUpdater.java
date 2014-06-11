package multipong.board;

import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;

import com.badlogic.gdx.Gdx;

public class BoardUpdater {

	public static void update(Board board, float deltaTime) {
		parseInput(board);
		updateState(board, deltaTime);

		checkBallHit(board);
		checkPadBounds(board);
		checkBallBounce(board);
	}

	private static void checkBallBounce(Board board) {
		if (board.ball.getTop() > board.field.getTop()) {

			board.ball.bounds.y = board.field.getTop() - board.ball.getHeight();
			board.ball.reverseY();
			board.ball.dampen();

		} else if (board.ball.getBottom() < board.field.getBottom()) {

			board.ball.bounds.y = board.field.getBottom();
			board.ball.reverseY();
			board.ball.dampen();
		}
	}

	private static void checkBallHit(Board board) {

		if (board.ball.bounds.overlaps(board.rightPlayerPad.bounds)) {

			board.ball.addYVelocity(board.rightPlayerPad.getVelocity());
			board.ball.bounds.x = board.rightPlayerPad.getLeft()
					- board.ball.bounds.getWidth();
			board.ball.reverseX();

		} else if (board.ball.bounds.overlaps(board.leftPlayerPad.bounds)) {

			board.ball.addYVelocity(board.leftPlayerPad.getVelocity());
			board.ball.bounds.x = board.leftPlayerPad.getRight();
			board.ball.reverseX();
		}

	}

	public static Player getWinner(Board board) {
		if (board.ball.getRight() >= board.field.getRight()) {

			board.ball.bounds.x = board.field.getRight()
					- board.ball.bounds.getWidth();
			return board.leftPlayer;

		} else if (board.ball.getLeft() <= board.field.getLeft()) {

			board.ball.bounds.x = board.field.getLeft();
			return board.rightPlayer;
		}
		return null;
	}

	private static void checkPadBounds(Board board) {
		checkPadBounds(board.leftPlayerPad, board);
		checkPadBounds(board.rightPlayerPad, board);
	}

	private static void checkPadBounds(Pad pad, Board board) {
		if (pad.getBottom() < board.field.getBottom()) {

			pad.bounds.y = board.field.getBottom();
			pad.stop();

		} else if (pad.getTop() > board.field.getTop()) {

			pad.bounds.y = board.field.getTop()
					- board.leftPlayerPad.getHeight();
			pad.stop();
		}
	}

	private static void parseInput(Board board) {
		parseInput(board.leftPlayer.downKey, board.leftPlayer.upKey,
				board.leftPlayerPad);
		parseInput(board.rightPlayer.downKey, board.rightPlayer.upKey,
				board.rightPlayerPad);

	}

	private static void parseInput(int downKey, int upKey, Pad pad) {
		if (Gdx.input.isKeyPressed(downKey)) {
			pad.up();
		} else if (Gdx.input.isKeyPressed(upKey)) {
			pad.down();
		} else {
			pad.stop();
		}
	}

	public static void updateState(Board board, float deltaTime) {
		board.ball.update(deltaTime);
		board.leftPlayerPad.update(deltaTime);
		board.rightPlayerPad.update(deltaTime);

		board.stateTime += deltaTime;
	}

}
