#version 150 core

#define MIN_TEMP 1000
#define MAX_TEMP 40000

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform float maxSize = 1024;
uniform float screenHeight = 1;
uniform float exposure = 1;
uniform float contrast = 1;
uniform float saturation = 1;

uniform sampler2D texSpectrum;

in vec3 in_Position;
in float in_Magnitude;
in float in_Temperature;

out float pass_Size;
out float pass_Brightness;
out vec4 pass_Color;

void main(void) {
	gl_Position = projectionMatrix * viewMatrix * vec4(in_Position, 1);
	
	pass_Brightness = 100 * exposure * pow(10, -0.4 * contrast * in_Magnitude);
	
	float s = sqrt(pass_Brightness);
	float full = s;
	if(pass_Brightness>1) {
		pass_Brightness = pow(pass_Brightness, saturation);
		full = 16*sqrt(pass_Brightness);
	}
	pass_Size = min(max(full, 1), maxSize);
	gl_PointSize = pass_Size;
	
	float t = (in_Temperature - MIN_TEMP) / (MAX_TEMP - MIN_TEMP);
	vec4 color = texture(texSpectrum, vec2(t, 0));
	pass_Color = mix(vec4(1, 1, 1, 1), color, clamp(pass_Brightness*0.5, 0, 1));
}
