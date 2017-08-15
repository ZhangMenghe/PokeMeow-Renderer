#version 420
layout(early_fragment_tests) in;
precision mediump float;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

in vec4 o_color;
out vec4 frag_color;

void main() {
     frag_color = o_color;
}
