#version 410

precision mediump float;

out vec4 frag_color;
uniform vec4 vcolor;

void main() {
    frag_color = vcolor;
}
