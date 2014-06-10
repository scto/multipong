package multipong.board;

import multipong.board.boardobjects.Ball;
import multipong.board.boardobjects.Field;
import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Board {

	public Vector2[] separatorPos;
	public float separatorLength;

	public Player leftPlayer;
	public Pad leftPlayerPad;
	public Vector2 leftPlayerScore;
	public Vector2 leftPlayerName;

	public Player rightPlayer;
	public Pad rightPlayerPad;
	public Vector2 rightPlayerScore;
	public Vector2 rightPlayerName;

	public Field field;
	public Ball ball;

	public int stateTime = 0;
	private float x, y, width, height;

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
	public Board(float x, float y, float width, float height) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		createPads();
		createSeparator();
		createScorePositions();
		createPlayerNamePositions();
		createField();
		createBall();
	}

	public Board() {

	}

	public void setPlayers(Player leftPlayer, Player rightPlayer) {
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
	}

	private void createBall() {
		float ballSize = width * 0.010f;
		float ballStartingXDirection = (MathUtils.random(0, 1) != 1) ? -1f : 1f;

		ball = new Ball(x + width / 2, y + height / 2, ballSize,
				ballStartingXDirection);
	}

	private void createField() {
		field = new Field(x, y, width, height);
	}

	private void createPlayerNamePositions() {
		leftPlayerName = new Vector2(x, y);
		rightPlayerName = new Vector2(x + width / 2, y);
	}

	private void createScorePositions() {
		float playerScoreXoffset = width / 4;
		float playerScoreYoffset = height / 10;

		leftPlayerScore = new Vector2(x + playerScoreXoffset, y
				+ playerScoreYoffset);
		rightPlayerScore = new Vector2(x + playerScoreXoffset * 3, y
				+ playerScoreYoffset);
	}

	private void createSeparator() {
		float separatorAmount = 32;
		separatorLength = height / (separatorAmount * 4);
		separatorPos = new Vector2[(int) separatorAmount];

		float xOffset = x + width / 2;
		float yStep = (height - separatorLength) / (separatorAmount - 1);

		for (int i = 0; i < separatorAmount; i++) {

			separatorPos[i] = new Vector2(xOffset, y + i * yStep);
		}
	}

	private void createPads() {
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
