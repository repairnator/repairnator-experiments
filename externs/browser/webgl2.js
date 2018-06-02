/*
 * Copyright 2018 The Closure Compiler Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Definitions for WebGL functions as described at
 * http://www.khronos.org/registry/webgl/specs/latest/
 *
 * This file is current up to the WebGL 2.0 spec.
 *
 * This relies on webgl.js and html5.js being included for WebGL1, Canvas and
 * Typed Array support.
 *
 * @externs
 */


/**
 * @constructor
 * @extends {WebGLRenderingContext}
 */
function WebGL2RenderingContext() {}



/** @type {number} */
WebGL2RenderingContext.UNPACK_ROW_LENGTH;

/** @type {number} */
WebGL2RenderingContext.UNPACK_SKIP_ROWS;

/** @type {number} */
WebGL2RenderingContext.UNPACK_SKIP_PIXELS;

/** @type {number} */
WebGL2RenderingContext.PACK_ROW_LENGTH;

/** @type {number} */
WebGL2RenderingContext.PACK_SKIP_ROWS;

/** @type {number} */
WebGL2RenderingContext.PACK_SKIP_PIXELS;

/** @type {number} */
WebGL2RenderingContext.COLOR;

/** @type {number} */
WebGL2RenderingContext.DEPTH;

/** @type {number} */
WebGL2RenderingContext.STENCIL;

/** @type {number} */
WebGL2RenderingContext.RED;

/** @type {number} */
WebGL2RenderingContext.RGB8;

/** @type {number} */
WebGL2RenderingContext.RGBA8;

/** @type {number} */
WebGL2RenderingContext.RGB10_A2;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_BINDING_3D;

/** @type {number} */
WebGL2RenderingContext.UNPACK_SKIP_IMAGES;

/** @type {number} */
WebGL2RenderingContext.UNPACK_IMAGE_HEIGHT;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_3D;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_WRAP_R;

/** @type {number} */
WebGL2RenderingContext.MAX_3D_TEXTURE_SIZE;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_2_10_10_10_REV;

/** @type {number} */
WebGL2RenderingContext.MAX_ELEMENTS_VERTICES;

/** @type {number} */
WebGL2RenderingContext.MAX_ELEMENTS_INDICES;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_MIN_LOD;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_MAX_LOD;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_BASE_LEVEL;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_MAX_LEVEL;

/** @type {number} */
WebGL2RenderingContext.MIN;

/** @type {number} */
WebGL2RenderingContext.MAX;

/** @type {number} */
WebGL2RenderingContext.DEPTH_COMPONENT24;

/** @type {number} */
WebGL2RenderingContext.MAX_TEXTURE_LOD_BIAS;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_COMPARE_MODE;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_COMPARE_FUNC;

/** @type {number} */
WebGL2RenderingContext.CURRENT_QUERY;

/** @type {number} */
WebGL2RenderingContext.QUERY_RESULT;

/** @type {number} */
WebGL2RenderingContext.QUERY_RESULT_AVAILABLE;

/** @type {number} */
WebGL2RenderingContext.STREAM_READ;

/** @type {number} */
WebGL2RenderingContext.STREAM_COPY;

/** @type {number} */
WebGL2RenderingContext.STATIC_READ;

/** @type {number} */
WebGL2RenderingContext.STATIC_COPY;

/** @type {number} */
WebGL2RenderingContext.DYNAMIC_READ;

/** @type {number} */
WebGL2RenderingContext.DYNAMIC_COPY;

/** @type {number} */
WebGL2RenderingContext.MAX_DRAW_BUFFERS;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER0;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER1;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER2;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER3;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER4;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER5;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER6;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER7;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER8;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER9;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER10;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER11;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER12;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER13;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER14;

/** @type {number} */
WebGL2RenderingContext.DRAW_BUFFER15;

/** @type {number} */
WebGL2RenderingContext.MAX_FRAGMENT_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.MAX_VERTEX_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.SAMPLER_3D;

/** @type {number} */
WebGL2RenderingContext.SAMPLER_2D_SHADOW;

/** @type {number} */
WebGL2RenderingContext.FRAGMENT_SHADER_DERIVATIVE_HINT;

/** @type {number} */
WebGL2RenderingContext.PIXEL_PACK_BUFFER;

/** @type {number} */
WebGL2RenderingContext.PIXEL_UNPACK_BUFFER;

/** @type {number} */
WebGL2RenderingContext.PIXEL_PACK_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.PIXEL_UNPACK_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.FLOAT_MAT2x3;

/** @type {number} */
WebGL2RenderingContext.FLOAT_MAT2x4;

/** @type {number} */
WebGL2RenderingContext.FLOAT_MAT3x2;

/** @type {number} */
WebGL2RenderingContext.FLOAT_MAT3x4;

/** @type {number} */
WebGL2RenderingContext.FLOAT_MAT4x2;

/** @type {number} */
WebGL2RenderingContext.FLOAT_MAT4x3;

/** @type {number} */
WebGL2RenderingContext.SRGB;

/** @type {number} */
WebGL2RenderingContext.SRGB8;

/** @type {number} */
WebGL2RenderingContext.SRGB8_ALPHA8;

/** @type {number} */
WebGL2RenderingContext.COMPARE_REF_TO_TEXTURE;

/** @type {number} */
WebGL2RenderingContext.RGBA32F;

/** @type {number} */
WebGL2RenderingContext.RGB32F;

/** @type {number} */
WebGL2RenderingContext.RGBA16F;

/** @type {number} */
WebGL2RenderingContext.RGB16F;

/** @type {number} */
WebGL2RenderingContext.VERTEX_ATTRIB_ARRAY_INTEGER;

/** @type {number} */
WebGL2RenderingContext.MAX_ARRAY_TEXTURE_LAYERS;

/** @type {number} */
WebGL2RenderingContext.MIN_PROGRAM_TEXEL_OFFSET;

/** @type {number} */
WebGL2RenderingContext.MAX_PROGRAM_TEXEL_OFFSET;

/** @type {number} */
WebGL2RenderingContext.MAX_VARYING_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_BINDING_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.R11F_G11F_B10F;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_10F_11F_11F_REV;

/** @type {number} */
WebGL2RenderingContext.RGB9_E5;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_5_9_9_9_REV;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_BUFFER_MODE;

/** @type {number} */
WebGL2RenderingContext.MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_VARYINGS;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_BUFFER_START;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_BUFFER_SIZE;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN;

/** @type {number} */
WebGL2RenderingContext.RASTERIZER_DISCARD;

/** @type {number} */
WebGL2RenderingContext.MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8C8A;

/** @type {number} */
WebGL2RenderingContext.MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS;

/** @type {number} */
WebGL2RenderingContext.INTERLEAVED_ATTRIBS;

/** @type {number} */
WebGL2RenderingContext.SEPARATE_ATTRIBS;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_BUFFER;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.RGBA32UI;

/** @type {number} */
WebGL2RenderingContext.RGB32UI;

/** @type {number} */
WebGL2RenderingContext.RGBA16UI;

/** @type {number} */
WebGL2RenderingContext.RGB16UI;

/** @type {number} */
WebGL2RenderingContext.RGBA8UI;

/** @type {number} */
WebGL2RenderingContext.RGB8UI;

/** @type {number} */
WebGL2RenderingContext.RGBA32I;

/** @type {number} */
WebGL2RenderingContext.RGB32I;

/** @type {number} */
WebGL2RenderingContext.RGBA16I;

/** @type {number} */
WebGL2RenderingContext.RGB16I;

/** @type {number} */
WebGL2RenderingContext.RGBA8I;

/** @type {number} */
WebGL2RenderingContext.RGB8I;

/** @type {number} */
WebGL2RenderingContext.RED_INTEGER;

/** @type {number} */
WebGL2RenderingContext.RGB_INTEGER;

/** @type {number} */
WebGL2RenderingContext.RGBA_INTEGER;

/** @type {number} */
WebGL2RenderingContext.SAMPLER_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.SAMPLER_2D_ARRAY_SHADOW;

/** @type {number} */
WebGL2RenderingContext.SAMPLER_CUBE_SHADOW;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_VEC2;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_VEC3;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_VEC4;

/** @type {number} */
WebGL2RenderingContext.INT_SAMPLER_2D;

/** @type {number} */
WebGL2RenderingContext.INT_SAMPLER_3D;

/** @type {number} */
WebGL2RenderingContext.INT_SAMPLER_CUBE;

/** @type {number} */
WebGL2RenderingContext.INT_SAMPLER_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_SAMPLER_2D;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_SAMPLER_3D;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_SAMPLER_CUBE;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_SAMPLER_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.DEPTH_COMPONENT32F;

/** @type {number} */
WebGL2RenderingContext.DEPTH32F_STENCIL8;

/** @type {number} */
WebGL2RenderingContext.FLOAT_32_UNSIGNED_INT_24_8_REV;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_RED_SIZE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_GREEN_SIZE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_BLUE_SIZE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_DEFAULT;

/** @type {number} */
WebGL2RenderingContext.DEPTH_STENCIL_ATTACHMENT;

/** @type {number} */
WebGL2RenderingContext.DEPTH_STENCIL;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_INT_24_8;

/** @type {number} */
WebGL2RenderingContext.DEPTH24_STENCIL8;

/** @type {number} */
WebGL2RenderingContext.UNSIGNED_NORMALIZED;

/** @type {number} */
WebGL2RenderingContext.DRAW_FRAMEBUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.READ_FRAMEBUFFER;

/** @type {number} */
WebGL2RenderingContext.DRAW_FRAMEBUFFER;

/** @type {number} */
WebGL2RenderingContext.READ_FRAMEBUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.RENDERBUFFER_SAMPLES;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER;

/** @type {number} */
WebGL2RenderingContext.MAX_COLOR_ATTACHMENTS;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT1;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT2;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT3;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT4;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT5;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT6;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT7;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT8;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT9;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT10;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT11;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT12;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT13;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT14;

/** @type {number} */
WebGL2RenderingContext.COLOR_ATTACHMENT15;

/** @type {number} */
WebGL2RenderingContext.FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;

/** @type {number} */
WebGL2RenderingContext.MAX_SAMPLES;

/** @type {number} */
WebGL2RenderingContext.HALF_FLOAT;

/** @type {number} */
WebGL2RenderingContext.RG;

/** @type {number} */
WebGL2RenderingContext.RG_INTEGER;

/** @type {number} */
WebGL2RenderingContext.R8;

/** @type {number} */
WebGL2RenderingContext.RG8;

/** @type {number} */
WebGL2RenderingContext.R16F;

/** @type {number} */
WebGL2RenderingContext.R32F;

/** @type {number} */
WebGL2RenderingContext.RG16F;

/** @type {number} */
WebGL2RenderingContext.RG32F;

/** @type {number} */
WebGL2RenderingContext.R8I;

/** @type {number} */
WebGL2RenderingContext.R8UI;

/** @type {number} */
WebGL2RenderingContext.R16I;

/** @type {number} */
WebGL2RenderingContext.R16UI;

/** @type {number} */
WebGL2RenderingContext.R32I;

/** @type {number} */
WebGL2RenderingContext.R32UI;

/** @type {number} */
WebGL2RenderingContext.RG8I;

/** @type {number} */
WebGL2RenderingContext.RG8UI;

/** @type {number} */
WebGL2RenderingContext.RG16I;

/** @type {number} */
WebGL2RenderingContext.RG16UI;

/** @type {number} */
WebGL2RenderingContext.RG32I;

/** @type {number} */
WebGL2RenderingContext.RG32UI;

/** @type {number} */
WebGL2RenderingContext.VERTEX_ARRAY_BINDING;

/** @type {number} */
WebGL2RenderingContext.R8_SNORM;

/** @type {number} */
WebGL2RenderingContext.RG8_SNORM;

/** @type {number} */
WebGL2RenderingContext.RGB8_SNORM;

/** @type {number} */
WebGL2RenderingContext.RGBA8_SNORM;

/** @type {number} */
WebGL2RenderingContext.SIGNED_NORMALIZED;

/** @type {number} */
WebGL2RenderingContext.COPY_READ_BUFFER;

/** @type {number} */
WebGL2RenderingContext.COPY_WRITE_BUFFER;

/** @type {number} */
WebGL2RenderingContext.COPY_READ_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.COPY_WRITE_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BUFFER;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BUFFER_START;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BUFFER_SIZE;

/** @type {number} */
WebGL2RenderingContext.MAX_VERTEX_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.MAX_FRAGMENT_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.MAX_COMBINED_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.MAX_UNIFORM_BUFFER_BINDINGS;

/** @type {number} */
WebGL2RenderingContext.MAX_UNIFORM_BLOCK_SIZE;

/** @type {number} */
WebGL2RenderingContext.MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BUFFER_OFFSET_ALIGNMENT;

/** @type {number} */
WebGL2RenderingContext.ACTIVE_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_TYPE;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_SIZE;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_INDEX;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_OFFSET;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_ARRAY_STRIDE;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_MATRIX_STRIDE;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_IS_ROW_MAJOR;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_BINDING;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_DATA_SIZE;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_ACTIVE_UNIFORMS;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER;

/** @type {number} */
WebGL2RenderingContext.UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER;

/** @type {number} */
WebGL2RenderingContext.INVALID_INDEX;

/** @type {number} */
WebGL2RenderingContext.MAX_VERTEX_OUTPUT_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.MAX_FRAGMENT_INPUT_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.MAX_SERVER_WAIT_TIMEOUT;

/** @type {number} */
WebGL2RenderingContext.OBJECT_TYPE;

/** @type {number} */
WebGL2RenderingContext.SYNC_CONDITION;

/** @type {number} */
WebGL2RenderingContext.SYNC_STATUS;

/** @type {number} */
WebGL2RenderingContext.SYNC_FLAGS;

/** @type {number} */
WebGL2RenderingContext.SYNC_FENCE;

/** @type {number} */
WebGL2RenderingContext.SYNC_GPU_COMMANDS_COMPLETE;

/** @type {number} */
WebGL2RenderingContext.UNSIGNALED;

/** @type {number} */
WebGL2RenderingContext.SIGNALED;

/** @type {number} */
WebGL2RenderingContext.ALREADY_SIGNALED;

/** @type {number} */
WebGL2RenderingContext.TIMEOUT_EXPIRED;

/** @type {number} */
WebGL2RenderingContext.CONDITION_SATISFIED;

/** @type {number} */
WebGL2RenderingContext.WAIT_FAILED;

/** @type {number} */
WebGL2RenderingContext.SYNC_FLUSH_COMMANDS_BIT;

/** @type {number} */
WebGL2RenderingContext.VERTEX_ATTRIB_ARRAY_DIVISOR;

/** @type {number} */
WebGL2RenderingContext.ANY_SAMPLES_PASSED;

/** @type {number} */
WebGL2RenderingContext.ANY_SAMPLES_PASSED_CONSERVATIVE;

/** @type {number} */
WebGL2RenderingContext.SAMPLER_BINDING;

/** @type {number} */
WebGL2RenderingContext.RGB10_A2UI;

/** @type {number} */
WebGL2RenderingContext.INT_2_10_10_10_REV;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_PAUSED;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_ACTIVE;

/** @type {number} */
WebGL2RenderingContext.TRANSFORM_FEEDBACK_BINDING;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_IMMUTABLE_FORMAT;

/** @type {number} */
WebGL2RenderingContext.MAX_ELEMENT_INDEX;

/** @type {number} */
WebGL2RenderingContext.TEXTURE_IMMUTABLE_LEVELS;

/** @type {number} */
WebGL2RenderingContext.TIMEOUT_IGNORED;

/** @type {number} */
WebGL2RenderingContext.MAX_CLIENT_WAIT_TIMEOUT_WEBGL;


/** @type {number} */
WebGL2RenderingContext.prototype.UNPACK_ROW_LENGTH;

/** @type {number} */
WebGL2RenderingContext.prototype.UNPACK_SKIP_ROWS;

/** @type {number} */
WebGL2RenderingContext.prototype.UNPACK_SKIP_PIXELS;

/** @type {number} */
WebGL2RenderingContext.prototype.PACK_ROW_LENGTH;

/** @type {number} */
WebGL2RenderingContext.prototype.PACK_SKIP_ROWS;

/** @type {number} */
WebGL2RenderingContext.prototype.PACK_SKIP_PIXELS;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH;

/** @type {number} */
WebGL2RenderingContext.prototype.STENCIL;

/** @type {number} */
WebGL2RenderingContext.prototype.RED;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB8;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA8;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB10_A2;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_BINDING_3D;

/** @type {number} */
WebGL2RenderingContext.prototype.UNPACK_SKIP_IMAGES;

/** @type {number} */
WebGL2RenderingContext.prototype.UNPACK_IMAGE_HEIGHT;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_3D;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_WRAP_R;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_3D_TEXTURE_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_2_10_10_10_REV;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_ELEMENTS_VERTICES;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_ELEMENTS_INDICES;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_MIN_LOD;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_MAX_LOD;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_BASE_LEVEL;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_MAX_LEVEL;

/** @type {number} */
WebGL2RenderingContext.prototype.MIN;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH_COMPONENT24;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_TEXTURE_LOD_BIAS;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_COMPARE_MODE;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_COMPARE_FUNC;

/** @type {number} */
WebGL2RenderingContext.prototype.CURRENT_QUERY;

/** @type {number} */
WebGL2RenderingContext.prototype.QUERY_RESULT;

/** @type {number} */
WebGL2RenderingContext.prototype.QUERY_RESULT_AVAILABLE;

/** @type {number} */
WebGL2RenderingContext.prototype.STREAM_READ;

/** @type {number} */
WebGL2RenderingContext.prototype.STREAM_COPY;

/** @type {number} */
WebGL2RenderingContext.prototype.STATIC_READ;

/** @type {number} */
WebGL2RenderingContext.prototype.STATIC_COPY;

/** @type {number} */
WebGL2RenderingContext.prototype.DYNAMIC_READ;

/** @type {number} */
WebGL2RenderingContext.prototype.DYNAMIC_COPY;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_DRAW_BUFFERS;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER0;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER1;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER2;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER3;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER4;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER5;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER6;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER7;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER8;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER9;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER10;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER11;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER12;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER13;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER14;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_BUFFER15;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_FRAGMENT_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_VERTEX_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.SAMPLER_3D;

/** @type {number} */
WebGL2RenderingContext.prototype.SAMPLER_2D_SHADOW;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAGMENT_SHADER_DERIVATIVE_HINT;

/** @type {number} */
WebGL2RenderingContext.prototype.PIXEL_PACK_BUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.PIXEL_UNPACK_BUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.PIXEL_PACK_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.PIXEL_UNPACK_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_MAT2x3;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_MAT2x4;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_MAT3x2;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_MAT3x4;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_MAT4x2;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_MAT4x3;

/** @type {number} */
WebGL2RenderingContext.prototype.SRGB;

/** @type {number} */
WebGL2RenderingContext.prototype.SRGB8;

/** @type {number} */
WebGL2RenderingContext.prototype.SRGB8_ALPHA8;

/** @type {number} */
WebGL2RenderingContext.prototype.COMPARE_REF_TO_TEXTURE;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA32F;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB32F;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA16F;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB16F;

/** @type {number} */
WebGL2RenderingContext.prototype.VERTEX_ATTRIB_ARRAY_INTEGER;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_ARRAY_TEXTURE_LAYERS;

/** @type {number} */
WebGL2RenderingContext.prototype.MIN_PROGRAM_TEXEL_OFFSET;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_PROGRAM_TEXEL_OFFSET;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_VARYING_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_BINDING_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.prototype.R11F_G11F_B10F;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_10F_11F_11F_REV;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB9_E5;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_5_9_9_9_REV;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_BUFFER_MODE;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_VARYINGS;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_BUFFER_START;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_BUFFER_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN;

/** @type {number} */
WebGL2RenderingContext.prototype.RASTERIZER_DISCARD;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS =
    0x8C8A;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS;

/** @type {number} */
WebGL2RenderingContext.prototype.INTERLEAVED_ATTRIBS;

/** @type {number} */
WebGL2RenderingContext.prototype.SEPARATE_ATTRIBS;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_BUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA32UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB32UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA16UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB16UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA8UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB8UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA32I;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB32I;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA16I;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB16I;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA8I;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB8I;

/** @type {number} */
WebGL2RenderingContext.prototype.RED_INTEGER;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB_INTEGER;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA_INTEGER;

/** @type {number} */
WebGL2RenderingContext.prototype.SAMPLER_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.prototype.SAMPLER_2D_ARRAY_SHADOW;

/** @type {number} */
WebGL2RenderingContext.prototype.SAMPLER_CUBE_SHADOW;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_VEC2;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_VEC3;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_VEC4;

/** @type {number} */
WebGL2RenderingContext.prototype.INT_SAMPLER_2D;

/** @type {number} */
WebGL2RenderingContext.prototype.INT_SAMPLER_3D;

/** @type {number} */
WebGL2RenderingContext.prototype.INT_SAMPLER_CUBE;

/** @type {number} */
WebGL2RenderingContext.prototype.INT_SAMPLER_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_SAMPLER_2D;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_SAMPLER_3D;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_SAMPLER_CUBE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_SAMPLER_2D_ARRAY;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH_COMPONENT32F;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH32F_STENCIL8;

/** @type {number} */
WebGL2RenderingContext.prototype.FLOAT_32_UNSIGNED_INT_24_8_REV;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_RED_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_GREEN_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_BLUE_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_DEFAULT;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH_STENCIL_ATTACHMENT;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH_STENCIL;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_INT_24_8;

/** @type {number} */
WebGL2RenderingContext.prototype.DEPTH24_STENCIL8;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNED_NORMALIZED;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_FRAMEBUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.READ_FRAMEBUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.DRAW_FRAMEBUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.READ_FRAMEBUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.RENDERBUFFER_SAMPLES;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_COLOR_ATTACHMENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT1;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT2;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT3;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT4;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT5;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT6;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT7;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT8;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT9;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT10;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT11;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT12;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT13;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT14;

/** @type {number} */
WebGL2RenderingContext.prototype.COLOR_ATTACHMENT15;

/** @type {number} */
WebGL2RenderingContext.prototype.FRAMEBUFFER_INCOMPLETE_MULTISAMPLE;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_SAMPLES;

/** @type {number} */
WebGL2RenderingContext.prototype.HALF_FLOAT;

/** @type {number} */
WebGL2RenderingContext.prototype.RG;

/** @type {number} */
WebGL2RenderingContext.prototype.RG_INTEGER;

/** @type {number} */
WebGL2RenderingContext.prototype.R8;

/** @type {number} */
WebGL2RenderingContext.prototype.RG8;

/** @type {number} */
WebGL2RenderingContext.prototype.R16F;

/** @type {number} */
WebGL2RenderingContext.prototype.R32F;

/** @type {number} */
WebGL2RenderingContext.prototype.RG16F;

/** @type {number} */
WebGL2RenderingContext.prototype.RG32F;

/** @type {number} */
WebGL2RenderingContext.prototype.R8I;

/** @type {number} */
WebGL2RenderingContext.prototype.R8UI;

/** @type {number} */
WebGL2RenderingContext.prototype.R16I;

/** @type {number} */
WebGL2RenderingContext.prototype.R16UI;

/** @type {number} */
WebGL2RenderingContext.prototype.R32I;

/** @type {number} */
WebGL2RenderingContext.prototype.R32UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RG8I;

/** @type {number} */
WebGL2RenderingContext.prototype.RG8UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RG16I;

/** @type {number} */
WebGL2RenderingContext.prototype.RG16UI;

/** @type {number} */
WebGL2RenderingContext.prototype.RG32I;

/** @type {number} */
WebGL2RenderingContext.prototype.RG32UI;

/** @type {number} */
WebGL2RenderingContext.prototype.VERTEX_ARRAY_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.R8_SNORM;

/** @type {number} */
WebGL2RenderingContext.prototype.RG8_SNORM;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB8_SNORM;

/** @type {number} */
WebGL2RenderingContext.prototype.RGBA8_SNORM;

/** @type {number} */
WebGL2RenderingContext.prototype.SIGNED_NORMALIZED;

/** @type {number} */
WebGL2RenderingContext.prototype.COPY_READ_BUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.COPY_WRITE_BUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.COPY_READ_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.COPY_WRITE_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BUFFER;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BUFFER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BUFFER_START;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BUFFER_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_VERTEX_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_FRAGMENT_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_COMBINED_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_UNIFORM_BUFFER_BINDINGS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_UNIFORM_BLOCK_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BUFFER_OFFSET_ALIGNMENT;

/** @type {number} */
WebGL2RenderingContext.prototype.ACTIVE_UNIFORM_BLOCKS;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_TYPE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_INDEX;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_OFFSET;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_ARRAY_STRIDE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_MATRIX_STRIDE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_IS_ROW_MAJOR;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_DATA_SIZE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_ACTIVE_UNIFORMS;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER;

/** @type {number} */
WebGL2RenderingContext.prototype.UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER;

/** @type {number} */
WebGL2RenderingContext.prototype.INVALID_INDEX;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_VERTEX_OUTPUT_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_FRAGMENT_INPUT_COMPONENTS;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_SERVER_WAIT_TIMEOUT;

/** @type {number} */
WebGL2RenderingContext.prototype.OBJECT_TYPE;

/** @type {number} */
WebGL2RenderingContext.prototype.SYNC_CONDITION;

/** @type {number} */
WebGL2RenderingContext.prototype.SYNC_STATUS;

/** @type {number} */
WebGL2RenderingContext.prototype.SYNC_FLAGS;

/** @type {number} */
WebGL2RenderingContext.prototype.SYNC_FENCE;

/** @type {number} */
WebGL2RenderingContext.prototype.SYNC_GPU_COMMANDS_COMPLETE;

/** @type {number} */
WebGL2RenderingContext.prototype.UNSIGNALED;

/** @type {number} */
WebGL2RenderingContext.prototype.SIGNALED;

/** @type {number} */
WebGL2RenderingContext.prototype.ALREADY_SIGNALED;

/** @type {number} */
WebGL2RenderingContext.prototype.TIMEOUT_EXPIRED;

/** @type {number} */
WebGL2RenderingContext.prototype.CONDITION_SATISFIED;

/** @type {number} */
WebGL2RenderingContext.prototype.WAIT_FAILED;

/** @type {number} */
WebGL2RenderingContext.prototype.SYNC_FLUSH_COMMANDS_BIT;

/** @type {number} */
WebGL2RenderingContext.prototype.VERTEX_ATTRIB_ARRAY_DIVISOR;

/** @type {number} */
WebGL2RenderingContext.prototype.ANY_SAMPLES_PASSED;

/** @type {number} */
WebGL2RenderingContext.prototype.ANY_SAMPLES_PASSED_CONSERVATIVE;

/** @type {number} */
WebGL2RenderingContext.prototype.SAMPLER_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.RGB10_A2UI;

/** @type {number} */
WebGL2RenderingContext.prototype.INT_2_10_10_10_REV;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_PAUSED;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_ACTIVE;

/** @type {number} */
WebGL2RenderingContext.prototype.TRANSFORM_FEEDBACK_BINDING;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_IMMUTABLE_FORMAT;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_ELEMENT_INDEX;

/** @type {number} */
WebGL2RenderingContext.prototype.TEXTURE_IMMUTABLE_LEVELS;

/** @type {number} */
WebGL2RenderingContext.prototype.TIMEOUT_IGNORED;

/** @type {number} */
WebGL2RenderingContext.prototype.MAX_CLIENT_WAIT_TIMEOUT_WEBGL;

/* Buffer objects */

/**
 * @param {number} target
 * @param {?ArrayBufferView|?ArrayBuffer|number} data
 * @param {number} usage
 * @param {number=} opt_srcOffset
 * @param {number=} opt_length
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.bufferData = function(
    target, data, usage, opt_srcOffset, opt_length) {};

/**
 * @param {number} target
 * @param {number} offset
 * @param {?ArrayBufferView|?ArrayBuffer} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_length
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.bufferSubData = function(
    target, offset, data, opt_srcOffset, opt_length) {};

/**
 * @param {number} readTarget
 * @param {number} writeTarget
 * @param {number} readOffset
 * @param {number} writeOffset
 * @param {number} size
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.copyBufferSubData = function(
    readTarget, writeTarget, readOffset, writeOffset, size) {};

/**
 * @param {number} target
 * @param {number} srcByteOffset
 * @param {?ArrayBufferView|?ArrayBuffer} dstBuffer
 * @param {number=} opt_dstOffset
 * @param {number=} opt_length
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.getBufferSubData = function(
    target, srcByteOffset, dstBuffer, opt_dstOffset, opt_length) {};

/* Framebuffer objects */

/**
 * @param {number} srcX0
 * @param {number} srcY0
 * @param {number} srcX1
 * @param {number} srcY1
 * @param {number} dstX0
 * @param {number} dstY0
 * @param {number} dstX1
 * @param {number} dstY1
 * @param {number} mask
 * @param {number} filter
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.blitFramebuffer = function(
    srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter) {};

/**
 * @param {number} target
 * @param {number} attachment
 * @param {?WebGLTexture} texture
 * @param {number} level
 * @param {number} layer
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.framebufferTextureLayer = function(
    target, attachment, texture, level, layer) {};

/**
 * @param {number} target
 * @param {!Array<number>} attachments
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.invalidateFramebuffer = function(
    target, attachments) {};

/**
 * @param {number} target
 * @param {!Array<number>} attachments
 * @param {number} x
 * @param {number} y
 * @param {number} width
 * @param {number} height
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.invalidateSubFramebuffer = function(
    target, attachments, x, y, width, height) {};

/**
 * @param {number} src
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.readBuffer = function(src) {};

/* Renderbuffer objects */

/**
 * @param {number} target
 * @param {number} internalformat
 * @param {number} pname
 * @return {*}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getInternalformatParameter = function(
    target, internalformat, pname) {};

/**
 * @param {number} target
 * @param {number} samples
 * @param {number} internalformat
 * @param {number} width
 * @param {number} height
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.renderbufferStorageMultisample = function(
    target, samples, internalformat, width, height) {};

/* Texture objects */

/**
 * @param {number} target
 * @param {number} levels
 * @param {number} internalformat
 * @param {number} width
 * @param {number} height
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.texStorage2D = function(
    target, levels, internalformat, width, height) {};

/**
 * @param {number} target
 * @param {number} levels
 * @param {number} internalformat
 * @param {number} width
 * @param {number} height
 * @param {number} depth
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.texStorage3D = function(
    target, levels, internalformat, width, height, depth) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} internalformat
 * @param {number} formatOrWidth
 * @param {number} typeOrHeight
 * @param {?ImageData|?HTMLImageElement|?HTMLCanvasElement|?HTMLVideoElement|number} imgOrBorder
 * @param {number=} opt_format
 * @param {number=} opt_type
 * @param {?ArrayBufferView|?ImageData|?HTMLImageElement|?HTMLCanvasElement|?HTMLVideoElement|number=} opt_imgOrOffset
 * @param {number=} opt_srcOffset
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.texImage2D = function(
    target, level, internalformat, formatOrWidth, typeOrHeight, imgOrBorder,
    opt_format, opt_type, opt_imgOrOffset, opt_srcOffset) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} xoffset
 * @param {number} yoffset
 * @param {number} formatOrWidth
 * @param {number} typeOrHeight
 * @param {?ImageData|?HTMLImageElement|?HTMLCanvasElement|?HTMLVideoElement|number} dataOrFormat
 * @param {number=} opt_type
 * @param {?ArrayBufferView|?ImageData|?HTMLImageElement|?HTMLCanvasElement|?HTMLVideoElement|number=} opt_imgOrOffset
 * @param {number=} opt_srcOffset
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.texSubImage2D = function(
    target, level, xoffset, yoffset, formatOrWidth, typeOrHeight, dataOrFormat,
    opt_type, opt_imgOrOffset, opt_srcOffset) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} internalformat
 * @param {number} width
 * @param {number} height
 * @param {number} depth
 * @param {number} border
 * @param {number} format
 * @param {number} type
 * @param {?ArrayBufferView|?ImageData|?HTMLImageElement|?HTMLCanvasElement|?HTMLVideoElement|number} srcData
 * @param {number=} opt_srcOffset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.texImage3D = function(
    target, level, internalformat, width, height, depth, border, format, type,
    srcData, opt_srcOffset) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} xoffset
 * @param {number} yoffset
 * @param {number} zoffset
 * @param {number} width
 * @param {number} height
 * @param {number} depth
 * @param {number} format
 * @param {number} type
 * @param {?ArrayBufferView|?ImageData|?HTMLImageElement|?HTMLCanvasElement|?HTMLVideoElement|number} srcData
 * @param {number=} opt_srcOffset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.texSubImage3D = function(
    target, level, xoffset, yoffset, zoffset, width, height, depth, format,
    type, srcData, opt_srcOffset) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} xoffset
 * @param {number} yoffset
 * @param {number} zoffset
 * @param {number} x
 * @param {number} y
 * @param {number} width
 * @param {number} height
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.copyTexSubImage3D = function(
    target, level, xoffset, yoffset, zoffset, x, y, width, height) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} internalformat
 * @param {number} width
 * @param {number} height
 * @param {number} border
 * @param {?ArrayBufferView|number} srcDataOrSize
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLengthOverride
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.compressedTexImage2D = function(
    target, level, internalformat, width, height, border, srcDataOrSize,
    opt_srcOffset, opt_srcLengthOverride) {};

/**
 * @param {number} target
 * @param {number} level
 * @param {number} internalformat
 * @param {number} width
 * @param {number} height
 * @param {number} depth
 * @param {number} border
 * @param {!ArrayBufferView|number} srcDataOrSize
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLengthOverride
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.compressedTexImage3D = function(
    target, level, internalformat, width, height, depth, border, srcDataOrSize,
    opt_srcOffset, opt_srcLengthOverride) {};


/**
 * @param {number} target
 * @param {number} level
 * @param {number} xoffset
 * @param {number} yoffset
 * @param {number} width
 * @param {number} height
 * @param {number} format
 * @param {?ArrayBufferView|number} srcDataOrSize
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLengthOverride
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.compressedTexSubImage2D = function(
    target, level, xoffset, yoffset, width, height, format, srcDataOrSize,
    opt_srcOffset, opt_srcLengthOverride) {};


/**
 * @param {number} target
 * @param {number} level
 * @param {number} xoffset
 * @param {number} yoffset
 * @param {number} zoffset
 * @param {number} width
 * @param {number} height
 * @param {number} depth
 * @param {number} format
 * @param {!ArrayBufferView|number} srcDataOrSize
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLengthOverride
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.compressedTexSubImage3D = function(
    target, level, xoffset, yoffset, zoffset, width, height, depth, format,
    srcDataOrSize, opt_srcOffset, opt_srcLengthOverride) {};

/* Programs and shaders */

/**
 * @param {!WebGLProgram} program
 * @param {string} name
 * @return {number}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getFragDataLocation = function(
    program, name) {};

/* Uniforms */

/**
 * @param {?WebGLUniformLocation} location
 * @param {number} v0
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform1ui = function(location, v0) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {number} v0
 * @param {number} v1
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform2ui = function(location, v0, v1) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {number} v0
 * @param {number} v1
 * @param {number} v2
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform3ui = function(location, v0, v1, v2) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {number} v0
 * @param {number} v1
 * @param {number} v2
 * @param {number} v3
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform4ui = function(
    location, v0, v1, v2, v3) {};


/**
 * @param {?WebGLUniformLocation} location
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform1fv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform2fv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform3fv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform4fv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Int32Array|?Array<number>|?Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform1iv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Int32Array|?Array<number>|?Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform2iv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Int32Array|?Array<number>|?Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform3iv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {?Int32Array|?Array<number>|?Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniform4iv = function(
    location, data, opt_srcOffset, opt_srcLength) {};


/**
 * @param {?WebGLUniformLocation} location
 * @param {!Uint32Array|!Array<number>|!Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform1uiv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {!Uint32Array|!Array<number>|!Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform2uiv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {!Uint32Array|!Array<number>|!Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform3uiv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {!Uint32Array|!Array<number>|!Array<boolean>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniform4uiv = function(
    location, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniformMatrix2fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {!Float32Array|!Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformMatrix3x2fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {!Float32Array|!Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformMatrix4x2fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {!Float32Array|!Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformMatrix2x3fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniformMatrix3fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {!Float32Array|!Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformMatrix4x3fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {!Float32Array|!Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformMatrix2x4fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {!Float32Array|!Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformMatrix3x4fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/**
 * @param {?WebGLUniformLocation} location
 * @param {boolean} transpose
 * @param {?Float32Array|?Array<number>} data
 * @param {number=} opt_srcOffset
 * @param {number=} opt_srcLength
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.uniformMatrix4fv = function(
    location, transpose, data, opt_srcOffset, opt_srcLength) {};

/* Vertex attribs */

/**
 * @param {number} index
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.vertexAttribI4i = function(
    index, x, y, z, w) {};

/**
 * @param {number} index
 * @param {!Int32Array|!Array<number>|!Array<boolean>} values
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.vertexAttribI4iv = function(index, values) {};

/**
 * @param {number} index
 * @param {number} x
 * @param {number} y
 * @param {number} z
 * @param {number} w
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.vertexAttribI4ui = function(
    index, x, y, z, w) {};

/**
 * @param {number} index
 * @param {!Uint32Array|!Array<number>|!Array<boolean>} values
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.vertexAttribI4uiv = function(index, values) {};

/**
 * @param {number} index
 * @param {number} size
 * @param {number} type
 * @param {number} stride
 * @param {number} offset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.vertexAttribIPointer = function(
    index, size, type, stride, offset) {};

/* Writing to the drawing buffer */

/**
 * @param {number} index
 * @param {number} divisor
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.vertexAttribDivisor = function(
    index, divisor) {};

/**
 * @param {number} mode
 * @param {number} first
 * @param {number} count
 * @param {number} instanceCount
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.drawArraysInstanced = function(
    mode, first, count, instanceCount) {};

/**
 * @param {number} mode
 * @param {number} count
 * @param {number} type
 * @param {number} offset
 * @param {number} instanceCount
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.drawElementsInstanced = function(
    mode, count, type, offset, instanceCount) {};

/**
 * @param {number} mode
 * @param {number} start
 * @param {number} end
 * @param {number} count
 * @param {number} type
 * @param {number} offset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.drawRangeElements = function(
    mode, start, end, count, type, offset) {};

/* Reading back pixels */

/**
 * @param {number} x
 * @param {number} y
 * @param {number} width
 * @param {number} height
 * @param {number} format
 * @param {number} type
 * @param {?ArrayBufferView|number} dstDataOrOffset
 * @param {number=} opt_dstOffset
 * @return {undefined}
 * @override
 */
WebGL2RenderingContext.prototype.readPixels = function(
    x, y, width, height, format, type, dstDataOrOffset, opt_dstOffset) {};

/* Multiple Render Targets */

/**
 * @param {!Array<number>} buffers
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.drawBuffers = function(buffers) {};


/**
 * @param {number} buffer
 * @param {number} drawbuffer
 * @param {!Float32Array|!Array<number>} values
 * @param {number=} opt_srcOffset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.clearBufferfv = function(
    buffer, drawbuffer, values, opt_srcOffset) {};

/**
 * @param {number} buffer
 * @param {number} drawbuffer
 * @param {!Int32Array|!Array<number>|!Array<boolean>} values
 * @param {number=} opt_srcOffset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.clearBufferiv = function(
    buffer, drawbuffer, values, opt_srcOffset) {};

/**
 * @param {number} buffer
 * @param {number} drawbuffer
 * @param {!Uint32Array|!Array<number>|!Array<boolean>} values
 * @param {number=} opt_srcOffset
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.clearBufferuiv = function(
    buffer, drawbuffer, values, opt_srcOffset) {};

/**
 * @param {number} buffer
 * @param {number} drawbuffer
 * @param {number} depth
 * @param {number} stencil
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.clearBufferfi = function(
    buffer, drawbuffer, depth, stencil) {};

/* Query Objects */

/**
 * @return {?WebGLQuery}
 */
WebGL2RenderingContext.prototype.createQuery = function() {};

/**
 * @param {?WebGLQuery} query
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.deleteQuery = function(query) {};

/**
 * @param {?WebGLQuery} query
 * @return {boolean}
 */
WebGL2RenderingContext.prototype.isQuery = function(query) {};

/**
 * @param {number} target
 * @param {!WebGLQuery} query
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.beginQuery = function(target, query) {};

/**
 * @param {number} target
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.endQuery = function(target) {};

/**
 * @param {number} target
 * @param {number} pname
 * @return {?WebGLQuery}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getQuery = function(target, pname) {};

/**
 * @param {!WebGLQuery} query
 * @param {number} pname
 * @return {*}
 */
WebGL2RenderingContext.prototype.getQueryParameter = function(query, pname) {};

/* Sampler Objects */

/**
 * @return {?WebGLSampler}
 */
WebGL2RenderingContext.prototype.createSampler = function() {};

/**
 * @param {?WebGLSampler} sampler
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.deleteSampler = function(sampler) {};

/**
 * @param {?WebGLSampler} sampler
 * @return {boolean}
 */
WebGL2RenderingContext.prototype.isSampler = function(sampler) {};

/**
 * @param {number} unit
 * @param {?WebGLSampler} sampler
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.bindSampler = function(unit, sampler) {};

/**
 * @param {!WebGLSampler} sampler
 * @param {number} pname
 * @param {number} param
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.samplerParameteri = function(
    sampler, pname, param) {};

/**
 * @param {!WebGLSampler} sampler
 * @param {number} pname
 * @param {number} param
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.samplerParameterf = function(
    sampler, pname, param) {};

/**
 * @param {!WebGLSampler} sampler
 * @param {number} pname
 * @return {*}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getSamplerParameter = function(
    sampler, pname) {};

/* Sync objects */

/**
 * @param {number} condition
 * @param {number} flags
 * @return {?WebGLSync}
 */
WebGL2RenderingContext.prototype.fenceSync = function(condition, flags) {};

/**
 * @param {?WebGLSync} sync
 * @return {boolean}
 */
WebGL2RenderingContext.prototype.isSync = function(sync) {};

/**
 * @param {?WebGLSync} sync
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.deleteSync = function(sync) {};

/**
 * @param {!WebGLSync} sync
 * @param {number} flags
 * @param {number} timeout
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.clientWaitSync = function(
    sync, flags, timeout) {};

/**
 * @param {!WebGLSync} sync
 * @param {number} flags
 * @param {number} timeout
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.waitSync = function(sync, flags, timeout) {};

/**
 * @param {!WebGLSync} sync
 * @param {number} pname
 * @return {*}
 */
WebGL2RenderingContext.prototype.getSyncParameter = function(sync, pname) {};

/* Transform Feedback */

/**
 * @return {?WebGLTransformFeedback}
 */
WebGL2RenderingContext.prototype.createTransformFeedback = function() {};

/**
 * @param {?WebGLTransformFeedback} tf
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.deleteTransformFeedback = function(tf) {};

/**
 * @param {?WebGLTransformFeedback} tf
 * @return {boolean}
 */
WebGL2RenderingContext.prototype.isTransformFeedback = function(tf) {};

/**
 * @param {number} target
 * @param {?WebGLTransformFeedback} tf
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.bindTransformFeedback = function(
    target, tf) {};

/**
 * @param {number} primitiveMode
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.beginTransformFeedback = function(
    primitiveMode) {};

/**
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.endTransformFeedback = function() {};

/**
 * @param {!WebGLProgram} program
 * @param {!Array<string>} varyings
 * @param {number} bufferMode
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.transformFeedbackVaryings = function(
    program, varyings, bufferMode) {};

/**
 * @param {!WebGLProgram} program
 * @param {number} index
 * @return {?WebGLActiveInfo}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getTransformFeedbackVarying = function(
    program, index) {};

/**
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.pauseTransformFeedback = function() {};

/**
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.resumeTransformFeedback = function() {};

/* Uniform Buffer Objects and Transform Feedback Buffers */

/**
 * @param {number} target
 * @param {number} index
 * @param {?WebGLBuffer} buffer
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.bindBufferBase = function(
    target, index, buffer) {};

/**
 * @param {number} target
 * @param {number} index
 * @param {?WebGLBuffer} buffer
 * @param {number} offset
 * @param {number} size
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.bindBufferRange = function(
    target, index, buffer, offset, size) {};

/**
 * @param {number} target
 * @param {number} index
 * @return {*}
 */
WebGL2RenderingContext.prototype.getIndexedParameter = function(
    target, index) {};

/**
 * @param {!WebGLProgram} program
 * @param {!Array<string>} uniformNames
 * @return {!Array<number>}
 */
WebGL2RenderingContext.prototype.getUniformIndices = function(
    program, uniformNames) {};

/**
 * @param {!WebGLProgram} program
 * @param {!Array<number>} uniformIndices
 * @param {number} pname
 * @return {*}
 */
WebGL2RenderingContext.prototype.getActiveUniforms = function(
    program, uniformIndices, pname) {};

/**
 * @param {!WebGLProgram} program
 * @param {string} uniformBlockName
 * @return {number}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getUniformBlockIndex = function(
    program, uniformBlockName) {};

/**
 * @param {!WebGLProgram} program
 * @param {number} uniformBlockIndex
 * @param {number} pname
 * @return {*}
 */
WebGL2RenderingContext.prototype.getActiveUniformBlockParameter = function(
    program, uniformBlockIndex, pname) {};

/**
 * @param {!WebGLProgram} program
 * @param {number} uniformBlockIndex
 * @return {?string}
 * @nosideeffects
 */
WebGL2RenderingContext.prototype.getActiveUniformBlockName = function(
    program, uniformBlockIndex) {};

/**
 * @param {!WebGLProgram} program
 * @param {number} uniformBlockIndex
 * @param {number} uniformBlockBinding
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.uniformBlockBinding = function(
    program, uniformBlockIndex, uniformBlockBinding) {};

/* Vertex Array Objects */

/**
 * @return {?WebGLVertexArrayObject}
 */
WebGL2RenderingContext.prototype.createVertexArray = function() {};

/**
 * @param {?WebGLVertexArrayObject} vertexArray
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.deleteVertexArray = function(vertexArray) {};

/**
 * @param {?WebGLVertexArrayObject} vertexArray
 * @return {boolean}
 */
WebGL2RenderingContext.prototype.isVertexArray = function(vertexArray) {};

/**
 * @param {?WebGLVertexArrayObject} array
 * @return {undefined}
 */
WebGL2RenderingContext.prototype.bindVertexArray = function(array) {};


/**
 * @constructor
 * @extends {WebGLObject}
 */
function WebGLQuery() {}


/**
 * @constructor
 * @extends {WebGLObject}
 */
function WebGLSampler() {}


/**
 * @constructor
 * @extends {WebGLObject}
 */
function WebGLSync() {}


/**
 * @constructor
 * @extends {WebGLObject}
 */
function WebGLTransformFeedback() {}


/**
 * @constructor
 * @extends {WebGLObject}
 */
function WebGLVertexArrayObject() {}
