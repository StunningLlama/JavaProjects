#version 330 core
 
layout(location = 0) in vec4 in_Position;
layout(location = 1) in vec4 in_Color;
layout(location = 2) in vec3 in_Normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 light_direction;

out vec4 pass_Color;

void main(void) {
	gl_Position = in_Position;
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
	pass_Color = in_Color;// * (abs(dot(in_Normal, light_direction)) * 0.8 + 0.2);
//	pass_Color.a = in_Color.a;
}