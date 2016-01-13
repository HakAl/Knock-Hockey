package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.TextureShaderProgram;
import static  com.jacmobile.knockhockey.model.Config.*;
import static android.opengl.GLES20.*;

public class TextureTable
{
    public static final float LEFT_BOUND = -.5f;
    public static final float RIGHT_BOUND = .5f;
    public static final float FAR_BOUND = -.8f;
    public static final float NEAR_BOUND = .8f;

    //Order of coordinates: X, Y, S, T
    public static final float[] VERTEX_DATA = {
            0f, 0f, .5f, .5f,
            LEFT_BOUND, FAR_BOUND, 0f, .9f,
            RIGHT_BOUND, FAR_BOUND, 1f, .9f,
            RIGHT_BOUND, NEAR_BOUND, 1f, .1f,
            LEFT_BOUND, NEAR_BOUND, 0f, .1f,
            LEFT_BOUND, FAR_BOUND, 0f, .9f
    };

    public int texture;
    private final VertexArray vertexArray;

    public TextureTable(int texture)
    {
        this.texture = texture;
        this.vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bind(TextureShaderProgram textureShaderProgram)
    {
        vertexArray.setVertexAttributePointer(
                0,
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                TABLE_STRIDE);
        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                textureShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                TABLE_STRIDE);
    }

    public void draw()
    {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}