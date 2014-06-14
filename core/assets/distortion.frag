uniform sampler2D u_texture;

varying vec4 vColor;
varying vec2 vTexCoord;
uniform float time;
uniform vec2 resolution;

float rand(vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float smooth(float n) {
	return n/2.0 + (n-1.0)/4.0 + (n+1.0)/4.0;
}

float decimals(float n) {
	return n - float(int(n));
}

void main() {

	// The noisy big pixels //
	
	float x = float(int(vTexCoord.x*resolution.x));
	float y = float(int(vTexCoord.y*resolution.y));
	float r = rand(vec2(x,y));

	float t = decimals(time);
	float rt = rand(vec2(t,0));
	float v = decimals(rand(vec2(rt*x, rt*y)));
		  
	// Rolling effect 2 sec for every 10
	float af = mod(time, 10.0);
	bool a = af <= 2.0;

	// some other effect
	float bf = mod(time - float(int(time*0.1)), 15);
	bool b = bf < 1.0 && (rt < 0.5);
	
	if (a) {
	
		float ascale = 1.0-(2.0 - af)/2.0;
		float height = (resolution.y - resolution.y * 0.2);
		float botY = resolution.y * ascale * 1.5;
		float topY = -(height - botY);
		bool no_paint = (y < topY || y > botY);
		
		if (no_paint) {
		
			gl_FragColor = vec4(0.0,0.0,0.0,0.0);
			
		} else {
			
			float p = y / resolution.y - y * 0.1;
			
			if (cos(topY - y) < 0.5) {
				float s = p;
				
				gl_FragColor = vec4(s,s,s, 0.25 - v * 0.5);
				
			} else {
				float s = sin(p);
				
				gl_FragColor = vec4(s,s,s, 0.2 - v * 0.8);
			}
		}
	
	} else if (b) {
		
		float n = smooth(smooth(v));
		float c = 0.25;
		
		gl_FragColor = vec4(c, c, c, 0.4-n*0.5);
	
	} else {
	
		gl_FragColor = vec4(0.0,0.0,0.0, 0.0);
	}


	
}

