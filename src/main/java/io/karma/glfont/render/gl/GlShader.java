package io.karma.glfont.render.gl;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GlShader extends GlObject {
    private static final String VERTEX_SOURCE = "#version 330\n" +
            "\n" +
            "    #ifdef GL_ES\n" +
            "    precision highp float;\n" +
            "    #endif\n" +
            "\n" +
            "    in vec2 position;\n" +
            "    in vec2 texCoord;\n" +
            "    in uint color;\n" +
            "\n" +
            "    out vec2 texCoordFS;\n" +
            "    out vec4 colorFS;\n" +
            "\n" +
            "    uniform vec2 resolution;\n" +
            "\n" +
            "    void main() {\n" +
            "        vec2 v = (position / resolution) * 2.0 - 1.0;\n" +
            "        gl_Position = vec4(v.x, -v.y, 0.0, 1.);\n" +
            "        texCoordFS = vec2(texCoord.x, 1.0 - texCoord.y);\n" +
            "        colorFS = vec4(1.0);\n" +
            "        //colorFS = (vec4(float(((color >> 16) & 0xff)), float((color >> 8) & 0xff), float((color & 0xff)), float(((color >> 24) & 0xff))) / vec4(255.));\n" +
            "    }\n";

    private static final String FRAGMENT_SOURCE = "#version 330\n" +
            "\n" +
            "    #ifdef GL_ES\n" +
            "    precision highp float;\n" +
            "    #endif\n" +
            "\n" +
            "    in vec2 texCoordFS;\n" +
            "    in vec4 colorFS;\n" +
            "\n" +
            "    uniform sampler2D fontTexture;\n" +
            "\n" +
            "    out vec4 outColor;\n" +
            "\n" +
            "    void main(void) {\n" +
            "        outColor = colorFS * vec4(1.0, 1.0, 1.0, texture(fontTexture, texCoordFS).r);\n" +
            "    }\n";

    public GlShader(final @NotNull String vertexSource, final @NotNull String fragmentSource) {
        super(GL20.glCreateProgram());

        final int vertexShader = createShader(GL20.GL_VERTEX_SHADER, vertexSource);
        GL20.glAttachShader(id, vertexShader);

        final int fragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);
        GL20.glAttachShader(id, fragmentShader);

        GL20.glLinkProgram(id);

        if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
            throw new GlException(String.format("glLinkProgram failed: %s", GL20.glGetProgramInfoLog(id, 2048)));
        }

        GL20.glDeleteShader(fragmentShader);
        GL20.glDeleteShader(vertexShader);
    }

    public GlShader() {
        this(VERTEX_SOURCE, FRAGMENT_SOURCE);
    }

    private static int createShader(int shaderType, final @NotNull String source) {
        final int shader = GL20.glCreateShader(shaderType);
        if (shader == 0) {
            throw new GlException("glCreateShader failed");
        }

        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
            throw new GlException(String.format("glCompileShader failed: %s", GL20.glGetShaderInfoLog(shader, 2048)));
        }

        return shader;
    }

    @Override
    public void close() throws Exception {
        GL20.glDeleteProgram(id);
    }

    public void uniform2f(@NotNull final String location, float x, float y) {
        GL20.glUniform2f(GL20.glGetUniformLocation(id, location), x, y);
    }

    public void bind() {
        GL20.glUseProgram(id);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }
}
