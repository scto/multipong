package multipong.board;

import com.badlogic.gdx.Gdx;

public class BoardEvaluator {

	private static void parseInput(BoardState data) {
		if (Gdx.input.isKeyPressed(data.leftPlayer.downKey)) {
			data.leftPlayerPad.up();
		} else if (Gdx.input.isKeyPressed(data.leftPlayer.upKey)) {
			data.leftPlayerPad.down();
		} else {
			data.leftPlayerPad.stop();
		}

		if (Gdx.input.isKeyPressed(data.rightPlayer.downKey)) {
			data.rightPlayerPad.up();
		} else if (Gdx.input.isKeyPressed(data.rightPlayer.upKey)) {
			data.rightPlayerPad.down();
		} else {
			data.rightPlayerPad.stop();
		}
	}

	private static void checkPadBounds(BoardState data) {
		if (data.leftPlayerPad.getBottom() < data.field.getBottom()) {
			data.leftPlayerPad.bounds.y = data.field.getBottom();
			data.leftPlayerPad.stop();

		} else if (data.leftPlayerPad.getTop() > data.field.getTop()) {
			data.leftPlayerPad.bounds.y = data.field.getTop()
					- data.leftPlayerPad.getHeight();
			data.leftPlayerPad.stop();
		}

		if (data.rightPlayerPad.getBottom() < data.field.getBottom()) {
			data.rightPlayerPad.bounds.y = data.field.getBottom();
			data.rightPlayerPad.stop();

		} else if (data.rightPlayerPad.getTop() > data.field.getTop()) {
			data.rightPlayerPad.bounds.y = data.field.getTop()
					- data.rightPlayerPad.getHeight();
			data.rightPlayerPad.stop();
		}
	}

	private static void checkBallBounds(BoardState data) {

		if (data.ball.bounds.overlaps(data.rightPlayerPad.bounds)) {

			data.ball.vel.y += data.rightPlayerPad.getVelocity();

			data.ball.bounds.x = data.rightPlayerPad.getLeft()
					- data.ball.bounds.getWidth();
			data.ball.reverseX();
		}

		if (data.ball.bounds.overlaps(data.leftPlayerPad.bounds)) {

			data.ball.vel.y += data.leftPlayerPad.getVelocity();

			data.ball.bounds.x = data.leftPlayerPad.getRight();
			data.ball.reverseX();
		}

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

		if (data.ball.getTop() > data.field.getTop()) {
			data.ball.bounds.y = data.field.getTop() - data.ball.getHeight();
			data.ball.reverseY();
		}

		if (data.ball.getBottom() < data.field.getBottom()) {
			data.ball.bounds.y = data.field.getBottom();
			data.ball.reverseY();
		}
	}

	public static void update(BoardState data, float deltaTime) {
		checkBallBounds(data);
		checkPadBounds(data);
		parseInput(data);

		data.ball.update(deltaTime);
		data.leftPlayerPad.update(deltaTime);
		data.rightPlayerPad.update(deltaTime);

		data.stateTime += deltaTime;
	}

}
