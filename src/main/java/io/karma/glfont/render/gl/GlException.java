package io.karma.glfont.render.gl;

import org.jetbrains.annotations.NotNull;

public class GlException extends RuntimeException {
    public GlException(final @NotNull String message) {
        super(message);
    }

    public GlException(final @NotNull String message, final @NotNull Throwable cause) {
        super(message, cause);
    }

    public GlException(final @NotNull Throwable cause) {
        super(cause);
    }

    protected GlException(final @NotNull String message, final @NotNull Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
