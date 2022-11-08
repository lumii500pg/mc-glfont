package io.karma.glfont.injection.mixins.client;

import io.karma.glfont.render.TrueTypeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Minecraft.class, priority = 1337) // when it gets later injected it's safer
public abstract class MixinMinecraft {
    @Redirect(
            method = "startGame",
            at = @At(
                    value = "NEW",
                    target = "net.minecraft.client.gui.FontRenderer",
                    ordinal = 0
            )
    )
    public final FontRenderer replaceFontRenderer(final GameSettings settings, final ResourceLocation resourceLocation,
                                                  final TextureManager textureManager, final boolean unicodeFlag) {
        return new TrueTypeFontRenderer();
    }
}
