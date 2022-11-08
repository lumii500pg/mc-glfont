package io.karma.glfont.render;

import io.karma.glfont.render.gl.GlBuffer;
import io.karma.glfont.render.gl.GlShader;
import io.karma.glfont.util.Font;
import io.karma.glfont.util.GlyphInfo;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class FontMeshBuilder {
    private final Font font;

    private final FloatArrayList vertices = new FloatArrayList();
    private final IntArrayList indices = new IntArrayList();

    public FontMeshBuilder(final @NotNull Font font) {
        this.font = font;
    }

    public void begin() {
        vertices.clear();
        indices.clear();
    }

    public void addVertex(final float x, final float y, final float u, final float v, final int color) {
        vertices.add(x);
        vertices.add(y);
        vertices.add(u);
        vertices.add(v);
        vertices.add(Float.intBitsToFloat(color));
    }

    public void addQuad(
            final Vertex vertex0,//final float x0, final float y0, final float u0, final float v0, final int color0,
            final Vertex vertex1,//final float x1, final float y1, final float u1, final float v1, final int color1,
            final Vertex vertex2,//final float x2, final float y2, final float u2, final float v2, final int color2,
            final Vertex vertex3//final float x3, final float y3, final float u3, final float v3, final int color3
    ) {
        final int index = vertices.size() / 5;

        addVertex(vertex0.x, vertex0.y, vertex0.u, vertex0.v, vertex0.color);
        addVertex(vertex1.x, vertex1.y, vertex1.u, vertex1.v, vertex1.color);
        addVertex(vertex2.x, vertex2.y, vertex2.u, vertex2.v, vertex2.color);
        addVertex(vertex3.x, vertex3.y, vertex3.u, vertex3.v, vertex3.color);

        indices.add(index);
        indices.add(index + 1);
        indices.add(index + 3);
        indices.add(index + 3);
        indices.add(index + 1);
        indices.add(index + 2);
    }

    public void push(final String message, final float x, final float y, final int color) {
        final float font_height = font.height;
        final float font_tex_w = font.texture.getWidth();
        final float font_tex_h = font.texture.getHeight();

        float offset_x = 0;
        float offset_y = 0;

        boolean first_glyph_of_line = true;

        final char[] chars = message.toCharArray();
        for (final char current : chars) {
            if (current == '\r') continue;
            if (current == '\n') {
                offset_y += font_height; //TODO: CIV
                offset_x = 0.0f;
                first_glyph_of_line = true;
                continue;
            }

            final GlyphInfo glyph = font.glyphInfos().get(current);
            if (glyph == null) continue;

            float x_char_offset = glyph.offsetX;
            if (first_glyph_of_line) {
                if (glyph.offsetX < 0.0f) {
                    x_char_offset = 0.0f;
                }
                first_glyph_of_line = false;
            }

            final Vertex vertex_tl = new Vertex(
                    x + offset_x + x_char_offset,
                    y + offset_y + font_height - glyph.offsetY,
                    glyph.texCoordX / font_tex_w,
                    glyph.texCoordY / font_tex_h,
                    color
            );

            final Vertex vertex_bl = new Vertex(
                    vertex_tl.x,
                    vertex_tl.y + glyph.height,
                    glyph.texCoordX / font_tex_w,
                    (glyph.texCoordY + glyph.height) / font_tex_h,
                    color
            );

            final Vertex vertex_br = new Vertex(
                    vertex_tl.x + glyph.width,
                    vertex_tl.y + glyph.height,
                    (glyph.texCoordX + glyph.width) / font_tex_w,
                    (glyph.texCoordY + glyph.height) / font_tex_h,
                    color
            );

            final Vertex vertex_tr = new Vertex(
                    vertex_tl.x + glyph.width,
                    vertex_tl.y,
                    (glyph.texCoordX + glyph.width) / font_tex_w,
                    glyph.texCoordY / font_tex_h,
                    color
            );

            addQuad(vertex_tl, vertex_br, vertex_tr, vertex_bl);

            offset_x += glyph.xAdvance;
            offset_y += glyph.yAdvance;
        }
    }


    public void end(final @NotNull GlShader shader) {
        final FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.size());
        vertexBuffer.put(vertices.elements(), 0, vertices.size());
        vertexBuffer.flip();

        final IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.size());
        indexBuffer.put(indices.elements(), 0, indices.size());
        indexBuffer.flip();

        final Minecraft mc = Minecraft.getMinecraft();

        try (final GlBuffer buffer = new GlBuffer(vertexBuffer, indexBuffer, indices.size())) {
            shader.bind();
            shader.uniform2f("resolution", mc.displayWidth, mc.displayHeight);

            font.texture.bind(0);

            buffer.draw();

            font.texture.unbind(0);

            shader.unbind();
        }
    }

    private static class Vertex {
        public float x, y;
        public float u, v;
        public int color;

        // @formatter:off
        Vertex() {}
        // @formatter:on

        public Vertex(final float x, final float y, final float u, final float v, final int color) {
            this.x = x;
            this.y = y;
            this.u = u;
            this.v = v;
            this.color = color;
        }
    }
}
