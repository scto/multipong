package multipong.screens;

import java.util.List;

import multipong.board.Board;
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

		Match firstBoard = new Match(0, 0, width, height);
		handler.addMatch(firstBoard);

		Gdx.app.debug("First board", firstBoard.toString());
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
			Board state = match.board;

			font.draw(batch, Integer.toString(state.leftPlayer.score),
					state.leftPlayerScore.x, state.leftPlayerScore.y);
			font.draw(batch, Integer.toString(state.rightPlayer.score),
					state.rightPlayerScore.x, state.rightPlayerScore.y);

			font.draw(batch, state.leftPlayer.name, state.leftPlayerName.x,
					state.leftPlayerName.y);
			font.draw(batch, state.rightPlayer.name, state.rightPlayerName.x,
					state.rightPlayerName.y);
		}
		batch.end();

		renderer.begin(ShapeType.Line);
		for (Match match : handler.getVisibleMatches()) {
			if (!match.isPlayable()) {
				continue;
			}
			Board state = match.board;

			renderer.setColor(Color.WHITE);
			renderer.rect(state.field.bounds.x+1, state.field.bounds.y,
					state.field.bounds.width-1, state.field.bounds.height-1);

			renderer.setColor(Color.GRAY);
			for (Vector2 separator : state.separatorPos) {
				renderer.rect(separator.x, separator.y, 0,
						state.separatorLength);
			}
		}
		renderer.end();

		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		for (Match match : handler.getVisibleMatches()) {
			if (!match.isPlayable()) {
				continue;
			}
			Board state = match.board;

			renderer.rect(state.leftPlayerPad.bounds.x,
					state.leftPlayerPad.bounds.y,
					state.leftPlayerPad.bounds.width,
					state.leftPlayerPad.bounds.height);
			renderer.rect(state.rightPlayerPad.bounds.x,
					state.rightPlayerPad.bounds.y,
					state.rightPlayerPad.bounds.width,
					state.rightPlayerPad.bounds.height);

			renderer.rect(state.ball.bounds.x, state.ball.bounds.y,
					state.ball.bounds.width, state.ball.bounds.height);

		}
		renderer.end();
		stateTime += deltaTime;
	}
}
