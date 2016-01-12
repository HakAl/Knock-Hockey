package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.ColorShaderProgram;
import com.jacmobile.knockhockey.opengl.Geometry;

import java.util.List;

public class Puck
{
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPoints)
    {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0,0,0), radius, height), numPoints);
        this.radius = radius;
        this.height = height;
        this.vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void draw(ColorShaderProgram colorShaderProgram)
    {
        vertexArray.setVertexAttributePointer(
                0,
                colorShaderProgram.getPositionLocation(),
                POSITION_COMPONENT_COUNT,
                0);

        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}