package multipong.rendering;

import java.util.ArrayList;
import java.util.List;

import multipong.font.Fonts;
import multipong.match.Match;
import multipong.settings.Settings;
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
	ShaderProgram noisyPixelsShader;

	ShapeRenderer renderer = new ShapeRenderer();
	float stateTime = 0;
	ShaderProgram vignetteShader;

	List<Match> visibleMatches;

	int width, height;

	public MatchRenderer(Camera camera, List<Match> visibleMatches) {
		this.camera = camera;
		this.visibleMatches = visibleMatches;

		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		width = (int) camera.viewportWidth;
		height = (int) camera.viewportHeight;

		bkgTex = new Texture(width, height, Format.RGBA8888);
		ShaderProgram.pedantic = false;
		noisyPixelsShader = Shaders.loadNoisyPixelsShader();
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
		noisyPixelsShader.dispose();
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

		Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g,
				backgroundColor.b, backgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateRenderables();
		if (!Settings.shadersUseColorBleed) {
			renderStrings(allBoardsRenderableStrings);
			renderRectangles(allBoardsRenderableRectangles);
		} else {
			renderColorBleed();
		}
		renderOverlayShaders();
	}

	private void renderColorBleed() {
		Color color1 = Color.GREEN;
		Color color2 = Color.CYAN;
		Color color3 = Color.RED;

		float color1Xoffset = -1;
		float color1Yoffset = 0;
		float color2Xoffset = 0;
		float color2Yoffset = 1;
		float color3Xoffset = 0;
		float color3Yoffset = -1;

		batch.begin();
		for (RenderableString r : allBoardsRenderableStrings) {
			font = r.font;
			Color topColor = r.color;
			float topColorAlpha = r.color.a;

			r.color = color1;
			font.setColor(r.color.r, r.color.g, r.color.b, topColorAlpha);
			font.draw(batch, r.text, r.pos.x + color1Xoffset, r.pos.y
					+ color1Yoffset);

			r.color = color2;
			font.setColor(r.color.r, r.color.g, r.color.b, topColorAlpha);
			font.draw(batch, r.text, r.pos.x + color2Xoffset, r.pos.y
					+ color2Yoffset);

			r.color = color3;
			font.setColor(r.color.r, r.color.g, r.color.b, topColorAlpha);
			font.draw(batch, r.text, r.pos.x + color3Xoffset, r.pos.y
					+ color3Yoffset);

			r.color = topColor;
			font.setColor(r.color);
			font.draw(batch, r.text, r.pos.x, r.pos.y);
		}
		batch.end();

		enableBlend();
		renderer.begin(ShapeType.Filled);
		for (RenderableRectangle r : allBoardsRenderableRectangles) {
			Color topColor = r.color;
			float topColorAlpha = r.color.a;

			r.color = color1;
			renderer.setColor(r.color.r, r.color.g, r.color.b, topColorAlpha);
			for (Rectangle rectangle : r.rects) {
				renderer.rect(rectangle.x + color1Xoffset, rectangle.y
						+ color1Yoffset, rectangle.width, rectangle.height);
			}

			r.color = color2;
			renderer.setColor(r.color.r, r.color.g, r.color.b, topColorAlpha);
			for (Rectangle rectangle : r.rects) {
				renderer.rect(rectangle.x + color2Xoffset, rectangle.y
						+ color2Yoffset, rectangle.width, rectangle.height);
			}

			r.color = color3;
			renderer.setColor(r.color.r, r.color.g, r.color.b, topColorAlpha);
			for (Rectangle rectangle : r.rects) {
				renderer.rect(rectangle.x + color3Xoffset, rectangle.y
						+ color3Yoffset, rectangle.width, rectangle.height);
			}

			r.color = topColor;
			renderer.setColor(r.color);
			for (Rectangle rectangle : r.rects) {
				renderer.rect(rectangle.x, rectangle.y, rectangle.width,
						rectangle.height);
			}
		}
		renderer.end();
		disableBlend();
	}

	private void renderOverlayShaders() {
		if (Settings.shadersUseDistortion) {
			batch.begin();
			distortionShader.begin();
			distortionShader.setUniformf("time", stateTime);
			distortionShader.setUniformf("resolution", width, height);
			distortionShader.end();
			batch.setShader(distortionShader);
			batch.draw(bkgTex, 0, 0);
			batch.end();
		}
		if (Settings.shadersUseVignette) {
			batch.begin();
			vignetteShader.begin();
			vignetteShader.setUniformf("resolution", width, height);
			vignetteShader.end();
			batch.setShader(vignetteShader);
			batch.draw(bkgTex, 0, 0);
			batch.end();
		}
		if (Settings.shadersUseNoisyPixels) {
			batch.begin();
			noisyPixelsShader.begin();
			noisyPixelsShader.setUniformf("time", stateTime);
			noisyPixelsShader.setUniformf("resolution", width, height);
			noisyPixelsShader.end();
			batch.setShader(noisyPixelsShader);
			batch.draw(bkgTex, 0, 0);
			batch.end();
		}

		// batch.begin();
		// glowShader.begin();
		// glowShader.setUniformf("time", stateTime);
		// for (Match match : visibleMatches) {
		// glowShader.setUniformf("resolution", width, height);
		// float[] coords = { match.board.leftPlayerPad.getX(),
		// match.board.leftPlayerPad.getY(),
		// match.board.leftPlayerPad.getRight(),
		// match.board.leftPlayerPad.getTop() };
		// glowShader.setUniform4fv("rect", coords, 0, 4);
		// }
		// glowShader.end();
		// batch.setShader(glowShader);
		// batch.draw(bkgTex, 0, 0);
		// batch.end();

		batch.begin();
		batch.setShader(null);
		batch.end();
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

	public void updateRenderables() {
		allBoardsRenderableRectangles.clear();
		allBoardsRenderableStrings.clear();
		for (Match match : visibleMatches) {
			allBoardsRenderableRectangles.addAll(match.renderableRectangles);
			allBoardsRenderableStrings.addAll(match.renderableStrings);
		}
	}

}
