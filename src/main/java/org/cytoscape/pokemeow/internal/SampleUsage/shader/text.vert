#version 410

layout(location = 0) in vec4 vertex; // <vec2 pos, vec2 tex>

out vec2  TexCoords;
uniform mat4 modelMatrix;
void main() {
    gl_Position = modelMatrix*vec4(vertex.xy,.0f, 1.0f);
    TexCoords = vertex.zw;
}