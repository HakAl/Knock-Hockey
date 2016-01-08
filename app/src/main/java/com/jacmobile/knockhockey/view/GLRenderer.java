package com.jacmobile.knockhockey.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jacmobile.knockhockey.BuildConfig;
import com.jacmobile.knockhockey.R;
import com.jacmobile.knockhockey.utils.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.jacmobile.knockhockey.model.ObjectVertices.COLOR_COMPONENT_COUNT;
import static com.jacmobile.knockhockey.model.ObjectVertices.NUMBER_OF_VERTICES;
import static com.jacmobile.knockhockey.model.ObjectVertices.POSITION_COMPONENT_COUNT;
import static com.jacmobile.knockhockey.model.ObjectVertices.TABLE_VERTICES_TRIANGLES;
import static com.jacmobile.knockhockey.utils.GLUtils.BYTES_PER_FLOAT;
import static com.jacmobile.knockhockey.utils.GLUtils.compileFragmentShader;
import static com.jacmobile.knockhockey.utils.GLUtils.compileVertexShader;
import static com.jacmobile.knockhockey.utils.GLUtils.linkProgram;
import static com.jacmobile.knockhockey.utils.GLUtils.readTextFileFromResource;
import static com.jacmobile.knockhockey.utils.GLUtils.validateProgram;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private final Context context;
    private final FloatBuffer vertexData;
    private int program;
    //attribute defined in vertex shader
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    //varying color defined in vertex / fragment shaders
    private static final String A_COLOR = "a_Color";
    private int aColorLocation;
    private static final String U_MATRIX = "u_Matrix";
    
    public static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;


    /**
     * Allocate memory for the vertices.
     * After copying the data to memory, the data must be passed
     * to OpenGL through shaders. Shaders tell the GPU how to
     * process data.
     *
     * If code requires many ByteBuffers, refer to this:
     * http://en.wikipedia.org/wiki/Memory_pool
     */
    public GLRenderer(Context context)
    {
        this.context = context.getApplicationContext();

        //this allocates native memory that won't be garbage collected
        this.vertexData = ByteBuffer
                .allocateDirect(NUMBER_OF_VERTICES * GLUtils.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        //copy from Java to native memory
        vertexData.put(TABLE_VERTICES_TRIANGLES);
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        glClearColor(0,0,0,0);

        //link shaders
        String vertexShaderString = readTextFileFromResource(context, R.raw.vertex_shader);
        String fragmentShaderString = readTextFileFromResource(context, R.raw.fragment_shader);
        int vertexShader = compileVertexShader(vertexShaderString);
        int fragmentShader = compileFragmentShader(fragmentShaderString);
        program = linkProgram(vertexShader, fragmentShader);
        if (BuildConfig.DEBUG) validateProgram(program);
        glUseProgram(program);

        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
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
    }

    @Override public void onDrawFrame(GL10 gl)
    {
        glClear(GL_COLOR_BUFFER_BIT);
//        //Table
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
//        //Dividing line
        glDrawArrays(GL_LINES, 6, 2);
//        //Mallets
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
    }
}
