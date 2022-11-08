package io.karma.glfont.render;

import io.karma.glfont.render.gl.GlShader;
import io.karma.glfont.util.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class TrueTypeFontRenderer extends FontRenderer {
    public static Font font = null;

    public TrueTypeFontRenderer() {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().renderEngine, false); // needed to prevent nullpointers ._.

        Font font = null;
        try {
            font = new Font(Class.class.getResourceAsStream("/assets/VeraMono.ttf"), 22);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        TrueTypeFontRenderer.font = font;
    }

    private static final GlShader TEXT_SHADER = new GlShader();

    @Override
    public int drawString(final String input, final float x, final float y, final int color, final boolean shadow) {
        if (font.texture != null) {
            final FontMeshBuilder fontMeshBuilder = new FontMeshBuilder(font);
            fontMeshBuilder.begin();
            fontMeshBuilder.push(input, x, y, color);
            fontMeshBuilder.end(TEXT_SHADER);
        }
        return 0;
    }

    @Override
    public int getStringWidth(final String input) {
        // needed
        return 0;
    }

    @Override
    public int getCharWidth(final char character) {
        // needed
        return 0;
    }

    @Override
    public String trimStringToWidth(final String input, final int width, final boolean reverse) {
        // needed
        return "";
    }

    @Override
    public void drawSplitString(final String input, final int x, final int y, final int wrapWidth, final int color) {
        // needed
    }

    @Override
    public int splitStringWidth(final String input, final int desiredWidth) {
        // needed
        return 0; // orig: return this.FONT_HEIGHT * this.listFormattedStringToWidth(str, maxLength).size();
    }

    @Override
    protected void setColor(final float red, final float green, final float blue, final float alpha) {
        // needed
    }

    @Override
    public void onResourceManagerReload(final IResourceManager iResourceManager) {
        // not needed?
    }

    @Override
    protected float renderDefaultChar(final int character, final boolean italic) {
        // not needed
        return .0F;
    }

    @Override
    protected float renderUnicodeChar(final char character, final boolean italic) {
        // not needed
        return .0F;
    }

    @Override
    public int drawStringWithShadow(final String input, final float x, final float y, final int color) { // not needed
        return this.drawString(input, x, y, color, true); // <- this is much more important
    }

    @Override
    public int drawString(final String input, final int x, final int y, final int color) { // not needed
        return this.drawString(input, x, y, color, false); // this is much more important
    }

    @Override
    public List<String> listFormattedStringToWidth(final String input, final int desiredWidth) { // maybe done..?
        return super.listFormattedStringToWidth(input, desiredWidth);
    }

    @Override
    protected void doDraw(final float p_doDraw_1_) {
        // not needed
    }

    @Override
    public String trimStringToWidth(final String input, final int width) {
        return this.trimStringToWidth(input, width, false);
    }

    @Override
    public boolean getUnicodeFlag() {
        return super.getUnicodeFlag(); // irrelevant
    }

    @Override
    public void setUnicodeFlag(final boolean flag) {
        super.setUnicodeFlag(flag); // irrelevant
    }

    @Override
    public boolean getBidiFlag() { // useless
        return super.getBidiFlag();
    }

    @Override
    public void setBidiFlag(final boolean flag) { // useless
        super.setBidiFlag(flag);
    }

    @Override
    public int getColorCode(final char color) { // done
        return super.getColorCode(color);
    }

    @Override
    protected void enableAlpha() {
        super.enableAlpha(); // done (it's literally calling GlStateManager.enableAlpha(); mojang wtf are you doing ._.)
    }

    @Override
    protected void bindTexture(final ResourceLocation resourceLocation) { // done (it's literally calling this.renderEngine.bindTexture(p_bindTexture_1_); mojang wtf are you doing ._.)
        super.bindTexture(resourceLocation);
    }

    @Override
    protected InputStream getResourceInputStream(final ResourceLocation resourceLocation) throws IOException { // done (it's literally calling Minecraft.getMinecraft().getResourceManager().getResource(p_getResourceInputStream_1_).getInputStream(); mojang wtf are you doing ._.)
        return super.getResourceInputStream(resourceLocation);
    }
}
