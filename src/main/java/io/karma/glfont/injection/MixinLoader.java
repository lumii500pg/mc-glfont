package io.karma.glfont.injection;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

/**
 * @author superblaubeere27
 * might breaking copyright and licensing lmao idgaf
 */
public final class MixinLoader implements IFMLLoadingPlugin {
    public MixinLoader() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.glfont.json");
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
    }

    @NotNull
    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Nullable
    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    // @formatter:off
    @Override
    public void injectData(final Map<String, Object> data) {}
    // @formatter:on

    @Nullable
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}