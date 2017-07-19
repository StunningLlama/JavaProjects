#version 150 core
 
in vec4 pass_Color;
out vec4 out_Color;
uniform sampler2D prev_frame;
uniform vec2 resolution;
uniform float time;
uniform float pstr;
uniform float decay;

void main(void) {
//	out_Color = pass_Color;
//	out_Color = vec4(0, 0, 1, 0);
//	out_Color = texture2D(prev_frame, gl_FragCoord.xy/resolution.xy) + vec4(0.01, 0.01, 0.01, 0.01);
//	out_Color = pass_Color + 0.8*texture2D(prev_frame, gl_FragCoord.xy/resolution.xy);
	out_Color = pstr*pass_Color + decay*texture2D(prev_frame, gl_FragCoord.xy/resolution.xy) + vec4(-0.5f/256.0, -0.5f/256.0, -0.5f/256.0, 0);
//	out_Color = max(0.75 * texture2D(prev_frame, gl_FragCoord.xy/resolution.xy), pass_Color);
//	out_Color = vec4(time, gl_FragCoord.x / resolution.x, gl_FragCoord.y / resolution.y, 1);
//	out_Color = texture2D(prev_frame, 2.0 * gl_FragCoord.xy/resolution.xy);
}