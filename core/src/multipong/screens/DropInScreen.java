package multipong.screens;

import java.util.List;

import multipong.match.Match;
import multipong.matchhandlers.DropInMatchHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class DropInScreen extends AbstractScreen {

	List<KeyMap> availableKeyMaps;
	String className = this.getClass().getSimpleName();
	DropInMatchHandler handler;

	public DropInScreen(Game game, int width, int height) {
		super(game, width, height);
		availableKeyMaps = loadKeyMaps();
		handler = new DropInMatchHandler(width, height);
	}

	private void update(float deltaTime) {
		KeyMap newPlayerKeys = getPressedKeyMap();

		if (newPlayerKeys != null) {
			handler.addNewPlayer(newPlayerKeys);
		}
		handler.showBoardsInHiddenMatches();
		handler.updateBoardsInVisibleMatches(deltaTime);
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		update(deltaTime);

		renderBoards(deltaTime);
		stateTime += deltaTime;
	}

	private KeyMap getPressedKeyMap() {
		for (KeyMap keys : availableKeyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey) && stateTime > keyDelay) {
				// TODO: simultaneous press possible, use a list probably
				availableKeyMaps.remove(keys);
				return keys;
			}
		}
		return null;
	}

	private void renderBoards(float deltaTime) {

		for (Match match : handler.getVisibleMatches()) {

			renderFields(match);

			if (match.isPlayable()) {
				renderLeftName(match);
				renderRightName(match);
				renderScores(match);
				renderLeftPad(match);
				renderRightPad(match);
				renderBall(match);

			} else if (match.hasLeftPlayer() && !match.hasRightPlayer()) {
				// Show "waiting for other player" on right side.
				renderRightSideWaiting(match);
				renderLeftPad(match);
				renderLeftName(match);

			} else if (!match.hasLeftPlayer() && !match.hasLeftPlayer()) {
				// Show "waiting for player" on both sides.
				renderLeftSideWaiting(match);
				renderRightSideWaiting(match);
			}
		}

	}

	private void renderFields(Match match) {
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.WHITE);
		// Draw field
		renderer.rect(match.board.field.bounds.x + 1,
				match.board.field.bounds.y, match.board.field.bounds.width - 1,
				match.board.field.bounds.height - 1);

		renderer.setColor(Color.GRAY);
		for (Vector2 separator : match.board.separatorPos) {
			renderer.rect(separator.x, separator.y, 0,
					match.board.separatorLength);
		}
		renderer.end();
	}

	private void renderLeftSideWaiting(Match match) {
		String str = "Waiting for player...";
		float xOffset = match.board.leftMidPointX
				- (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY;
		batch.begin();
		font.draw(batch, str, xOffset, yOffset);
		batch.end();
	}

	private void renderRightSideWaiting(Match match) {
		String str = "Waiting for player...";
		float xOffset = match.board.rightMidPointX
				- (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY;
		batch.begin();
		font.draw(batch, str, xOffset, yOffset);
		batch.end();
	}

	private void renderLeftPad(Match match) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		// Draw pad
		renderer.rect(match.board.leftPlayerPad.bounds.x,
				match.board.leftPlayerPad.bounds.y,
				match.board.leftPlayerPad.bounds.width,
				match.board.leftPlayerPad.bounds.height);
		renderer.end();
	}

	private void renderRightPad(Match match) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		// Draw pad
		renderer.rect(match.board.rightPlayerPad.bounds.x,
				match.board.rightPlayerPad.bounds.y,
				match.board.rightPlayerPad.bounds.width,
				match.board.rightPlayerPad.bounds.height);
		renderer.end();
	}

	private void renderLeftName(Match match) {
		batch.begin();
		font.draw(batch, match.board.leftPlayer.name,
				match.board.leftPlayerNamePos.x,
				match.board.leftPlayerNamePos.y);
		batch.end();
	}

	private void renderRightName(Match match) {
		batch.begin();
		font.draw(batch, match.board.rightPlayer.name,
				match.board.rightPlayerNamePos.x,
				match.board.rightPlayerNamePos.y);
		batch.end();
	}

	private void renderScores(Match match) {

		batch.begin();
		// Draw scores
		font.draw(batch, Integer.toString(match.board.leftPlayer.score),
				match.board.leftPlayerScore.x, match.board.leftPlayerScore.y);
		font.draw(batch, Integer.toString(match.board.rightPlayer.score),
				match.board.rightPlayerScore.x, match.board.rightPlayerScore.y);
		batch.end();
	}

	private void renderBall(Match match) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		renderer.rect(match.board.ball.bounds.x, match.board.ball.bounds.y,
				match.board.ball.bounds.width, match.board.ball.bounds.height);
		renderer.end();
	}

}
