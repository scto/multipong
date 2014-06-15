package multipong.screens;

import java.util.ArrayList;
import java.util.List;

import multipong.board.boardobjects.Player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RenderObjects {

	public class StringRenderable {
		Vector2 pos;
		BitmapFont font;
		String text;
		Color color;
	}

	public class RectRenderable {
		List<Rectangle> rects = new ArrayList<Rectangle>();
		ShapeType type;
		Color color;
	}

	float x, y, width, height;
	Rectangle board, leftPad, rightPad, ball;
	Player leftPlayer, rightPlayer;

	public RenderObjects(Rectangle board, Rectangle leftPad,
			Rectangle rightPad, Rectangle ball, Player leftPlayer,
			Player rightPlayer) {
		this.x = board.x;
		this.y = board.y;
		this.width = board.width;
		this.height = board.height;
		this.board = board;
		this.leftPad = leftPad;
		this.rightPad = rightPad;
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
	}

	public RectRenderable ball() {
		RectRenderable r = new RectRenderable();
		r.rects.add(ball);
		r.type = ShapeType.Filled;
		r.color = Color.WHITE;
		return r;
	}

	public RectRenderable leftPad() {
		RectRenderable r = new RectRenderable();
		r.rects.add(leftPad);
		r.type = ShapeType.Filled;
		r.color = Color.WHITE;
		return r;
	}

	public RectRenderable rightPad() {
		RectRenderable r = new RectRenderable();
		r.rects.add(rightPad);
		r.type = ShapeType.Filled;
		r.color = Color.WHITE;
		return r;
	}

	public RectRenderable field() {
		RectRenderable r = new RectRenderable();

		float border = 2;
		float width = board.getWidth();
		float height = board.getHeight();
		float left = board.getX();
		float right = board.getX() + board.getWidth() - border;
		float bottom = board.getY();
		float top = board.getY() + board.getHeight() - border;

		r.rects.add(new Rectangle(left, bottom, width, border));
		r.rects.add(new Rectangle(left, top, width, border));
		r.rects.add(new Rectangle(left, bottom, border, height));
		r.rects.add(new Rectangle(right, bottom, border, height));

		float separatorAmount = 32;
		float separatorWidth = 1;
		float separatorHeight = height / (separatorAmount * 4);
		float separatorX = x + width / 2 - separatorWidth / 2;

		float yStep = (height - separatorHeight) / (separatorAmount - 1);

		for (int i = 0; i < separatorAmount; i++) {

			float separatorY = y + i * yStep;

			r.rects.add(new Rectangle(separatorX, separatorY, separatorWidth,
					separatorHeight));
		}

		r.type = ShapeType.Filled;
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable countDownMsg(float number) {
		StringRenderable r = new StringRenderable();
		r.font = Fonts.fontLarge;
		r.color = Color.WHITE;
		r.color = new Color(r.color.r, r.color.g, r.color.b, number
				- (int) number);
		r.text = Integer.toString((int) number);
		float midPointX = x + width / 2;
		float midPointY = y + height / 2;
		float xOffset = midPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = midPointY + (r.font.getBounds(r.text).height / 2);
		r.pos = new Vector2(xOffset, yOffset);
		return r;
	}

	public StringRenderable matchWinnerMsg(String name) {
		StringRenderable r = new StringRenderable();
		r.text = name + " is the winner!";
		r.font = Fonts.fontSmall;
		float midPointX = x + width / 2;
		float midPointY = y + height / 2;
		float xOffset = midPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = midPointY - (r.font.getBounds(r.text).height / 2);
		r.pos = new Vector2(xOffset, yOffset);
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable leftSideWaitingMsg() {
		StringRenderable r = new StringRenderable();
		r.text = "Waiting for player...";
		r.font = Fonts.fontSmall;
		float leftMidPointX = x + width / 4;
		float xOffset = leftMidPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = y + height / 2;
		r.pos = new Vector2(xOffset, yOffset);
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable rightSideWaitingMsg() {
		StringRenderable r = new StringRenderable();
		r.text = "Waiting for player...";
		r.font = Fonts.fontSmall;
		float rightMidPointX = x + width / 4 * 3;
		float xOffset = rightMidPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = y + height / 2;
		r.pos = new Vector2(xOffset, yOffset);
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable leftPlayerName() {
		StringRenderable r = new StringRenderable();
		float topOffset = height / 30;
		r.pos = new Vector2(x, height + y - topOffset);
		r.font = Fonts.fontSmall;
		r.text = leftPlayer.name;
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable rightPlayerName() {
		StringRenderable r = new StringRenderable();
		float topOffset = height / 30;
		r.pos = new Vector2(x + width / 2, height + y - topOffset);
		r.font = Fonts.fontSmall;
		r.text = rightPlayer.name;
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable leftPlayerScore() {
		StringRenderable r = new StringRenderable();
		float playerScoreXoffset = width / 4;
		float playerScoreYoffset = height - height / 12;
		r.pos = new Vector2(x + playerScoreXoffset, y + playerScoreYoffset);
		r.font = Fonts.fontMedium;
		r.text = Integer.toString(leftPlayer.score);
		r.color = Color.WHITE;
		return r;
	}

	public StringRenderable rightPlayerScore() {
		StringRenderable r = new StringRenderable();
		float playerScoreXoffset = width / 4;
		float playerScoreYoffset = height - height / 12;
		r.pos = new Vector2(x + playerScoreXoffset * 3, y + playerScoreYoffset);
		r.font = Fonts.fontMedium;
		r.text = Integer.toString(rightPlayer.score);
		r.color = Color.WHITE;
		return r;
	}

}
