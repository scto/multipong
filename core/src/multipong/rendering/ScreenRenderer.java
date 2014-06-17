package multipong.rendering;

import java.util.ArrayList;
import java.util.List;

import multipong.font.Fonts;
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

public class ScreenRenderer {

	private List<RenderableRectangle> allRenderableRectangles = new ArrayList<RenderableRectangle>();
	private List<RenderableString> allRenderableStrings = new ArrayList<RenderableString>();
	private Color bkgColor = Color.BLACK;

	private SpriteBatch batch = new SpriteBatch();
	private Texture bkgTex;

	private ShaderProgram distortionShader;
	private BitmapFont font = Fonts.fontSmall;
	private ShaderProgram glowShader;
	private ShaderProgram noisyPixelsShader;
	private ShaderProgram vignetteShader;

	private ShapeRenderer renderer = new ShapeRenderer();
	private float stateTime = 0;

	int width, height;

	public ScreenRenderer(Camera camera) {

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

		Gdx.gl.glClearColor(bkgColor.r, bkgColor.g, bkgColor.b, bkgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!Settings.shadersUseColorBleed) {
			renderStrings();
			renderRectangles();
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
		for (RenderableString r : allRenderableStrings) {
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
		for (RenderableRectangle r : allRenderableRectangles) {
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

		batch.begin();
		batch.setShader(null);
		batch.end();
	}

	private void renderRectangles() {
		enableBlend();
		renderer.begin(ShapeType.Filled);
		for (RenderableRectangle r : allRenderableRectangles) {
			renderer.setColor(r.color);
			for (Rectangle rectangle : r.rects) {
				renderer.rect(rectangle.x, rectangle.y, rectangle.width,
						rectangle.height);
			}
		}
		renderer.end();
		disableBlend();
	}

	private void renderStrings() {
		batch.begin();
		for (RenderableString r : allRenderableStrings) {
			font = r.font;
			font.setColor(r.color);
			font.draw(batch, r.text, r.pos.x, r.pos.y);
		}
		batch.end();
	}

	public void setRenderables(List<RenderableRectangle> rectangles,
			List<RenderableString> strings) {
		allRenderableRectangles.clear();
		allRenderableStrings.clear();
		if (rectangles != null) {
			allRenderableRectangles.addAll(rectangles);
		}
		if (strings != null) {
			allRenderableStrings.addAll(strings);
		}
	}

}
