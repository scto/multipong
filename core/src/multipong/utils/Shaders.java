package multipong.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {

	public static ShaderProgram loadNoisyPixelsShader() {
		String vert = Gdx.files.local("shaders/common.vert").readString();
		String frag = Gdx.files.local("shaders/noisy_pixels.frag").readString();
		ShaderProgram shader = new ShaderProgram(vert, frag);
		ShaderProgram.pedantic = false;
		if (!shader.isCompiled()) {
			Gdx.app.debug("NoisyPixelsShader", "\n" + shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length() != 0) {
			Gdx.app.debug("NoisyPixelsShader", "\n" + shader.getLog());
		}
		return shader;
	}

	public static ShaderProgram loadVignetteShader() {
		String vert = Gdx.files.local("shaders/common.vert").readString();
		String frag = Gdx.files.local("shaders/vignette.frag").readString();
		ShaderProgram shader = new ShaderProgram(vert, frag);
		ShaderProgram.pedantic = false;
		if (!shader.isCompiled()) {
			Gdx.app.debug("VignetteShader", "\n" + shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length() != 0) {
			Gdx.app.debug("VignetteShader", "\n" + shader.getLog());
		}
		return shader;
	}

	public static ShaderProgram loadDistortionShader() {
		String vert = Gdx.files.local("shaders/common.vert").readString();
		String frag = Gdx.files.local("shaders/distortion.frag").readString();
		ShaderProgram shader = new ShaderProgram(vert, frag);
		ShaderProgram.pedantic = false;
		if (!shader.isCompiled()) {
			Gdx.app.debug("DistortionShader", "\n" + shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length() != 0) {
			Gdx.app.debug("DistortionShader", "\n" + shader.getLog());
		}
		return shader;
	}
	
	public static ShaderProgram loadGlowShader() {
		String vert = Gdx.files.local("shaders/common.vert").readString();
		String frag = Gdx.files.local("shaders/glow.frag").readString();
		ShaderProgram shader = new ShaderProgram(vert, frag);
		ShaderProgram.pedantic = false;
		if (!shader.isCompiled()) {
			Gdx.app.debug("GlowShader", "\n" + shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length() != 0) {
			Gdx.app.debug("GlowShader", "\n" + shader.getLog());
		}
		return shader;
	}

}
