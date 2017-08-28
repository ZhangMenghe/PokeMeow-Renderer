#version 410

layout(location = 0) in vec3 position;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
void main() {
    gl_Position = viewMatrix* modelMatrix * vec4(position, 1.0f);
}