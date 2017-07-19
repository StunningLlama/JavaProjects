#version 150 core
 
in vec2 in_Position;
in vec4 in_Color;

out vec4 pass_Color;

void main(void) {
    gl_Position = vec4(in_Position.x, in_Position.y, 0., 1.);
    pass_Color = in_Color;
}