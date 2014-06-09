package pong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Board {

	Ball ball;
	Field field;

	Vector2[] separatorPos;
	float separatorLength;

	Player leftPlayer;
	Pad leftPlayerPad;
	Vector2 leftPlayerScore;
	Vector2 leftPlayerName;

	Player rightPlayer;
	Pad rightPlayerPad;
	Vector2 rightPlayerScore;
	Vector2 rightPlayerName;

	float stateTime = 0;

	public Board(float x, float y, float width, float height,
			Player leftPlayer, Player rightPlayer) {

		float padHeight = height * 0.055f;
		float padWidth = width * 0.020f;
		float padXoffset = width * 0.16f;
		float padYoffset = (height - padHeight) / 2f;

		leftPlayerPad = new Pad(x + padXoffset, y + padYoffset, padWidth,
				padHeight);
		rightPlayerPad = new Pad(x + width - padXoffset - padWidth, y
				+ padYoffset, padWidth, padHeight);

		float separatorAmount = 32;
		float separatorXoffset = width / 2;
		separatorLength = height / ((separatorAmount * 2) - 1);
		separatorPos = new Vector2[(int) separatorAmount];
		for (int i = 0; i < separatorAmount; i++) {
			separatorPos[i] = new Vector2(x + separatorXoffset, y
					+ separatorLength * i * 2);
		}

		float playerScoreXoffset = width / 4;
		float playerScoreYoffset = height / 10;
		leftPlayerScore = new Vector2(x + playerScoreXoffset, y
				+ playerScoreYoffset);
		rightPlayerScore = new Vector2(x + playerScoreXoffset * 3, y
				+ playerScoreYoffset);

		leftPlayerName = new Vector2(x, y);
		rightPlayerName = new Vector2(x + width / 2, y);

		field = new Field(x, y, width, height);

		float ballSize = width * 0.010f;
		float ballStartingXDirection = (MathUtils.random(0, 1) != 1) ? -1f : 1f;
		ball = new Ball(x + width / 2, y + height / 2, ballSize,
				ballStartingXDirection);

		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;

	}

	private void parseInput() {
		if (Gdx.input.isKeyPressed(leftPlayer.downKey)) {
			leftPlayerPad.up();
		} else if (Gdx.input.isKeyPressed(leftPlayer.upKey)) {
			leftPlayerPad.down();
		} else {
			leftPlayerPad.stop();
		}

		if (Gdx.input.isKeyPressed(rightPlayer.downKey)) {
			rightPlayerPad.up();
		} else if (Gdx.input.isKeyPressed(rightPlayer.upKey)) {
			rightPlayerPad.down();
		} else {
			rightPlayerPad.stop();
		}
	}

	private void checkPadBounds() {
		if (leftPlayerPad.getBottom() < field.getBottom()) {
			leftPlayerPad.bounds.y = field.getBottom();
			leftPlayerPad.stop();

		} else if (leftPlayerPad.getTop() > field.getTop()) {
			leftPlayerPad.bounds.y = field.getTop() - leftPlayerPad.getHeight();
			leftPlayerPad.stop();
		}

		if (rightPlayerPad.getBottom() < field.getBottom()) {
			rightPlayerPad.bounds.y = field.getBottom();
			rightPlayerPad.stop();

		} else if (rightPlayerPad.getTop() > field.getTop()) {
			rightPlayerPad.bounds.y = field.getTop()
					- rightPlayerPad.getHeight();
			rightPlayerPad.stop();
		}
	}

	private void checkBallBounds() {

		if (ball.bounds.overlaps(rightPlayerPad.bounds)) {

			ball.vel.y += rightPlayerPad.getVelocity();

			ball.bounds.x = rightPlayerPad.getLeft() - ball.bounds.getWidth();
			ball.reverseX();
		}

		if (ball.bounds.overlaps(leftPlayerPad.bounds)) {

			ball.vel.y += leftPlayerPad.getVelocity();

			ball.bounds.x = leftPlayerPad.getRight();
			ball.reverseX();
		}

		if (ball.getRight() >= field.getRight()) {
			ball.bounds.x = field.getRight() - ball.bounds.getWidth();
			ball.reset(1);
			leftPlayer.incrementScore();

		} else if (ball.getLeft() <= field.getLeft()) {
			ball.bounds.x = field.getLeft();
			ball.reset(-1);
			rightPlayer.incrementScore();
		}

		if (ball.getTop() > field.getTop()) {
			ball.bounds.y = field.getTop() - ball.getHeight();
			ball.reverseY();
		}

		if (ball.getBottom() < field.getBottom()) {
			ball.bounds.y = field.getBottom();
			ball.reverseY();
		}
	}

	public void update(float deltaTime) {
		checkBallBounds();
		checkPadBounds();
		parseInput();

		ball.update(deltaTime);
		leftPlayerPad.update(deltaTime);
		rightPlayerPad.update(deltaTime);

		stateTime += deltaTime;
	}

}
