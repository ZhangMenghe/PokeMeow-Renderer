#version 330
in vec2 TexCoord;
in vec4 vColor;
uniform sampler2D texSampler;

void main() {
    gl_FragColor = texture(texSampler, TexCoord);
}
