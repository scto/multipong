package multipong.board;

import multipong.board.boardobjects.BallInterface;
import multipong.board.boardobjects.BallType2;
import multipong.board.boardobjects.Field;
import multipong.board.boardobjects.Pad;
import multipong.board.boardobjects.Player;
import multipong.settings.Settings;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Board {

	public BallInterface ball;
	public Field field;

	public Player leftPlayer;
	public Pad leftPad;

	private float midPointX;
	private float midPointY;

	public Player rightPlayer;
	public Pad rightPad;

	public float x, y, width, height, stateTime;

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

		setPads();
		setField();
		setBall();
	}

	public Rectangle getBounds() {
		return field.getBounds();
	}

	private void setBall() {
		float ballSize = height * Settings.ballSizePercentOfBoardHeight / 100;
		float ballStartingXDirection = (MathUtils.random(0, 1) != 1) ? -1f : 1f;

		ball = new BallType2(midPointX, midPointY, ballSize, width, height,
				ballStartingXDirection);
	}

	private void setField() {
		field = new Field(x, y, width, height);
	}

	private void setPads() {
		float padHeight = height * Settings.padHeightPercentOfBoardHeight / 100;
		float padWidth = width * Settings.padWidthPercentOfBoardWidth / 100;
		float padXoffset = width * Settings.padXOffsetPercentOfBoardWidth / 100;
		float padYoffset = (height - padHeight) / 2f;

		leftPad = new Pad(x + padXoffset, y + padYoffset, padWidth, padHeight,
				width, height);
		rightPad = new Pad(x + width - padXoffset - padWidth, y + padYoffset,
				padWidth, padHeight, width, height);
	}

	public void setPlayers(Player leftPlayer, Player rightPlayer) {
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
	}
}
