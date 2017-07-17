#version 410

precision mediump float;

uniform vec3 textColor;
uniform sampler2D texSampler;

in vec2 TexCoords;
out vec4 frag_color;

void main() {
    float sampled = texture(texSampler, TexCoords).r;
    frag_color = vec4(textColor, sampled);//*sampled;
}
