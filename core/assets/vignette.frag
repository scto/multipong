#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif
//our screen resolution, set from Java whenever the display is resized
uniform vec2 resolution;

//"in" attributes from our vertex shader
varying LOWP vec4 vColor;
varying vec2 vTexCoord;

//RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float RADIUS = 0.9;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.9;


void main() {

	// Vignette //
	
	vec2 position = vec2(vTexCoord.y , vTexCoord.x) - vec2(0.5, 0.5);
	vec2 position1 = vec2(position.x, position.y*0.8);
	float len = length(position1);
	
	float m = mod(len, 0.01);
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);
	
 	if (m < 0.005) {
		float v = (0.8-vignette)*m*100;
		gl_FragColor = vec4(0.0, 0.0, 0.0, v);
	} else {
		gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);
	}
}