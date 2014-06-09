package multipong.board;

import com.badlogic.gdx.Gdx;

public class BoardEvaluator {

	public static void evaluate(BoardState data, float deltaTime) {
		checkBallHit(data);
		checkPadBounds(data);
		checkBallBounce(data);
		checkBallWin(data);

		parseInput(data);

		updateState(data, deltaTime);

	}

	private static void checkBallBounce(BoardState data) {
		if (data.ball.getTop() > data.field.getTop()) {

			data.ball.bounds.y = data.field.getTop() - data.ball.getHeight();
			data.ball.reverseY();

		} else if (data.ball.getBottom() < data.field.getBottom()) {

			data.ball.bounds.y = data.field.getBottom();
			data.ball.reverseY();
		}
	}

	private static void checkBallHit(BoardState data) {

		if (data.ball.bounds.overlaps(data.rightPlayerPad.bounds)) {

			data.ball.vel.y += data.rightPlayerPad.getVelocity();
			data.ball.bounds.x = data.rightPlayerPad.getLeft()
					- data.ball.bounds.getWidth();
			data.ball.reverseX();

		} else if (data.ball.bounds.overlaps(data.leftPlayerPad.bounds)) {

			data.ball.vel.y += data.leftPlayerPad.getVelocity();
			data.ball.bounds.x = data.leftPlayerPad.getRight();
			data.ball.reverseX();
		}

	}

	private static void checkBallWin(BoardState data) {
		if (data.ball.getRight() >= data.field.getRight()) {

			data.ball.bounds.x = data.field.getRight()
					- data.ball.bounds.getWidth();
			data.ball.reset(1);
			data.leftPlayer.incrementScore();

		} else if (data.ball.getLeft() <= data.field.getLeft()) {

			data.ball.bounds.x = data.field.getLeft();
			data.ball.reset(-1);
			data.rightPlayer.incrementScore();
		}
	}

	private static void checkPadBounds(BoardState data) {
		checkPadBounds(data.leftPlayerPad, data);
		checkPadBounds(data.rightPlayerPad, data);
	}

	private static void checkPadBounds(Pad pad, BoardState data) {
		if (pad.getBottom() < data.field.getBottom()) {

			pad.bounds.y = data.field.getBottom();
			pad.stop();

		} else if (pad.getTop() > data.field.getTop()) {

			pad.bounds.y = data.field.getTop() - data.leftPlayerPad.getHeight();
			pad.stop();
		}
	}

	private static void parseInput(BoardState data) {

		parseInput(data.leftPlayer.downKey, data.leftPlayer.upKey,
				data.leftPlayerPad);
		parseInput(data.rightPlayer.downKey, data.rightPlayer.upKey,
				data.rightPlayerPad);

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

	public static void updateState(BoardState data, float deltaTime) {
		data.ball.update(deltaTime);
		data.leftPlayerPad.update(deltaTime);
		data.rightPlayerPad.update(deltaTime);

		data.stateTime += deltaTime;
	}

}
