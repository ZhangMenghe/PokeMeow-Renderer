#version 410

precision mediump float;

in vec4 v_color;
out vec4 frag_color;

void main() {
    frag_color = v_color;
   //frag_color = vec4(.0f,1.0f,.0f,1.0f);
}
