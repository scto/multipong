package multipong.screens;

import java.util.List;

import multipong.match.Match;
import multipong.matchhandlers.DropInMatchHandler;
import multipong.settings.Settings;

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

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		update(deltaTime);

		renderBoards(deltaTime);
		stateTime += deltaTime;
		
		if (handler.allVisibleMatchesAreFinished()) {
			
		}
	}

	private void renderBall(Match match) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		renderer.rect(match.board.ball.getX(), match.board.ball.getY(),
				match.board.ball.getWidth(), match.board.ball.getHeight());
		renderer.end();
	}

	private void renderBoards(float deltaTime) {

		for (Match match : handler.getVisibleMatches()) {

			renderFields(match);

			if (match.isFinished()) {
				renderRoundWinner(match);
				
			} else if (match.isPlayable()) {
				renderLeftName(match);
				renderRightName(match);
				renderScores(match);
				renderLeftPad(match);
				renderRightPad(match);

				if (match.isCountingDown()) {
					renderCountDown(match);
				} else {
					renderBall(match);
				}

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

	private void renderRoundWinner(Match match) {
		batch.begin();
		font.setScale(2f);

		String str = match.getMatchWinner().name + " is the winner!";
		float xOffset = match.board.midPointX - (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY
				- (font.getBounds(str).height / 2);

		font.draw(batch, str, xOffset, yOffset);
		font.setScale(1);
		batch.end();

	}

	private void renderCountDown(Match match) {
		batch.begin();
		font.setScale(5f);

		String str = Integer
				.toString((int) (Settings.matchStartCountDownFrom + 1 - match.stateTime));
		float xOffset = match.board.midPointX - (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY
				- (font.getBounds(str).height / 2);

		font.draw(batch, str, xOffset, yOffset);
		font.setScale(1);
		batch.end();
	}

	private void renderFields(Match match) {
		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.WHITE);
		// Draw field
		renderer.rect(match.board.field.getX() + 1, match.board.field.getY(),
				match.board.field.getWidth() - 1,
				match.board.field.getHeight() - 1);

		renderer.setColor(Color.GRAY);
		for (Vector2 separator : match.board.separatorPos) {
			renderer.rect(separator.x, separator.y, 0,
					match.board.separatorLength);
		}
		renderer.end();
	}

	private void renderLeftName(Match match) {
		batch.begin();
		font.draw(batch, match.board.leftPlayer.name,
				match.board.leftPlayerNamePos.x,
				match.board.leftPlayerNamePos.y);
		batch.end();
	}

	private void renderLeftPad(Match match) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		// Draw pad
		renderer.rect(match.board.leftPlayerPad.getX(),
				match.board.leftPlayerPad.getY(),
				match.board.leftPlayerPad.getWidth(),
				match.board.leftPlayerPad.getHeight());
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

	private void renderRightName(Match match) {
		batch.begin();
		font.draw(batch, match.board.rightPlayer.name,
				match.board.rightPlayerNamePos.x,
				match.board.rightPlayerNamePos.y);
		batch.end();
	}

	private void renderRightPad(Match match) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		// Draw pad
		renderer.rect(match.board.rightPlayerPad.getX(),
				match.board.rightPlayerPad.getY(),
				match.board.rightPlayerPad.getWidth(),
				match.board.rightPlayerPad.getHeight());
		renderer.end();
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

	private void renderScores(Match match) {

		batch.begin();
		// Draw scores
		font.draw(batch, Integer.toString(match.board.leftPlayer.score),
				match.board.leftPlayerScore.x, match.board.leftPlayerScore.y);
		font.draw(batch, Integer.toString(match.board.rightPlayer.score),
				match.board.rightPlayerScore.x, match.board.rightPlayerScore.y);
		batch.end();
	}

	private void update(float deltaTime) {
		KeyMap newPlayerKeys = getPressedKeyMap();

		if (newPlayerKeys != null) {
			handler.addNewPlayer(newPlayerKeys);
		}
		handler.showBoardsInHiddenMatches();
		handler.updateBoardsInVisibleMatches(deltaTime);
	}

}
