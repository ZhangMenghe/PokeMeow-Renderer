#version 330
in vec2 TexCoord;
in vec4 vColor;
uniform sampler2D texSampler;
void main() {
    if(vColor.w == -1)
	    gl_FragColor = texture(texSampler, vColor.xy);
	else
	    gl_FragColor = vColor;
}
