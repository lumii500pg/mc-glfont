package io.karma.glfont.render.gl;

import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

public class GlTexture extends GlObject {
    private final int width;
    private final int height;

    public GlTexture(final @NotNull ByteBuffer buffer, final int width, final int height) {
        super(GlStateManager.generateTexture());

        this.width = width;
        this.height = height;

        GlStateManager.bindTexture(id);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R8, width, height, 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, buffer);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void close() {
        GlStateManager.deleteTexture(id);
    }

    public void bind(final int location) {
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + location);
        GlStateManager.bindTexture(id);
    }

    public void unbind(final int location) {
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0 + location);
        GlStateManager.bindTexture(0);
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }
}
