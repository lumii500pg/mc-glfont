#pragma once

#include <cstdint>

namespace glfr {
    class bitmap final {
    private:
        uint8_t* _pixels;
        uint32_t _width;
        uint32_t _height;

    public:
        bitmap(uint32_t width, uint32_t height)
                : _width(width), _height(height) {
            _pixels = new uint8_t[width * height];
        }

        ~bitmap() {
            delete[] _pixels;
        }

        [[nodiscard]] inline uint8_t& get(uint32_t x, uint32_t y) noexcept {
            return _pixels[(_height - y - 1) * _width + x];
        }

        [[nodiscard]] inline uint32_t get_width() const noexcept {
            return _width;
        }

        [[nodiscard]] inline uint32_t get_height() const noexcept {
            return _height;
        }

        [[nodiscard]] inline void* get_data() noexcept {
            return _pixels;
        }

        [[nodiscard]] inline const void* get_data() const noexcept {
            return _pixels;
        }
    };
}