#pragma once

#include <cstdint>

namespace glfr {
    struct glyph_info final {
        uint64_t value;
        float x_advance, y_advance;
        float width, height;
        float offset_x, offset_y;
        float tex_coord_x, tex_coord_y;

        glyph_info() = default;

        glyph_info(uint64_t value, float x_advance, float y_advance, float width, float height,
                   float offset_x, float offset_y, float tex_coord_x, float tex_coord_y) :
                value(value), x_advance(x_advance), y_advance(y_advance), width(width), height(height),
                offset_x(offset_x), offset_y(offset_y), tex_coord_x(tex_coord_x), tex_coord_y(tex_coord_y) {}
    };
}