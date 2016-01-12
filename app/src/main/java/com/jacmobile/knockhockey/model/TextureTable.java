package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.TextureShaderProgram;
import static  com.jacmobile.knockhockey.model.Config.*;
import static android.opengl.GLES20.*;

public class TextureTable
{
    //Order of coordinates: X, Y, S, T
    public static final float[] VERTEX_DATA = {
            0f, 0f, .5f, .5f,
            -.5f, -.8f, 0f, .9f,
            .5f, -.8f, 1f, .9f,
            .5f, .8f, 1f, .1f,
            -.5f, .8f, 0f, .1f,
            -.5f, -.8f, 0f, .9f
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