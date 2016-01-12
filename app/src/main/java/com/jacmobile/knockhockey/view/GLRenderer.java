package com.jacmobile.knockhockey.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jacmobile.knockhockey.R;
import com.jacmobile.knockhockey.model.Mallet;
import com.jacmobile.knockhockey.model.Puck;
import com.jacmobile.knockhockey.model.TextureTable;
import com.jacmobile.knockhockey.opengl.ColorShaderProgram;
import com.jacmobile.knockhockey.opengl.GLUtils;
import com.jacmobile.knockhockey.opengl.LinkedShaders;
import com.jacmobile.knockhockey.opengl.TextureShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.*;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.*;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private Context context;

    private Puck puck;
    private Mallet mallet;
    private TextureTable textureTable;

    private LinkedShaders linkedShaders;
    private ColorShaderProgram colorShaderProgram;
    private TextureShaderProgram textureShaderProgram;

    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

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

        this.textureTable = new TextureTable(
                GLUtils.loadTexture(context, R.drawable.air_hockey_surface));

        this.mallet = new Mallet(.8f, .15f, 32);
        this.puck = new Puck(.06f, .02f, 32);

        this.colorShaderProgram = new ColorShaderProgram(linkedShaders);
        this.textureShaderProgram = new TextureShaderProgram(linkedShaders);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);

        perspectiveM(projectionMatrix, 0, 60, (float) width / (float) height, 1, 10);
        setLookAtM(viewMatrix, 0, 0, 1.2f, 2.2f, 0, 0, 0, 0, 1, 0);
//        translateM(modelMatrix, 0, 0, 0, -2.5f);
//        rotateM(modelMatrix, 0, -50, 1, 0, 0);
//        final float[] temp = new float[16];
//        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override public void onDrawFrame(GL10 gl)
    {
        glClear(GL_COLOR_BUFFER_BIT);

        multiplyMM(viewMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        positionTable();
        textureShaderProgram.onDrawFrame();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix, textureTable.texture);
        textureTable.bind(textureShaderProgram);
        textureTable.draw();

//        colorShaderProgram.onDrawFrame();
//        colorShaderProgram.setUniforms(projectionMatrix);
//
//        mallet.bindAndDraw(colorShaderProgram);
    }

    private void positionTable()
    {
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90, 1, 0, 0);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }
}