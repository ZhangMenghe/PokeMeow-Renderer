#version 410

precision mediump float;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

in vec4 v_color;
out vec4 frag_color;

void main() {
    frag_color = v_color;
}
