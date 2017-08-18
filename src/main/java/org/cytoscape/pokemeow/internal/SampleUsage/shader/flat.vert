#version 410

layout(location = 0) in vec3 position;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform vec4 vcolor;
uniform vec4 vcolorGrad;
uniform int gradColorBorder;

out vec4 o_color;
void main() {
    gl_Position = modelMatrix*viewMatrix*vec4(position, 1.0f);
    switch(gradColorBorder){
        case -1:
            o_color = vcolor;
            break;
        case 0:
            if(position.y < 0.0)
                o_color = vcolor;
            else
                o_color = vcolorGrad;
            break;
        case 1:
            if(position.x < .0)
                o_color = vcolor;
            else
                o_color = vcolorGrad;
            break;
        case 2:
            if(position.x < position.y)
                o_color = vcolor;
            else
                o_color = vcolorGrad;
            break;
        case 3:
           if(position.x + position.y > 0)
                o_color = vcolor;
           else
               o_color = vcolorGrad;
           break;
        default:
           o_color = vcolor;
           break;
    }
}