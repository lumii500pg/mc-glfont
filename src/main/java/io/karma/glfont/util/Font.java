package io.karma.glfont.util;

import io.karma.glfont.render.gl.GlTexture;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public final class Font {
    public final GlTexture texture;
    public final float height;
    private final Char2ObjectOpenHashMap<GlyphInfo> glyphInfos = new Char2ObjectOpenHashMap<>(); // I dunno which capacity we need ._. ~lucy

    public Font(final @NotNull InputStream inputStream, final int height) throws IOException {
        this.height = (float) height;

        final byte[] bytes = new byte[inputStream.available()];
        if (inputStream.read(bytes) != bytes.length) {
            throw new IOException("Failed to read bytes");
        }

        System.out.println("FONT SIZE:" + bytes.length);

        texture = generateFontAtlas(bytes, height);

        // TODO: map data processing goes here x3

        for (final GlyphInfo glyphInfo : glyphInfos.values()) {
            System.out.println(glyphInfo.toString());
        }
    }

    public Char2ObjectOpenHashMap<GlyphInfo> glyphInfos() {
        return glyphInfos;
    }

    private native GlTexture generateFontAtlas(final byte @NotNull [] fontBytes, final int height);
}
