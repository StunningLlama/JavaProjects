#version 150 core

out vec4 out_Color;
uniform sampler2D prev_frame;
uniform vec2 resolution;

void main(void) {
	out_Color = texture2D(prev_frame, gl_FragCoord.xy/resolution.xy);
//	out_Color = pass_Color;
//	out_Color = vec4(1, 1, 0, 0);
//	out_Color = texture2D(prev_frame, gl_FragCoord.xy/resolution.xy) + vec4(0.01, 0.01, 0.01, 0.01);
//	out_Color = pass_Color + 0.8*texture2D(prev_frame, gl_FragCoord.xy/resolution.xy);
//	out_Color = 0.5*(pass_Color + texture2D(prev_frame, gl_FragCoord.xy));
//	out_Color = vec4(gl_FragCoord.x / resolution.x, gl_FragCoord.y / resolution.y, 1, 1);
//	out_Color = texture2D(prev_frame, 2.0 * gl_FragCoord.xy/resolution.xy);
}