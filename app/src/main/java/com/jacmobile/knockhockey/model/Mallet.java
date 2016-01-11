package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.ColorShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.jacmobile.knockhockey.model.Config.COLOR_COMPONENT_COUNT;
import static com.jacmobile.knockhockey.model.Config.MALLET_STRIDE;
import static com.jacmobile.knockhockey.model.Config.POSITION_COMPONENT_COUNT;

public class Mallet
{
    public static final float[] VERTEX_DATA = {
            0f, -.4f, 0f, 0f, 1,
            0f, .4f, 1f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public Mallet()
    {
        this.vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindAndDraw(ColorShaderProgram shaderProgram)
    {
        vertexArray.setVertexAttributePointer(
                0,
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                MALLET_STRIDE);
        vertexArray.setVertexAttributePointer(
                POSITION_COMPONENT_COUNT,
                shaderProgram.getColorLocation(),
                COLOR_COMPONENT_COUNT,
                MALLET_STRIDE);
        glDrawArrays(GL_POINTS, 0, 2);
    }
}