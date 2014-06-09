package multipong;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class BoardRenderer {

	Board board;
	OrthographicCamera camera;
	BitmapFont font;
	SpriteBatch batch = new SpriteBatch();
	ShapeRenderer renderer = new ShapeRenderer();

	float stateTime = 0;

	public BoardRenderer(Board board) {
		this.board = board;

		camera = new OrthographicCamera(board.field.getWidth(),
				board.field.getHeight());
		camera.setToOrtho(true);
		camera.position.set(board.field.getWidth(), board.field.getHeight(), 0);

		font = new BitmapFont(true);

	}

	public void render(float deltaTime) {

		camera.update();

		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		font.setScale(3, 2);
		font.draw(batch, Integer.toString(board.leftPlayer.score),
				board.leftPlayerScore.x, board.leftPlayerScore.y);
		font.draw(batch, Integer.toString(board.rightPlayer.score),
				board.rightPlayerScore.x, board.rightPlayerScore.y);
		font.setScale(1.5f, 1);
		font.draw(batch, board.leftPlayer.name, board.leftPlayerName.x,
				board.leftPlayerName.y);
		font.draw(batch, board.rightPlayer.name, board.rightPlayerName.x,
				board.rightPlayerName.y);
		batch.end();

		renderer.begin(ShapeType.Line);
		renderer.setColor(Color.WHITE);
		renderer.rect(board.field.bounds.x + 1, board.field.bounds.y,
				board.field.bounds.width, board.field.bounds.height);
		for (Vector2 separator : board.separatorPos) {
			renderer.rect(separator.x, separator.y, 0, board.separatorLength);
		}
		renderer.end();

		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.WHITE);
		renderer.rect(board.leftPlayerPad.bounds.x,
				board.leftPlayerPad.bounds.y, board.leftPlayerPad.bounds.width,
				board.leftPlayerPad.bounds.height);
		renderer.rect(board.rightPlayerPad.bounds.x,
				board.rightPlayerPad.bounds.y,
				board.rightPlayerPad.bounds.width,
				board.rightPlayerPad.bounds.height);
		renderer.rect(board.ball.bounds.x, board.ball.bounds.y,
				board.ball.bounds.width, board.ball.bounds.height);
		renderer.end();

		stateTime += deltaTime;
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		renderer.dispose();
	}
}
