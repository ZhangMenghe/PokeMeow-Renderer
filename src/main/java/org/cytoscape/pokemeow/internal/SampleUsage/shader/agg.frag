#version 410

precision mediump float;

uniform sampler2D dash_atlas;
uniform float dash_index, dash_phase, dash_period;
uniform float linelength, linewidth, antialias;
uniform vec2 caps;
uniform vec4 vcolor;

in vec2 TexCoords;
out vec4 frag_color;

float cap( float type, float u, float v, float t )
{
    // None
    if ( type < 0.5 ) discard;
    // Round
    else if ( abs(type - 1.0) < 0.5 ) return sqrt(u*u+v*v);
    // Triangle out
    else if ( abs(type - 2.0) < 0.5 ) return max(abs(v),(t+u-abs(v)));
    // Triangle in
    else if ( abs(type - 3.0) < 0.5 ) return (u+abs(v));
    // Square
    else if ( abs(type - 4.0) < 0.5 ) return max(u,v);
    // Butt
    else if ( abs(type - 5.0) < 0.5 ) return max(u+t,v);
    discard;
}

void main() {
    float w = linewidth;
    float freq = w*dash_period;
    float u = TexCoords.x;
    float v = TexCoords.y;
    float u_ = mod( u + w*dash_phase, freq );
    vec4 dash = texture2D(dash_atlas, vec2(u_/freq, dash_index));
    float dash_ref = dash.x;
    float dash_type = dash.y;
    float dash_start = (u - u_) + w * dash.z;
    float dash_stop = (u - u_) + w * dash.w;
    float line_start = 0.0;
    float line_stop = linelength;
    bool cross_start = (dash_start <= line_start) && (dash_stop >= line_start);
    bool cross_stop = (dash_stop >= line_stop) && (dash_start <= line_stop);
    float t = linewidth/2.0 - antialias;
    // Default distance to the line body
    float d = abs(v);
    // Dash stop is before line start
    if( dash_stop <= line_start )
        discard;
    // Dash start is beyond line stop
    else if( dash_start >= line_stop )
        discard;
    // Dash is across line start and fragment before line start (1)
    else if( (u <= line_start) && (cross_start) )
        d = cap( caps.x, u, v, t);
    // Dash is across line stop and fragment after line stop (4)
    else if( (u >= line_stop) && (cross_stop) )
        d = cap( caps.y, u - line_stop, v, t );
    // Dash cap start (5)
    else if( dash_type < 0.0 )
        d = cap( caps.y, u-dash_ref, v, t );
    // Dash cap stop (6)
    else if( dash_type > 0.0 )
        d = cap( caps.x, dash_ref-u, v, t );
    // Antialias test
    d -= t;
    if( d < 0.0 )
        frag_color = vcolor;
    else {
        d /= antialias;
        frag_color = vec4(1.0, .0, .0,1.0);//vec4(vcolor.rgb, exp(-d*d)*vcolor.a);
    }
}