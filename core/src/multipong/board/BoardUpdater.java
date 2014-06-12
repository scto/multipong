package multipong.board;

import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;

import com.badlogic.gdx.Gdx;

public class BoardUpdater {

	private static void checkBallBounce(Board board) {
		if (board.ball.getTop() > board.field.getTop()) {

			board.ball.setY(board.field.getTop() - board.ball.getHeight());
			board.ball.reverseY();
			board.ball.dampen();

		} else if (board.ball.getBottom() < board.field.getBottom()) {

			board.ball.setY(board.field.getBottom());
			board.ball.reverseY();
			board.ball.dampen();
		}
	}

	private static void checkBallHit(Board board) {

		if (board.ball.overlaps(board.rightPlayerPad.getBounds())) {

			board.ball.addYVelocityFromPad(board.rightPlayerPad.getVelocity());
			board.ball.addXVelocityFromPad(board.rightPlayerPad.getVelocity());
			board.ball.setX(board.rightPlayerPad.getLeft()- board.ball.getWidth());
			board.ball.reverseX();
			

		} else if (board.ball.overlaps(board.leftPlayerPad.getBounds())) {

			board.ball.addYVelocityFromPad(board.leftPlayerPad.getVelocity());

			
			
			board.ball.setX(board.leftPlayerPad.getRight());
			board.ball.reverseX();
			board.ball.addXVelocityFromPad(board.leftPlayerPad.getVelocity());
		}

	}

	private static void checkPadBounds(Board board) {
		checkPadBounds(board.leftPlayerPad, board);
		checkPadBounds(board.rightPlayerPad, board);
	}

	private static void checkPadBounds(Pad pad, Board board) {
		if (pad.getBottom() < board.field.getBottom()) {

			pad.setY(board.field.getBottom());
			pad.stop();

		} else if (pad.getTop() > board.field.getTop()) {

			pad.setY(board.field.getTop() - board.leftPlayerPad.getHeight());
			pad.stop();
		}
	}

	public static Player getRoundWinner(Board board) {
		if (board.ball.getRight() >= board.field.getRight()) {

			board.ball.setX(board.field.getRight() - board.ball.getWidth());
			return board.leftPlayer;

		} else if (board.ball.getLeft() <= board.field.getLeft()) {

			board.ball.setX(board.field.getLeft());
			return board.rightPlayer;
		}
		return null;
	}

	private static void parseInput(Board board) {
		parseInput(board.leftPlayer.downKey, board.leftPlayer.upKey,
				board.leftPlayerPad);
		parseInput(board.rightPlayer.downKey, board.rightPlayer.upKey,
				board.rightPlayerPad);

	}

	private static void parseInput(int downKey, int upKey, Pad pad) {
		if (Gdx.input.isKeyPressed(downKey) && Gdx.input.isKeyPressed(upKey)) {
			pad.stop();

		} else if (Gdx.input.isKeyPressed(downKey)) {
			if (pad.movingUp()) {
				pad.stop();
			}
			pad.up();

		} else if (Gdx.input.isKeyPressed(upKey)) {
			if (pad.movingDown()) {
				pad.stop();
			}
			pad.down();

		} else {
			pad.stop();
		}
	}

	public static void update(Board board, float deltaTime) {
		parseInput(board);
		updateState(board, deltaTime);

		checkBallHit(board);
		checkPadBounds(board);
		checkBallBounce(board);
	}

	public static void updateState(Board board, float deltaTime) {
		board.ball.update(deltaTime);
		board.leftPlayerPad.update(deltaTime);
		board.rightPlayerPad.update(deltaTime);

		board.stateTime += deltaTime;
	}

}
