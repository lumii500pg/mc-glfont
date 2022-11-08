#include <ft2build.h>
#include FT_FREETYPE_H

#include "util/bitmap.hpp"
#include "util/glyph_info.hpp"
#include <jni.h>

#include <cstdint>
#include <unordered_map>
#include <vector>

#include <iostream>

#ifdef __cplusplus
extern "C" {
#endif

const uint32_t PADDING_X = 2;
const uint32_t PADDING_Y = 2;

void calc_glyph_size(FT_Face face, const FT_ULong& character, uint32_t preferred_width,
                     uint32_t& current_atlas_width, uint32_t& max_atlas_width,
                     uint32_t& max_glyph_height, uint32_t& current_y_offset, uint32_t& current_y_height,
                     std::vector<std::pair<uint32_t, uint32_t>>& y_data) {
    FT_Error error = FT_Load_Char(face, character, FT_LOAD_DEFAULT);
    if (error) {
        //TODO: handle error
        return;
    }

    error = FT_Render_Glyph(face->glyph, FT_RENDER_MODE_NORMAL);
    if (error) {
        //TODO: handle error
        return;
    }

    current_atlas_width += face->glyph->bitmap.width + PADDING_X;
    uint32_t current_height = face->glyph->bitmap.rows;

    if (current_height > current_y_height) {
        current_y_height = current_height;
    }

    if (current_atlas_width > preferred_width) {
        if (current_atlas_width > max_atlas_width) {
            max_atlas_width = current_atlas_width;
        }
        current_atlas_width = 0;
        current_y_height += PADDING_Y;

        y_data.emplace_back(current_y_height, current_y_offset);
        current_y_offset += current_y_height;
        current_y_height = 0;
    }

    if (current_height > max_glyph_height) {
        max_glyph_height = current_height;
    }
}


JNIEXPORT jobject JNICALL Java_io_karma_glfont_util_Font_generateFontAtlas(JNIEnv* env, jobject object, jbyteArray font_bytes, jint font_height) {
    const auto font_bytes_ptr = env->GetByteArrayElements(font_bytes, nullptr);
    const auto font_bytes_size = env->GetArrayLength(font_bytes);

    static FT_Library library = nullptr;
    FT_Face face;
    FT_Error error;

    if (!library) {
        error = FT_Init_FreeType(&library);
        if (error) {
            std::cout << "FT_Init_FreeType" << std::endl;
            return nullptr;
        }
    }

    error = FT_New_Memory_Face(library, (const FT_Byte*) font_bytes_ptr, (FT_Long) font_bytes_size, 0, &face);
    if (error) {
        std::cout << "FT_New_Memory_Face" << std::endl;
        return nullptr;
    }

    error = FT_Select_Charmap(face, FT_ENCODING_UNICODE);
    if (error) {
        std::cout << "FT_Select_Charmap" << std::endl;
        return nullptr;
    }

    error = FT_Set_Pixel_Sizes(face, 0, font_height);
    if (error) {
        std::cout << "FT_Set_Pixel_Sizes" << std::endl;
        return nullptr;
    }

    const size_t preferred_width = font_height * 32;
    uint32_t current_atlas_width = 0;
    uint32_t max_atlas_width = 0;
    uint32_t temp_y_offset = 0;
    uint32_t temp_y_height = 0;
    uint32_t glyph_height = 0;
    std::vector<FT_ULong> char_codes;
    std::vector<std::pair<uint32_t, uint32_t>> y_data;

    FT_UInt glyph_index;
    FT_ULong char_code = FT_Get_First_Char(face, &glyph_index);
    if (glyph_index == 0) {
        std::cout << "FT_Get_First_Char" << std::endl;
        return nullptr;
    }

    calc_glyph_size(face, char_code, preferred_width, current_atlas_width, max_atlas_width, glyph_height,
                    temp_y_offset, temp_y_height, y_data);
    char_codes.push_back(char_code);

    while (glyph_index != 0) {
        char_code = FT_Get_Next_Char(face, char_code, &glyph_index);
        calc_glyph_size(face, char_code, preferred_width, current_atlas_width, max_atlas_width, glyph_height,
                        temp_y_offset, temp_y_height, y_data);
        char_codes.push_back(char_code);
    }
    y_data.emplace_back(temp_y_height, temp_y_offset);

    const auto height = std::get<0>(y_data.back()) + std::get<1>(y_data.back());
    glfr::bitmap my_bitmap(max_atlas_width, height);

    uint32_t offset_x = 0, y_index = 0;

    std::unordered_map<uint64_t, glfr::glyph_info> glyph_infos;

    for (unsigned long current_code: char_codes) {
        error = FT_Load_Char(face, current_code, FT_LOAD_DEFAULT);
        if (error) {
            std::cout << "FT_Load_Char" << std::endl;
            continue;
        }

        error = FT_Render_Glyph(face->glyph, FT_RENDER_MODE_NORMAL);
        if (error) {
            std::cout << "FT_Render_Glyph" << std::endl;
            continue;
        }

        const auto glyph_width = face->glyph->bitmap.width;
        const auto glyph_height = face->glyph->bitmap.rows;

        const auto offset_y = std::get<1>(y_data[y_index]);

        for (auto y = 0; y < glyph_height; y++) {
            for (auto x = 0; x < glyph_width; x++) {
                const auto value = face->glyph->bitmap.buffer[y * glyph_width + x];
                my_bitmap.get(offset_x + x, offset_y + y) = value;
            }
        }

        glyph_infos.emplace(std::piecewise_construct, std::make_tuple(current_code), std::make_tuple(
                current_code, static_cast<float>(face->glyph->advance.x >> 6),
                static_cast<float>(face->glyph->advance.y >> 6),
                static_cast<float>(glyph_width), static_cast<float>(glyph_height),
                static_cast<float>(face->glyph->bitmap_left), static_cast<float>(face->glyph->bitmap_top), offset_x,
                offset_y));

        offset_x += glyph_width + PADDING_X;
        if (offset_x > preferred_width) {
            offset_x = 0;
            ++y_index;
        }
    }

    static const auto hash_map_class = env->FindClass("it/unimi/dsi/fastutil/chars/Char2ObjectOpenHashMap");
    static const auto hash_map_put_method_id = env->GetMethodID(hash_map_class, "put", "(CLjava/lang/Object;)Ljava/lang/Object;");
    static const auto glyph_info_class = env->FindClass("io/karma/glfont/util/GlyphInfo");
    static const auto glyph_info_init_method_id = env->GetMethodID(glyph_info_class, "<init>", "(CFFFFFFFF)V");
    static const auto texture_class = env->FindClass("io/karma/glfont/render/gl/GlTexture");
    static const auto texture_class_init_method_id = env->GetMethodID(texture_class, "<init>", "(Ljava/nio/ByteBuffer;II)V");
    static const auto font_class = env->GetObjectClass(object);
    static const auto font_glyph_infos_field_id = env->GetFieldID(font_class, "glyphInfos", "Lit/unimi/dsi/fastutil/chars/Char2ObjectOpenHashMap;");

    const auto jglyph_infos = env->GetObjectField(object, font_glyph_infos_field_id);

    for (const auto& [ch, glyph_info]: glyph_infos) {
        const auto jglyph_info = env->NewObject(glyph_info_class, glyph_info_init_method_id, static_cast<jchar>(glyph_info.value),
                                                glyph_info.x_advance, glyph_info.y_advance, glyph_info.width, glyph_info.height,
                                                glyph_info.offset_x, glyph_info.offset_y, glyph_info.tex_coord_x, glyph_info.tex_coord_y);

        env->CallObjectMethod(jglyph_infos, hash_map_put_method_id, static_cast<jchar>(glyph_info.value), jglyph_info);
    }

    env->ReleaseByteArrayElements(font_bytes, font_bytes_ptr, 0);

    return env->NewObject(texture_class, texture_class_init_method_id,
                          env->NewDirectByteBuffer(my_bitmap.get_data(), my_bitmap.get_width() * my_bitmap.get_height()),
                          static_cast<jint>(my_bitmap.get_width()), static_cast<jint>(my_bitmap.get_height()));
}

#ifdef __cplusplus
}
#endif