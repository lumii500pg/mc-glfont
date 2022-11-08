package io.karma.glfont.render.gl;

public abstract class GlObject implements AutoCloseable {
    protected final int id;

    protected GlObject(final int id) {
        this.id = id;
    }

    public final int getId() {
        return id;
    }
}
