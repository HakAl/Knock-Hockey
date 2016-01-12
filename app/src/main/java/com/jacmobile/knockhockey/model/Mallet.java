package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.ColorShaderProgram;
import com.jacmobile.knockhockey.opengl.Geometry;

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

    public Mallet(float radius, float height, int numPoints)
    {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0, 0, 0), radius, height, numPoints);
        this.radius = radius;
        this.height = height;
        this.vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bind(ColorShaderProgram shaderProgram)
    {
        vertexArray.setVertexAttributePointer(
                0,
                shaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw()
    {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}