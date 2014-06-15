uniform sampler2D u_texture;

varying vec4 vColor;
varying vec2 vTexCoord;

uniform float time;
uniform vec2 resolution;

uniform vec4 rect;

float trim(float n) {
	return float(int(n));
}

// Y-axis is pointing down.

void main() {

	gl_FragColor = vec4(0.0,0.0,0.0, 0.0);

	float x = float(int(vTexCoord.x*resolution.x));
	float y = float(int(vTexCoord.y*resolution.y));
	
	float r_left = trim(rect.x);
	float r_bottom = resolution.y - trim(rect.y);
	float r_right = trim(rect.z);
	float r_top = resolution.y - trim(rect.w);
	float r_width = r_right - r_left;
	float r_height = r_bottom - r_top;
	
	if (y == r_top) {
		gl_FragColor = vec4(1.0,0.0,0.0, 1.0);
	}
	
	if (y == r_bottom) {
		gl_FragColor = vec4(1.0,1.0,0.0, 1.0);
	} 
	
	if (x == r_left) {
		gl_FragColor = vec4(0.0,1.0,0.0, 1.0);
	}
	
	if (x == r_right) {
		gl_FragColor = vec4(1.0,1.0,1.0, 1.0);
	}
	
	if (x >= r_left && x < r_right && y > r_top && y < r_bottom) {
		// Fill
		gl_FragColor = vec4(1.0,1.0,1.0, 1.0);
	}
	
}