package multipong.rendering;

import multipong.board.boardobjects.Player;
import multipong.font.Fonts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * This class creates renderable objects for a match.
 */
public class RenderableMatchObjects {

	public static RenderableRectangle ball(Rectangle ball) {
		RenderableRectangle r = new RenderableRectangle();
		r.rects.add(ball);
		r.color = Color.WHITE;
		return r;
	}

	public static RenderableRectangle leftPad(Rectangle leftPad) {
		RenderableRectangle r = new RenderableRectangle();
		r.rects.add(leftPad);
		r.color = Color.WHITE;
		return r;
	}

	public static RenderableRectangle rightPad(Rectangle rightPad) {
		RenderableRectangle r = new RenderableRectangle();
		r.rects.add(rightPad);
		r.color = Color.WHITE;
		return r;
	}

	public static RenderableRectangle field(Rectangle board) {
		RenderableRectangle r = new RenderableRectangle();

		float border = 2;
		float x = board.x;
		float y = board.y;
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

		r.color = Color.WHITE;
		return r;
	}

	public static RenderableString countDownMsg(Rectangle board, float number) {
		RenderableString r = new RenderableString();
		r.font = Fonts.fontLarge;
		r.color = Color.WHITE;
		r.color = new Color(r.color.r, r.color.g, r.color.b, number
				- (int) number);
		r.text = Integer.toString((int) number);
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float midPointX = x + width / 2;
		float midPointY = y + height / 2;
		float xOffset = midPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = midPointY + (r.font.getBounds(r.text).height / 2);
		r.pos = new Vector2(xOffset, yOffset);
		return r;
	}

	public static void updateCountDownMsg(RenderableString renderableString,
			float newNumber) {
		RenderableString r = renderableString;
		int newNumberInt = (int) newNumber;
		float newNumberDecimals = newNumber - newNumberInt;
		r.color = new Color(r.color.r, r.color.g, r.color.b, newNumberDecimals);
		r.text = Integer.toString(newNumberInt);
	}

	public static RenderableString matchWinnerMsg(Rectangle board, String name) {
		RenderableString r = new RenderableString();
		r.text = name + " is the winner!";
		r.font = Fonts.fontSmall;
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float midPointX = x + width / 2;
		float midPointY = y + height / 2;
		float xOffset = midPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = midPointY - (r.font.getBounds(r.text).height / 2);
		r.pos = new Vector2(xOffset, yOffset);
		return r;
	}

	public static RenderableString leftSideWaitingMsg(Rectangle board) {
		RenderableString r = new RenderableString();
		r.text = "Waiting for player...";
		r.font = Fonts.fontSmall;
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float leftMidPointX = x + width / 4;
		float xOffset = leftMidPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = y + height / 2;
		r.pos = new Vector2(xOffset, yOffset);
		return r;
	}

	public static RenderableString rightSideWaitingMsg(Rectangle board) {
		RenderableString r = new RenderableString();
		r.text = "Waiting for player...";
		r.font = Fonts.fontSmall;
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float rightMidPointX = x + width / 4 * 3;
		float xOffset = rightMidPointX - (r.font.getBounds(r.text).width / 2);
		float yOffset = y + height / 2;
		r.pos = new Vector2(xOffset, yOffset);
		return r;
	}

	public static RenderableString leftPlayerName(Rectangle board,
			String leftPlayerName) {
		RenderableString r = new RenderableString();
		r.font = Fonts.fontSmall;
		r.text = leftPlayerName;
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float topOffset = height / 30;
		float leftMidPointX = x + width / 4;
		float xPos = leftMidPointX - (r.font.getBounds(r.text).width / 2);
		float yPos = height + y - topOffset;
		r.pos = new Vector2(xPos, yPos);
		return r;
	}

	public static RenderableString rightPlayerName(Rectangle board,
			String rightPlayerName) {
		RenderableString r = new RenderableString();
		r.font = Fonts.fontSmall;
		r.text = rightPlayerName;
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float rightMidPointX = x + width / 4 * 3;
		float topOffset = height / 30;
		float xPos = rightMidPointX - (r.font.getBounds(r.text).width / 2);
		float yPos = height + y - topOffset;
		r.pos = new Vector2(xPos, yPos);
		return r;
	}

	public static RenderableString leftPlayerScore(Rectangle board,
			Player leftPlayer) {
		RenderableString r = new RenderableString();
		r.font = Fonts.fontMedium;
		r.text = Integer.toString(leftPlayer.score);
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float topOffset = height - height / 12;
		float leftMidPointX = x + width / 4;
		float yPos = y + topOffset;
		float xPos = leftMidPointX - (r.font.getBounds(r.text).width / 2);
		r.pos = new Vector2(xPos, yPos);
		return r;
	}

	public static RenderableString rightPlayerScore(Rectangle board,
			Player rightPlayer) {
		RenderableString r = new RenderableString();
		r.font = Fonts.fontMedium;
		r.text = Integer.toString(rightPlayer.score);
		r.color = Color.WHITE;
		float x = board.x;
		float y = board.y;
		float width = board.getWidth();
		float height = board.getHeight();
		float topOffset = height - height / 12;
		float rightMidPointX = x + width / 4 * 3;
		float yPos = y + topOffset;
		float xPos = rightMidPointX - (r.font.getBounds(r.text).width / 2);
		r.pos = new Vector2(xPos, yPos);
		return r;
	}

}
