package multipong.board;

import multipong.board.boardobjects.BallInterface;
import multipong.board.boardobjects.BallType1;
import multipong.board.boardobjects.Field;
import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;
import multipong.settings.Settings;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Board {

	public Vector2[] separatorPos;
	public float separatorLength;

	public Player leftPlayer;
	public Pad leftPlayerPad;
	public Vector2 leftPlayerScore;
	public Vector2 leftPlayerNamePos;

	public Player rightPlayer;
	public Pad rightPlayerPad;
	public Vector2 rightPlayerScore;
	public Vector2 rightPlayerNamePos;

	public Field field;
	public BallInterface ball;

	public int stateTime = 0;
	public float x, y, width, height;

	public float midPointX;
	public float midPointY;

	public float leftMidPointX;
	public float rightMidPointX;

	/**
	 * An empty board.
	 */
	public Board() {

	}

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

		midPointX = x + width / 2;
		midPointY = y + height / 2;

		leftMidPointX = x + width / 4;
		rightMidPointX = x + width / 4 * 3;

		createPads();
		createSeparator();
		createScorePositions();
		createPlayerNamePositions();
		createField();
		createBall();
	}

	private void createBall() {
		float ballSize = height * Settings.ballSizePercentOfBoardHeight / 100;
		float ballStartingXDirection = (MathUtils.random(0, 1) != 1) ? -1f : 1f;

		ball = new BallType1(midPointX, midPointY, ballSize, width, height,
				ballStartingXDirection);
	}

	private void createField() {
		field = new Field(x, y, width, height);
	}

	private void createPads() {
		float padHeight = height * Settings.padHeightPercentOfBoardHeight / 100;
		float padWidth = width * Settings.padWidthPercentOfBoardWidth / 100;
		float padXoffset = width * Settings.padXOffsetPercentOfBoardWidth / 100;
		float padYoffset = (height - padHeight) / 2f;

		leftPlayerPad = new Pad(x + padXoffset, y + padYoffset, padWidth,
				padHeight, width, height);
		rightPlayerPad = new Pad(x + width - padXoffset - padWidth, y
				+ padYoffset, padWidth, padHeight, width, height);
	}

	private void createPlayerNamePositions() {
		leftPlayerNamePos = new Vector2(x, y);
		rightPlayerNamePos = new Vector2(x + width / 2, y);
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

	public Rectangle getBounds(){
		return field.getBounds();
	}
	
	public void setPlayers(Player leftPlayer, Player rightPlayer) {
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
	}
}
