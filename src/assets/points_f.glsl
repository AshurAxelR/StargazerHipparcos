#version 150 core

uniform float saturation = 1;

out vec4 out_Color;

in float pass_Size;
in float pass_Brightness;
in vec4 pass_Color;

void main(void) {
	float c;
	if(pass_Brightness<1) {
		c = pass_Brightness * 2;
	}
	else {
		float d = distance(gl_PointCoord, vec2(0.5, 0.5)) * pass_Size * 2;
		if(d>pass_Size) discard;
		c = d<0.1*sqrt(pass_Brightness) ? 100 : pass_Brightness / d / d;
	}
	if(c<1.0/256.0) discard;
	out_Color = vec4(pass_Color.xyz * c, 1);
}
