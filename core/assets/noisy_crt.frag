uniform sampler2D u_texture;

varying vec4 vColor;
varying vec2 vTexCoord;
uniform float time;
uniform vec2 resolution;

float rand(vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float smooth(float n) {
	return n/2.0 + (n-1)/4.0 + (n+1)/4.0;
}

void main() {

	// The noisy big pixels //
	
	float x = float(int(vTexCoord.y*resolution.y));
	float y = float(int(vTexCoord.x*resolution.x));

	float r = rand(vec2(x,y));

	float t = float(time - int(time))*2567.0;
	float rt = float(t-int(t));
	float v = rand(vec2(rt*x, rt*y));
	v = float(v-int(v));
	
	bool noise = (mod(x, 6.0) < 1.0) || (mod(y, 4.0) < 1.0);
	bool less_noise = (mod(x, 6.0) > 4.0) || (mod(y, 4.0) < 2.0);
	
	float n = smooth(smooth(v));
	
	if (noise) {
		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.6-n*0.5);
	} else if (less_noise) {
		gl_FragColor = vec4(0.0, 0.0, 0.0, n*0.3);
	} else {
		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
	}
	
}

