#version 330 core
 
in vec4 pass_Color;
out vec4 out_Color;

void main(void) {
	out_Color = pass_Color;
//	out_Color = vec4(1., 1., 0., 0.);
}