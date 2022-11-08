cmake_minimum_required(VERSION 3.22)
project(glfont_native)

set(CMAKE_CXX_STANDARD 23)

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/lib)

find_package(JNI REQUIRED)

set(GLFONT_DEPENDENY_DIR ${CMAKE_SOURCE_DIR}/dependencies)

include_directories(${JNI_INCLUDE_DIRS})
if(WIN32)
    include_directories(${GLFONT_DEPENDENY_DIR}/include_win32)
endif()

file(GLOB_RECURSE GLFONT_NATIVE_SOURCE_FILES ${CMAKE_SOURCE_DIR}/src/*.c**)
file(GLOB_RECURSE GLFONT_NATIVE_HEADER_FILES ${CMAKE_SOURCE_DIR}/src/*.h**)

add_library(glfont_native SHARED ${GLFONT_NATIVE_SOURCE_FILES} ${GLFONT_NATIVE_HEADER_FILES})
if(WIN32)
    target_link_libraries(glfont_native ${GLFONT_DEPENDENY_DIR}/lib/freetype.lib)
endif()