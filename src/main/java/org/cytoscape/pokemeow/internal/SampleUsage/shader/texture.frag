#version 330
in vec2 TexCoord;
uniform sampler2D texSampler;
void main() {
	gl_FragColor = texture(texSampler, TexCoord);
}
