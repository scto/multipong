package multipong.board;

import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;
import multipong.utils.ControllerType;
import multipong.utils.KeyMap;
import multipong.utils.PS2Pad;
import multipong.utils.Xbox360Pad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

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

			board.ball.increaseXVelocity();

			board.ball.addYVelocityFromPad(board.rightPad.getVelocity());

		} else if (board.ball.overlaps(board.leftPad.getBounds())) {

			board.ball.setX(board.leftPad.getRight());

			board.ball.reverseX();

			board.ball.increaseXVelocity();

			board.ball.addYVelocityFromPad(board.leftPad.getVelocity());

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

		if (keyMap == null && controller == null) {
			return;
		}

		if (controller != null) {

			ControllerType type = ControllerType.getControllerType(controller);

			switch (type) {
			case PS2:
				int axis = (int) controller.getAxis(PS2Pad.AXIS_ANALOG_LEFT_Y);

				// Check if ANALOG deactivated
				if (axis == -1) {
					if (pad.movingDown()) {
						pad.stop();
					}
					pad.up(1f);

				} else if (axis == 1) {
					if (pad.movingUp()) {
						pad.stop();
					}
					pad.down(1f);

				} else {
					PovDirection ps2Pov = controller.getPov(PS2Pad.BUTTON_DPAD);
					// Check if ANALOG activated
					if (ps2Pov == PS2Pad.BUTTON_DPAD_UP) {
						if (pad.movingDown()) {
							pad.stop();
						}
						pad.up(1f);

					} else if (ps2Pov == PS2Pad.BUTTON_DPAD_DOWN) {
						if (pad.movingUp()) {
							pad.stop();
						}
						pad.down(1f);

					} else {
						pad.stop();
					}
				}
				break;

			case XBOX360:
				PovDirection xbox360Pov = controller
						.getPov(Xbox360Pad.AXIS_LEFT_Y);
				if (xbox360Pov == Xbox360Pad.BUTTON_DPAD_UP) {
					if (pad.movingDown()) {
						pad.stop();
					}
					pad.up(1f);

				} else if (xbox360Pov == Xbox360Pad.BUTTON_DPAD_DOWN) {
					if (pad.movingUp()) {
						pad.stop();
					}
					pad.down(1f);

				} else if (xbox360Pov == Xbox360Pad.BUTTON_DPAD_CENTER) {
					pad.stop();
				}
				break;

			default:
				// TODO: Use PS2 mapping for now...
				int axisUnknown = (int) controller
						.getAxis(PS2Pad.AXIS_ANALOG_LEFT_Y);

				if (axisUnknown == -1) {
					if (pad.movingDown()) {
						pad.stop();
					}
					pad.up(1f);

				} else if (axisUnknown == 1) {
					if (pad.movingUp()) {
						pad.stop();
					}
					pad.down(1f);

				} else {
				}
				break;
			}

		} else if (Gdx.input.isKeyPressed(keyMap.downKey)
				&& Gdx.input.isKeyPressed(keyMap.upKey)) {
			pad.stop();

		} else if (Gdx.input.isKeyPressed(keyMap.upKey)) {
			if (pad.movingDown()) {
				pad.stop();
			}
			pad.up(1f);

		} else if (Gdx.input.isKeyPressed(keyMap.downKey)) {
			if (pad.movingUp()) {
				pad.stop();
			}
			pad.down(1f);

		} else {
			pad.stop();
		}
	}

	public static void updatePads(Board board, float deltaTime) {
		parseInput(board);
		checkPadBounds(board);
		board.leftPad.update(deltaTime);
		board.rightPad.update(deltaTime);
	}

	public static void updateBall(Board board, float deltaTime) {
		board.ball.update(deltaTime);
		checkBallHit(board);
		checkBallBounce(board);
		board.stateTime += deltaTime;
	}

}
