package com.jacmobile.knockhockey.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.jacmobile.knockhockey.model.Config.*;
import static android.opengl.GLES20.*;

/**
 * Allocates Java float[] data to a FloatBuffer for native use.
 */
public class VertexArray
{
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertexData)
    {
        this.floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    /**
     * Associate a shader attribute with the buffer
     *
     * @param dataOffset
     * @param attributeLocation
     * @param componentCount
     * @param stride
     */
    public void setVertexAttributePointer(int dataOffset, int attributeLocation, int componentCount, int stride)
    {
        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }
}