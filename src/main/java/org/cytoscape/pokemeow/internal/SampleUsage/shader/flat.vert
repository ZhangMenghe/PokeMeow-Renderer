#version 410

layout(location = 0) in vec4 position;
uniform vec4 color;

out vec4 v_color;

void main() {
    gl_Position = position;
    v_color = color;
}