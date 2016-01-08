package com.jacmobile.knockhockey.model;

/**
 * Vertices are defined in "winding order", which allows
 * for performance optimizations during computation.
 *
 * OpenGL maps the screen to the range [-1, 1] for both x and y.
 */
public class ObjectVertices
{
    // order of coordinates: X, Y, R, G, B
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int COLOR_COMPONENT_COUNT = 3;

    // A float in Java has 32 bits of precision, while a byte has 8.
    public static final int BYTES_PER_FLOAT = 4;
    public static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    // Triangle fan -- requires 6 points for a rectangle
    public static final float[] TABLE_VERTICES_TRIANGLES = {
            0, 0, 1, 1, 1,
            -.5f, -.8f, .7f, .7f, .7f,
            .5f, -.8f, .7f, .7f, .7f,
            .5f, .8f, .7f, .7f, .7f,
            -.5f, .8f, .7f, .7f, .7f,
            -.5f, -.8f, .7f, .7f, .7f
    };

    // center divider in red
    public static final float[] CENTER_DIVIDER_LINE = {
            -.5f, 0f, 1, 0, 0,
            .5f, 0f, 1, 0, 0,
    };

    public static final float[] MALLET_A = {
            0, -.4f, 0, 0, 1,
    };

    public static final float[] MALLET_B = {
            0, .4f, 1, 0, 0
    };

    //Used to allocate memory
    public static final int NUMBER_OF_VERTICES = TABLE_VERTICES_TRIANGLES.length
            + CENTER_DIVIDER_LINE.length + MALLET_A.length + MALLET_B.length;
}
