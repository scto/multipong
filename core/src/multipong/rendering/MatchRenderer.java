package multipong.rendering;

import java.util.ArrayList;
import java.util.List;

import multipong.font.Fonts;
import multipong.match.Match;
import multipong.utils.Shaders;

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

public class MatchRenderer {

	List<RenderableRectangle> allBoardsRenderableRectangles = new ArrayList<RenderableRectangle>();
	List<RenderableString> allBoardsRenderableStrings = new ArrayList<RenderableString>();
	Color backgroundColor = Color.BLACK;

	SpriteBatch batch = new SpriteBatch();
	Texture bkgTex;

	Camera camera;
	ShaderProgram distortionShader;
	BitmapFont font = Fonts.fontSmall;
	ShaderProgram glowShader;
	ShaderProgram noiseShader;

	ShapeRenderer renderer = new ShapeRenderer();
	float stateTime = 0;
	ShaderProgram vignetteShader;

	List<Match> visibleMatches;

	int width, height;

	public MatchRenderer(Camera camera, List<Match> visibleMatches) {
		this.camera = camera;
		this.visibleMatches = visibleMatches;

		width = (int) camera.viewportWidth;
		height = (int) camera.viewportHeight;

		bkgTex = new Texture(width, height, Format.RGBA8888);
		ShaderProgram.pedantic = false;
		noiseShader = Shaders.loadNoiseShader();
		vignetteShader = Shaders.loadVignetteShader();
		distortionShader = Shaders.loadDistortionShader();
		glowShader = Shaders.loadGlowShader();
	}

	private void disableBlend() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void dispose() {
		renderer.dispose();
		batch.dispose();
		font.dispose();
		noiseShader.dispose();
		vignetteShader.dispose();
		distortionShader.dispose();
		glowShader.dispose();
	}

	private void enableBlend() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void render(float deltaTime) {
		stateTime += deltaTime;

		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		allBoardsRenderableRectangles.clear();
		allBoardsRenderableStrings.clear();
		for (Match match : visibleMatches) {
			allBoardsRenderableRectangles.addAll(match.renderableRectangles);
			allBoardsRenderableStrings.addAll(match.renderableStrings);
		}
		renderStrings(allBoardsRenderableStrings);
		renderRectangles(allBoardsRenderableRectangles);
		renderShaderOverlays();
	}

	private void renderRectangles(List<RenderableRectangle> renderableRectangles) {
		enableBlend();
		renderer.begin(ShapeType.Filled);
		for (RenderableRectangle renderableRectangle : renderableRectangles) {
			renderer.setColor(renderableRectangle.color);
			for (Rectangle rectangle : renderableRectangle.rects) {
				renderer.rect(rectangle.x, rectangle.y, rectangle.width,
						rectangle.height);
			}
		}
		renderer.end();
		disableBlend();
	}

	private void renderShaderOverlays() {

		batch.begin();
		distortionShader.begin();
		distortionShader.setUniformf("time", stateTime);
		distortionShader.setUniformf("resolution", width, height);
		distortionShader.end();
		batch.setShader(distortionShader);
		batch.draw(bkgTex, 0, 0);
		batch.end();

		batch.begin();
		vignetteShader.begin();
		vignetteShader.setUniformf("resolution", width, height);
		vignetteShader.end();
		batch.setShader(vignetteShader);
		batch.draw(bkgTex, 0, 0);
		batch.end();

		batch.begin();
		noiseShader.begin();
		noiseShader.setUniformf("time", stateTime);
		noiseShader.setUniformf("resolution", width, height);
		noiseShader.end();
		batch.setShader(noiseShader);
		batch.draw(bkgTex, 0, 0);
		batch.end();

		batch.begin();
		glowShader.begin();
		glowShader.setUniformf("time", stateTime);
		glowShader.setUniformf("resolution", width, height);
		glowShader.end();
		batch.setShader(glowShader);
		batch.draw(bkgTex, 0, 0);
		batch.end();

		batch.begin();
		batch.setShader(null);
		batch.end();
	}

	private void renderStrings(List<RenderableString> renderableStrings) {
		batch.begin();
		for (RenderableString renderableString : renderableStrings) {
			font = renderableString.font;
			font.setColor(renderableString.color);
			font.draw(batch, renderableString.text, renderableString.pos.x,
					renderableString.pos.y);
		}
		batch.end();
	}

}
