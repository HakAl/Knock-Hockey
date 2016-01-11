package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.ColorShaderProgram;

import java.util.List;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.jacmobile.knockhockey.model.Config.COLOR_COMPONENT_COUNT;
import static com.jacmobile.knockhockey.model.Config.MALLET_STRIDE;

public class Mallet
{
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public static final float[] VERTEX_DATA = {
            0f, -.4f, 0f, 0f, 1,
            0f, .4f, 1f, 0f, 0f
    };

    public Mallet(float radius, float height, int numPoints)
    {
        this.radius = radius;
        this.height = height;
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