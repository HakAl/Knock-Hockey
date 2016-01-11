package com.jacmobile.knockhockey.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jacmobile.knockhockey.R;
import com.jacmobile.knockhockey.model.Mallet;
import com.jacmobile.knockhockey.model.TextureTable;
import com.jacmobile.knockhockey.opengl.ColorShaderProgram;
import com.jacmobile.knockhockey.opengl.GLUtils;
import com.jacmobile.knockhockey.opengl.LinkedShaders;
import com.jacmobile.knockhockey.opengl.TextureShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private Context context;

    private Mallet mallet;
    private TextureTable textureTable;
    private int texture;

    private LinkedShaders linkedShaders;
    private ColorShaderProgram colorShaderProgram;
    private TextureShaderProgram textureShaderProgram;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    /**
     * Allocate native memory for the vertices *that won't be GC'ed*.
     * After copying the data to memory, the data must be passed
     * to OpenGL through shaders. Shaders tell the GPU how to
     * process data.
     *
     * If code requires many ByteBuffers, refer to this:
     * http://en.wikipedia.org/wiki/Memory_pool
     */
    public GLRenderer(Context context, LinkedShaders linkedShaders)
    {
        this.context = context;
        this.linkedShaders = linkedShaders;
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        glClearColor(0,0,0,0);
        this.textureTable = new TextureTable();
        this.mallet = new Mallet();
        this.colorShaderProgram = new ColorShaderProgram(linkedShaders);
        this.textureShaderProgram = new TextureShaderProgram(linkedShaders);
        this.texture = GLUtils.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        perspectiveM(projectionMatrix, 0, 60, (float) width / (float) height, 1, 10);
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, 0, 0, -2.5f);
        rotateM(modelMatrix, 0, -50, 1, 0, 0);
        final float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override public void onDrawFrame(GL10 gl)
    {
        glClear(GL_COLOR_BUFFER_BIT);
        textureShaderProgram.onFrame();
        textureShaderProgram.setUniforms(projectionMatrix, texture);
        textureTable.bindAndDraw(textureShaderProgram);
        colorShaderProgram.onFrame();
        colorShaderProgram.setUniformas(projectionMatrix);
        mallet.bindAndDraw(colorShaderProgram);
    }
}