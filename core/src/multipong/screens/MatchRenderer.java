package multipong.screens;

import java.util.List;

import multipong.match.Match;
import multipong.settings.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MatchRenderer {

	BitmapFont font = Fonts.fontSmall;
	SpriteBatch batch = new SpriteBatch();
	ShapeRenderer renderer = new ShapeRenderer();

	int width, height;
	float stateTime = 0;

	Texture bkgTex;
	ShaderProgram noiseShader;
	ShaderProgram vignetteShader;

	List<Match> visibleMatches;

	Camera camera;

	Color foregroundObjectColor = Color.WHITE;
	Color backgroundColor = Color.BLACK;

	public MatchRenderer(Camera camera, List<Match> visibleMatches) {
		this.camera = camera;
		this.visibleMatches = visibleMatches;

		width = (int) camera.viewportWidth;
		height = (int) camera.viewportHeight;

		bkgTex = new Texture(width, height, Format.RGBA8888);
		ShaderProgram.pedantic = false;
		noiseShader = Shaders.loadNoiseShader();
		vignetteShader = Shaders.loadVignetteShader();
	}

	public void dispose() {
		renderer.dispose();
		batch.dispose();
		font.dispose();
		noiseShader.dispose();
		vignetteShader.dispose();
	}

	Vector2 o = new Vector2(0, 0);

	public void render(float deltaTime) {
		stateTime += deltaTime;

		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float colorBlurOpacity = 0.9f;

		foregroundObjectColor = Color.GREEN;
		foregroundObjectColor.a = colorBlurOpacity;
		o.x = -1;
		o.y = 0;
		renderBoards(deltaTime);

		foregroundObjectColor = Color.MAGENTA;
		foregroundObjectColor.a = colorBlurOpacity;
		o.x = 0;
		o.y = 0.5f;
		renderBoards(deltaTime);

		foregroundObjectColor = Color.CYAN;
		foregroundObjectColor.a = colorBlurOpacity;
		o.x = 0;
		o.y = -1;
		renderBoards(deltaTime);

		foregroundObjectColor = Color.WHITE;
		o.x = 0;
		o.y = 0;
		renderBoards(deltaTime);

		batch.begin();
		vignetteShader.begin();
		vignetteShader.setUniformf("resolution", width, height);
		vignetteShader.end();
		batch.setShader(vignetteShader);
		batch.draw(bkgTex, 0, 0);
		batch.setShader(null);
		batch.end();
		
		batch.begin();
		noiseShader.begin();
		noiseShader.setUniformf("time", stateTime);
		noiseShader.setUniformf("resolution", width, height);
		noiseShader.end();
		batch.setShader(noiseShader);
		batch.draw(bkgTex, 0, 0);
		batch.setShader(null);
		batch.end();
		

	}

	private void enableBlend() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void disableBlend() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void renderBoards(float deltaTime) {

		for (Match match : visibleMatches) {

			renderFields(match);

			if (match.isFinished()) {
				renderMatchWinner(match);

			} else if (match.isPlayable()) {
				renderLeftName(match);
				renderRightName(match);
				renderScores(match);
				renderLeftPad(match);
				renderRightPad(match);

				if (match.hasStartCountDown()) {
					float number = (Settings.timeMatchStartCountDownFrom + 1 - match.stateTime);
					renderCountDown(number, match.board.midPointX,
							match.board.midPointY);
				} else if (match.hasRedrawCountDown()) {
					float number = (match.redrawCountDown + 1);
					renderCountDown(number, match.board.midPointX,
							match.board.midPointY);
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

	private void renderBall(Match match) {
		enableBlend();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(foregroundObjectColor);
		renderer.rect(match.board.ball.getX() + o.x, match.board.ball.getY()
				+ o.y, match.board.ball.getWidth() + o.x,
				match.board.ball.getHeight() + o.y);
		renderer.end();
		disableBlend();
	}

	private void renderCountDown(float number, float midPointX, float midPointY) {
		batch.begin();
		font = Fonts.fontLarge;
		Color c = foregroundObjectColor;
		font.setColor(c.r, c.g, c.b, number - (int) number);

		String str = Integer.toString((int) number);
		float xOffset = midPointX - (font.getBounds(str).width / 2);
		float yOffset = midPointY + (font.getBounds(str).height / 2);

		font.draw(batch, str, xOffset + o.x, yOffset + o.y);
		font = Fonts.fontSmall;
		font.setColor(foregroundObjectColor);
		batch.end();
	}

	private void renderFields(Match match) {
		enableBlend();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(foregroundObjectColor);

		float border = 2;

		float left = match.board.field.getLeft() + o.x;
		float right = match.board.field.getRight() - border + o.x;
		float bottom = match.board.field.getBottom() + o.y;
		float top = match.board.field.getTop() - border + o.y;
		float width = match.board.field.getWidth();
		float height = match.board.field.getHeight();

		renderer.rect(left, bottom, width, border);

		renderer.rect(left, top, width, border);

		renderer.rect(left, bottom, border, height);

		renderer.rect(right, bottom, border, height);

		for (Vector2 separator : match.board.separatorPos) {
			renderer.rect(separator.x + o.x, separator.y + o.y, 1,
					match.board.separatorLength + o.y);
		}
		renderer.end();
		disableBlend();
	}

	private void renderLeftName(Match match) {
		font.setColor(foregroundObjectColor);
		font = Fonts.fontSmall;

		batch.begin();
		float xOffset = match.board.leftMidPointX
				- (font.getBounds(match.board.leftPlayer.name).width / 2);
		font.draw(batch, match.board.leftPlayer.name, xOffset + o.x,
				match.board.leftPlayerNamePos.y + o.y);
		batch.end();
	}

	private void renderLeftPad(Match match) {
		enableBlend();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(foregroundObjectColor);
		// Draw pad
		renderer.rect(match.board.leftPlayerPad.getX() + o.x,
				match.board.leftPlayerPad.getY() + o.y,
				match.board.leftPlayerPad.getWidth() + o.x,
				match.board.leftPlayerPad.getHeight() + o.y);
		renderer.end();
		disableBlend();
	}

	private void renderLeftSideWaiting(Match match) {
		font.setColor(foregroundObjectColor);
		font = Fonts.fontSmall;

		String str = "Waiting for player...";
		float xOffset = match.board.leftMidPointX
				- (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY;
		batch.begin();
		font.draw(batch, str, xOffset + o.x, yOffset + o.y);
		batch.end();
	}

	private void renderRightName(Match match) {
		font.setColor(foregroundObjectColor);
		font = Fonts.fontSmall;

		batch.begin();
		float xOffset = match.board.rightMidPointX
				- (font.getBounds(match.board.rightPlayer.name).width / 2);
		font.draw(batch, match.board.rightPlayer.name, xOffset + o.x,
				match.board.rightPlayerNamePos.y + o.y);
		batch.end();
	}

	private void renderRightPad(Match match) {
		enableBlend();
		renderer.setColor(foregroundObjectColor);
		renderer.begin(ShapeType.Filled);
		// Draw pad
		renderer.rect(match.board.rightPlayerPad.getX() + o.x,
				match.board.rightPlayerPad.getY() + o.y,
				match.board.rightPlayerPad.getWidth() + o.x,
				match.board.rightPlayerPad.getHeight() + o.y);
		renderer.end();
		disableBlend();
	}

	private void renderRightSideWaiting(Match match) {
		font.setColor(foregroundObjectColor);
		font = Fonts.fontSmall;

		String str = "Waiting for player...";
		float xOffset = match.board.rightMidPointX
				- (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY;
		batch.begin();
		font.draw(batch, str, xOffset + o.x, yOffset + o.y);
		batch.end();
	}

	private void renderMatchWinner(Match match) {
		font.setColor(foregroundObjectColor);
		font = Fonts.fontSmall;

		batch.begin();

		String str = match.getMatchWinner().name + " is the winner!";
		float xOffset = match.board.midPointX - (font.getBounds(str).width / 2);
		float yOffset = match.board.midPointY
				- (font.getBounds(str).height / 2);

		font.draw(batch, str, xOffset + o.x, yOffset + o.y);
		batch.end();

	}

	private void renderScores(Match match) {
		font = Fonts.fontMedium;
		font.setColor(foregroundObjectColor);
		float lxOffset = (font.getBounds(Integer
				.toString(match.board.leftPlayer.score)).width / 2);
		float rxOffset = (font.getBounds(Integer
				.toString(match.board.rightPlayer.score)).width / 2);
		batch.begin();
		// Draw scores
		font.draw(batch, Integer.toString(match.board.leftPlayer.score),
				match.board.leftPlayerScore.x - lxOffset + o.x,
				match.board.leftPlayerScore.y + o.y);
		font.draw(batch, Integer.toString(match.board.rightPlayer.score),
				match.board.rightPlayerScore.x - rxOffset + o.x,
				match.board.rightPlayerScore.y + o.y);
		batch.end();
		font = Fonts.fontSmall;
	}
}
