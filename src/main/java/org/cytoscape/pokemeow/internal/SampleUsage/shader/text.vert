#version 330
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoord;

out vec2  TexCoords;
uniform mat4 modelMatrix;
void main() {
    gl_Position = modelMatrix*vec4(position, 1.0f);
    TexCoords = texcoord;
}