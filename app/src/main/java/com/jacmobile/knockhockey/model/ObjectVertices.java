package com.jacmobile.knockhockey.model;

/**
 * Vertices are defined in "winding order", which allows
 * for performance optimizations during computation.
 *
 * OpenGL maps the screen to the range [-1, 1] for both x and y.
 */
public class ObjectVertices
{
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int COLOR_COMPONENT_COUNT = 3;

    // order of coordinates: X, Y, R, G, B
    public static final float[] TABLE_VERTICES_TRIANGLES = {
        // Triangle fan -- requires 6 points for a rectangle
            0, 0, 1, 1, 1,
            -.5f, -.5f, .7f, .7f, .7f,
            .5f, -.5f, .7f, .7f, .7f,
            .5f, .5f, .7f, .7f, .7f,
            -.5f, .5f, .7f, .7f, .7f,
            -.5f, -.5f, .7f, .7f, .7f,
        // center divider
            -.5f, 0f, 1, 0, 0,
            .5f, 0f, 1, 0, 0,
        // first mallet
            0, -.25f, 0, 0, 1,
        // second mallet
            0, .25f, 1, 0, 0
    };

    //Used to allocate memory
    public static final int NUMBER_OF_VERTICES = TABLE_VERTICES_TRIANGLES.length;
}
