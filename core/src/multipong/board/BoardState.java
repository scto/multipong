package multipong.board;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BoardState {

	Vector2[] separatorPos;
	float separatorLength;

	Pad leftPlayerPad;
	Vector2 leftPlayerScore;
	Vector2 leftPlayerName;

	Pad rightPlayerPad;
	Vector2 rightPlayerScore;
	Vector2 rightPlayerName;

	Field field;
	Ball ball;
	Player leftPlayer, rightPlayer;

	int stateTime = 0;

	/**
	 * 
	 * @param x
	 *            Screen x-coordinate of board
	 * @param y
	 *            Screen y-coordinate of board
	 * @param width
	 *            Board width
	 * @param height
	 *            Board height
	 */
	public BoardState(float x, float y, float width, float height,
			Player leftPlayer, Player rightPlayer) {

		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;

		generatePads(x, y, width, height);
		generateSeparator(x, y, width, height);
		generateScorePositions(x, y, width, height);
		generatePlayerNamePositions(x, y, width, height);
		generateField(x, y, width, height);
		generateBall(x, y, width, height);

	}

	private void generateBall(float x, float y, float width, float height) {
		float ballSize = width * 0.010f;
		float ballStartingXDirection = (MathUtils.random(0, 1) != 1) ? -1f : 1f;
		ball = new Ball(x + width / 2, y + height / 2, ballSize,
				ballStartingXDirection);
	}

	private void generateField(float x, float y, float width, float height) {
		field = new Field(x, y, width, height);
	}

	private void generatePlayerNamePositions(float x, float y, float width,
			float height) {
		leftPlayerName = new Vector2(x, y);
		rightPlayerName = new Vector2(x + width / 2, y);
	}

	private void generateScorePositions(float x, float y, float width,
			float height) {
		float playerScoreXoffset = width / 4;
		float playerScoreYoffset = height / 10;
		leftPlayerScore = new Vector2(x + playerScoreXoffset, y
				+ playerScoreYoffset);
		rightPlayerScore = new Vector2(x + playerScoreXoffset * 3, y
				+ playerScoreYoffset);
	}

	private void generateSeparator(float x, float y, float width, float height) {
		float separatorAmount = 32;
		float separatorXoffset = width / 2;
		separatorLength = height / ((separatorAmount * 2) - 1);
		separatorPos = new Vector2[(int) separatorAmount];
		for (int i = 0; i < separatorAmount; i++) {
			separatorPos[i] = new Vector2(x + separatorXoffset, y
					+ separatorLength * i * 2);
		}
	}

	private void generatePads(float x, float y, float width, float height) {
		float padHeight = height * 0.055f;
		float padWidth = width * 0.020f;
		float padXoffset = width * 0.16f;
		float padYoffset = (height - padHeight) / 2f;

		leftPlayerPad = new Pad(x + padXoffset, y + padYoffset, padWidth,
				padHeight);
		rightPlayerPad = new Pad(x + width - padXoffset - padWidth, y
				+ padYoffset, padWidth, padHeight);
	}
}
