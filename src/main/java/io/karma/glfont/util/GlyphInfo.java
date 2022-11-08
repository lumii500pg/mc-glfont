package io.karma.glfont.util;

public class GlyphInfo {
    public final char value;
    public final float xAdvance, yAdvance;
    public final float width, height;
    public final float offsetX, offsetY;
    public final float texCoordX, texCoordY;

    public GlyphInfo(char value, float xAdvance, float yAdvance, float width, float height, float offsetX, float offsetY, float texCoordX, float texCoordY) {
        this.value = value;
        this.xAdvance = xAdvance;
        this.yAdvance = yAdvance;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.texCoordX = texCoordX;
        this.texCoordY = texCoordY;
    }
}
