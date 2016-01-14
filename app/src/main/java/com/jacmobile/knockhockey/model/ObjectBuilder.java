package com.jacmobile.knockhockey.model;

import com.jacmobile.knockhockey.opengl.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

import static com.jacmobile.knockhockey.opengl.Geometry.*;
        

public class ObjectBuilder
{
    private static final int FLOATS_PER_VERTEX = 3;

    public interface DrawCommand
    {
        void draw();
    }

    static class GeneratedData
    {
        final float[] vertexData;
        final List<DrawCommand> drawList;

        public GeneratedData(float[] vertexData, List<DrawCommand> drawList)
        {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private int offset = 0;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<>();

    public ObjectBuilder(int numberOfVertices)
    {
        this.vertexData = new float[numberOfVertices * FLOATS_PER_VERTEX];
    }

    private GeneratedData build()
    {
        return new GeneratedData(vertexData, drawList);
    }

    static GeneratedData createPuck(Cylinder puck, int numPoints)
    {
        int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        Circle puckTop = new Circle(puck.center.translateY(puck.height/2f), puck.radius);
        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
    }

    static GeneratedData createMallet(Point center, float radius, float height, int numPoints)
    {
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        float baseHeight = height * .25f;
        float handleRadius = radius / 3f;
        float handleHeight = height - baseHeight;

        Circle baseCircle = new Circle(center.translateY(-baseHeight), radius);
        Cylinder baseCylinder = new Cylinder(baseCircle.center.translateY(-baseHeight / 2f),
                radius, baseHeight);
        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        Circle handleCircle = new Circle(center.translateY(height / 2f), handleRadius);
        Cylinder handleCylinder = new Cylinder(handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius, handleHeight);
        builder.appendCircle(handleCircle, numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    static GeneratedData createKnockHockeyStick(Point center,
                                                float stickRadius, float length, int numPoints)
    {
        int size =
              + sizeOfOpenCylinderInVertices(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        float stickLength = length;
        float baseLength = length * .33f;
        float baseRadius = stickRadius * 2f;

        Circle baseCircle = new Circle(center.translateY(-stickLength), baseRadius);
        Cylinder baseCylinder = new Cylinder(baseCircle.center.translateY(-stickLength / 2f),
                stickRadius, stickLength);
        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        Circle handleCircle = new Circle(center.translateY(length / 2f), stickRadius);
        Cylinder handleCylinder = new Cylinder(handleCircle.center.translateY(-stickLength / 2f),
                handleRadius, handleHeight);
        builder.appendCircle(handleCircle, numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        // Center point of fan
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        // Fan around center point. <= is used because we want to generate
        // the point at the starting angle twice to complete the fan.
        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians =
                    ((float) i / (float) numPoints)
                            * ((float) Math.PI * 2f);

            vertexData[offset++] =
                    circle.center.x
                            + circle.radius * (float) Math.cos(angleInRadians);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] =
                    circle.center.z
                            + circle.radius * (float) Math.sin(angleInRadians);
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    private void appendOpenCylinder(Cylinder cylinder, final int numPoints)
    {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = getAngleInRadians(numPoints, i);
            float xPosition = cylinder.center.x + cylinder.radius
                    * (float) Math.cos(angleInRadians);
            float zPosition = cylinder.center.z + cylinder.radius
                    * (float) Math.sin(angleInRadians);
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override public void draw()
            {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    private float getAngleInRadians(float numPoints, float i)
    {
        return (i / numPoints) * ((float) Math.PI * 2f);
    }

    private static int sizeOfCircleInVertices(int numPoints)
    {
        return numPoints + 2;
    }

    private static int sizeOfOpenCylinderInVertices(int numPoints)
    {
        return (numPoints + 1) * 2;
    }
}