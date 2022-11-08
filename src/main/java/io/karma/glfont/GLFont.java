package io.karma.glfont;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = "glf",
        name = "GLFont",
        version = "1.0.0-release.1",
        acceptedMinecraftVersions = "[1.8.9]",
        clientSideOnly = true
)
public final class GLFont {
    private static final GLFont INSTANCE = new GLFont();

    static {
        System.load("X:\\Project Files\\Java\\glfont\\glfont_native\\cmake-build-debug\\glfont_native.dll");
    }

    public static GLFont getInstance() {
        return INSTANCE;
    }
}
