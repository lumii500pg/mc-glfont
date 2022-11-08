package io.karma.glfont.render.gl;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GlBuffer extends GlObject {
    public static final int VERTEX_SIZE = 20;

    private final int vertexBuffer;
    private final int indexBuffer;
    private final int indexCount;

    public GlBuffer(final @NotNull FloatBuffer vertexData, final @NotNull IntBuffer indexData, int indexCount) {
        super(GL30.glGenVertexArrays());

        this.indexCount = indexCount;

        GL30.glBindVertexArray(id);

        indexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);

        vertexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, VERTEX_SIZE, 0L);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, VERTEX_SIZE, 2 * Float.SIZE);
        GL20.glVertexAttribPointer(2, 1, GL11.GL_UNSIGNED_INT, false, VERTEX_SIZE, 4 * Float.SIZE);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);
    }

    @Override
    public void close() {
        GL30.glDeleteVertexArrays(id);
        GL15.glDeleteBuffers(indexBuffer);
        GL15.glDeleteBuffers(vertexBuffer);
    }

    public void draw() {
        GL30.glBindVertexArray(id);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);

        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
    }
}
