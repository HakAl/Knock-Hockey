package com.jacmobile.knockhockey.view;

import android.opengl.GLSurfaceView;

import com.jacmobile.knockhockey.BuildConfig;
import com.jacmobile.knockhockey.utils.GLUtils;
import com.jacmobile.knockhockey.utils.LinkedShaders;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.*;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.orthoM;
import static com.jacmobile.knockhockey.model.ObjectVertices.COLOR_COMPONENT_COUNT;
import static com.jacmobile.knockhockey.model.ObjectVertices.NUMBER_OF_VERTICES;
import static com.jacmobile.knockhockey.model.ObjectVertices.POSITION_COMPONENT_COUNT;
import static com.jacmobile.knockhockey.model.ObjectVertices.STRIDE;
import static com.jacmobile.knockhockey.model.ObjectVertices.*;
import static com.jacmobile.knockhockey.utils.GLUtils.compileFragmentShader;
import static com.jacmobile.knockhockey.utils.GLUtils.compileVertexShader;
import static com.jacmobile.knockhockey.utils.GLUtils.linkProgram;
import static com.jacmobile.knockhockey.utils.GLUtils.validateProgram;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private int program;
    private final FloatBuffer vertexData;
    private final LinkedShaders linkedShaders;

    private int aPositionLocation;
    private static final String A_POSITION = "a_Position";
    private int aColorLocation;
    private static final String A_COLOR = "a_Color";
    private int uMatrixLocation;
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];

    /**
     * Allocate native memory for the vertices *that won't be GC'ed*.
     * After copying the data to memory, the data must be passed
     * to OpenGL through shaders. Shaders tell the GPU how to
     * process data.
     *
     * If code requires many ByteBuffers, refer to this:
     * http://en.wikipedia.org/wiki/Memory_pool
     */
    public GLRenderer(LinkedShaders linkedShaders)
    {
        this.linkedShaders = linkedShaders;
        this.vertexData = ByteBuffer
                .allocateDirect(NUMBER_OF_VERTICES * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        //copy from Java to native memory
        vertexData.put(TABLE_VERTICES_TRIANGLES);
        vertexData.put(CENTER_DIVIDER_LINE);
        vertexData.put(MALLET_A);
        vertexData.put(MALLET_B);
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        glClearColor(0,0,0,0);

        program = linkProgram(compileVertexShader(linkedShaders.vertexShader),
                compileFragmentShader(linkedShaders.fragmentShader));
        validateProgram(program);
        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        vertexData.position(0);

        //tell GL where to read data for the attribute a_Position
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        //associate vertex data with the shader
        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);

    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0,0,width,height);

        final float aspectRatio = GLUtils.getAspectRatio(width, height);
        if (width > height) {
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, -1, 1);
        } else {
            orthoM(projectionMatrix, 0, -1, 1, -aspectRatio, aspectRatio, -1, 1);
        }
    }

    @Override public void onDrawFrame(GL10 gl)
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        // table rect
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        // center divider
        glDrawArrays(GL_LINES, 6, 2);
        // mallets
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
