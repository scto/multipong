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
	}

	private KeyMap getPressedKeyMap() {
		for (KeyMap keys : availableKeyMaps) {
			if (Gdx.input.isKeyPressed(keys.enterKey)) {
				// TODO: simultaneous press possible, use a list probably
				availableKeyMaps.remove(keys);
				return keys;
			}
		}
		return null;
	}

	@Override
	public void hide() {
		renderer.dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		renderer.dispose();
	}

	public void renderBoards(float deltaTime) {

		batch.begin();
		for (Match match : handler.getVisibleMatches()) {
			if (!match.isPlayable()) {
				continue;
			}

			font.draw(batch, Integer.toString(match.board.leftPlayer.score),
					match.board.leftPlayerScore.x,
					match.board.leftPlayerScore.y);
			font.draw(batch, Integer.toString(match.board.rightPlayer.score),
					match.board.rightPlayerScore.x,
					match.board.rightPlayerScore.y);

			font.draw(batch, match.board.leftPlayer.name,
					match.board.leftPlayerName.x, match.board.leftPlayerName.y);
			font.draw(batch, match.board.rightPlayer.name,
					match.board.rightPlayerName.x,
					match.board.rightPlayerName.y);
		}
		batch.end();

		renderer.begin(ShapeType.Line);
		for (Match match : handler.getVisibleMatches()) {
			if (!match.isPlayable()) {
				continue;
			}

			renderer.setColor(Color.WHITE);
			renderer.rect(match.board.field.bounds.x + 1,
					match.board.field.bounds.y,
					match.board.field.bounds.width - 1,
					match.board.field.bounds.height - 1);

			renderer.setColor(Color.GRAY);
			for (Vector2 separator : match.board.separatorPos) {
				renderer.rect(separator.x, separator.y, 0,
						match.board.separatorLength);
			}
		}
		renderer.end();

		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		for (Match match : handler.getVisibleMatches()) {
			if (!match.isPlayable()) {
				continue;
			}

			renderer.rect(match.board.leftPlayerPad.bounds.x,
					match.board.leftPlayerPad.bounds.y,
					match.board.leftPlayerPad.bounds.width,
					match.board.leftPlayerPad.bounds.height);
			renderer.rect(match.board.rightPlayerPad.bounds.x,
					match.board.rightPlayerPad.bounds.y,
					match.board.rightPlayerPad.bounds.width,
					match.board.rightPlayerPad.bounds.height);

			renderer.rect(match.board.ball.bounds.x, match.board.ball.bounds.y,
					match.board.ball.bounds.width,
					match.board.ball.bounds.height);

		}
		renderer.end();
		stateTime += deltaTime;
	}
}
