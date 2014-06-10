package multipong.board;

import java.util.List;

import multipong.match.Match;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class BoardRenderer {

	List<Match> visibleMatches;
	BitmapFont font;
	SpriteBatch batch = new SpriteBatch();
	ShapeRenderer renderer = new ShapeRenderer();
	OrthographicCamera camera = new OrthographicCamera();

	float stateTime = 0;

	public BoardRenderer(int appWidth, int appHeight, List<Match> visibleMatches) {
		this.visibleMatches = visibleMatches;

		font = new BitmapFont(true);

		camera = new OrthographicCamera(appWidth, appHeight);
		camera.setToOrtho(true);
		camera.position.set(appWidth / 2, appHeight / 2, 0);
		camera.update();
	}

	public void render(float deltaTime) {

		for (Match match : visibleMatches) {

			if (!match.isPlayable()) {
				continue;
			}

			camera.update();
			Board state = match.board;

			renderer.setProjectionMatrix(camera.combined);
			batch.setProjectionMatrix(camera.combined);

			batch.begin();
			font.draw(batch, Integer.toString(state.leftPlayer.score),
					state.leftPlayerScore.x, state.leftPlayerScore.y);
			font.draw(batch, Integer.toString(state.rightPlayer.score),
					state.rightPlayerScore.x, state.rightPlayerScore.y);
			font.draw(batch, state.leftPlayer.name, state.leftPlayerName.x,
					state.leftPlayerName.y);
			font.draw(batch, state.rightPlayer.name, state.rightPlayerName.x,
					state.rightPlayerName.y);
			batch.end();

			renderer.begin(ShapeType.Line);
			renderer.setColor(Color.WHITE);
			renderer.rect(state.field.bounds.x + 1, state.field.bounds.y,
					state.field.bounds.width, state.field.bounds.height);
			for (Vector2 separator : state.separatorPos) {
				renderer.rect(separator.x, separator.y, 0,
						state.separatorLength);
			}
			renderer.end();

			renderer.begin(ShapeType.Filled);
			renderer.setColor(Color.WHITE);
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
			renderer.end();
		}

		stateTime += deltaTime;
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		renderer.dispose();
	}
}
