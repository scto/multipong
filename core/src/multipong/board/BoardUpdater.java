package multipong.board;

import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;
import multipong.utils.ButtonMap;
import multipong.utils.KeyMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;

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

		if (board.ball.overlaps(board.rightPad.getBounds())) {

			board.ball.setX(board.rightPad.getLeft() - board.ball.getWidth());

			board.ball.reverseX();

			board.ball.addXVelocityFromPad(board.rightPad.getVelocity());

			board.ball.addTotalVelocityFromPad(board.rightPad.getVelocity());

		} else if (board.ball.overlaps(board.leftPad.getBounds())) {

			board.ball.setX(board.leftPad.getRight());

			board.ball.reverseX();

			board.ball.addXVelocityFromPad(board.leftPad.getVelocity());

			board.ball.addTotalVelocityFromPad(board.leftPad.getVelocity());

		}

	}

	private static void checkPadBounds(Board board) {
		checkPadBounds(board.leftPad, board);
		checkPadBounds(board.rightPad, board);
	}

	private static void checkPadBounds(Pad pad, Board board) {
		if (pad.getBottom() < board.field.getBottom()) {

			pad.setY(board.field.getBottom());
			pad.stop();

		} else if (pad.getTop() > board.field.getTop()) {

			pad.setY(board.field.getTop() - board.leftPad.getHeight());
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
		parseInput(board.leftPlayer.keyMap, board.leftPlayer.controller,
				board.leftPad);
		parseInput(board.rightPlayer.keyMap, board.rightPlayer.controller,
				board.rightPad);
	}

	private static void parseInput(KeyMap keyMap, Controller controller, Pad pad) {

		if (controller != null) {

			int axis = (int) controller.getAxis(1);

			if (ButtonMap.upButton == -1 && axis < 0 || ButtonMap.upButton == 1
					&& axis > 0) {
				if (pad.movingUp()) {
					pad.stop();
				}
				pad.up();

			} else if (ButtonMap.downButton == -1 && axis < 0
					|| ButtonMap.downButton == 1 && axis > 0) {
				if (pad.movingDown()) {
					pad.stop();
				}
				pad.down();

			} else {
				pad.stop();
			}

		} else if (Gdx.input.isKeyPressed(keyMap.downKey)
				&& Gdx.input.isKeyPressed(keyMap.upKey)) {
			pad.stop();

		} else if (Gdx.input.isKeyPressed(keyMap.upKey)) {
			if (pad.movingUp()) {
				pad.stop();
			}
			pad.up();

		} else if (Gdx.input.isKeyPressed(keyMap.downKey)) {
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
		board.leftPad.update(deltaTime);
		board.rightPad.update(deltaTime);

		board.stateTime += deltaTime;
	}

}
