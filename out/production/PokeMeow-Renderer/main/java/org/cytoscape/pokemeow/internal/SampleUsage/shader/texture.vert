#version 330
layout(location = 0) in vec2 position;
layout(location = 1) in vec4 color;

//out vec2 TexCoord;
out vec4 vColor;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

void main() {
	 gl_Position = modelMatrix*viewMatrix*vec4(position, .0f, 1.0f);
	 vColor = color;//vec4(position,.0f,-1.0f);
 }
