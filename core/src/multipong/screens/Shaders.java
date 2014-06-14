package multipong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {

	public static ShaderProgram loadNoiseShader() {
		String vert = Gdx.files.local("noisy_crt.vert").readString();
		String frag = Gdx.files.local("noisy_crt.frag").readString();
		ShaderProgram shader = new ShaderProgram(vert, frag);
		ShaderProgram.pedantic = false;
		if (!shader.isCompiled()) {
			Gdx.app.debug("NoiseShader", "\n" + shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length() != 0) {
			Gdx.app.debug("NoiseShader", "\n" + shader.getLog());
		}
		return shader;
	}

	public static ShaderProgram loadVignetteShader() {
		String vert = Gdx.files.local("vignette.vert").readString();
		String frag = Gdx.files.local("vignette.frag").readString();
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
		String vert = Gdx.files.local("distortion.vert").readString();
		String frag = Gdx.files.local("distortion.frag").readString();
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

}
