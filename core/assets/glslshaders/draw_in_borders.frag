#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {

    vec4 borders = v_color; // mis-use color attribute
    borders *= 1024;

    if (
        gl_FragCoord.x      >=  borders.r
        && gl_FragCoord.x   <=  borders.g
        && gl_FragCoord.y   >=  borders.b
        && gl_FragCoord.y   <=  borders.a
    ) {

        gl_FragColor = texture2D(u_texture, v_texCoords);
    }
}