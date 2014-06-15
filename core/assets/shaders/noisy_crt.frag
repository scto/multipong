uniform sampler2D u_texture;

varying vec4 vColor;
varying vec2 vTexCoord;
uniform float time;
uniform vec2 resolution;

//RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.75;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.9;


float rand(vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float smooth(float n) {
	return n*0.5 + (n-1.0)*0.25 + (n+1.0)*0.25;
}

float decimals(float n) {
	return n - float(int(n));
}

void main() {

	// The noisy big pixels //
	
	float x = float(int(vTexCoord.y*resolution.y));
	float y = float(int(vTexCoord.x*resolution.x));
	
	float t = decimals(time)*2567.0;
	float rt = decimals(t);
	
	float r = rand(vec2(x,y));
	float v = rand(vec2(rt*x, rt*y));
	float n = smooth(smooth(decimals(v)));
	
	// Vignette effect for highlights //
	
	vec2 position = vec2(vTexCoord.y , vTexCoord.x) - vec2(0.5, 0.5);
	vec2 position1 = vec2(position.x, position.y*0.8);
	float len = length(position1);
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	
	// Intervals //
	
	bool grid = (mod(x, 6.0) < 1.0) || (mod(y, 4.0) < 1.0);
	bool highlight = ((mod(x, 6.0) < 2.0) || (mod(y, 4.0) < 2.0)) && ((mod(x, 6.0) > 2.0) || (mod(y, 4.0) > 2.0));
	bool shadows = (mod(x, 6.0) > 4.0) || (mod(y, 4.0) > 2.0);

	
	if (grid) {
		float c = 0.0;
		gl_FragColor = vec4(c, c, c, 0.6-n*0.5);
		
	} else if (highlight) {
		float c = 1.0;
		gl_FragColor = vec4(c, c, c, vignette*0.05);
		
	} else if (shadows) {
		float c = 0.0;
		gl_FragColor = vec4(c, c, c, 0.2);
		
	} else {
		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
	}
	
}

