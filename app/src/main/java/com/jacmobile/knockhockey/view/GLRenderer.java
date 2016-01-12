package com.jacmobile.knockhockey.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.jacmobile.knockhockey.R;
import com.jacmobile.knockhockey.model.Mallet;
import com.jacmobile.knockhockey.model.Puck;
import com.jacmobile.knockhockey.model.TextureTable;
import com.jacmobile.knockhockey.opengl.ColorShaderProgram;
import com.jacmobile.knockhockey.opengl.GLUtils;
import com.jacmobile.knockhockey.opengl.Geometry;
import com.jacmobile.knockhockey.opengl.LinkedShaders;
import com.jacmobile.knockhockey.opengl.SimpleShaders;
import com.jacmobile.knockhockey.opengl.TextureShaderProgram;
import com.jacmobile.knockhockey.opengl.TextureShaders;

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

public class GLRenderer implements GLSurfaceView.Renderer, TouchHandler
{
    private Context context;

    private Puck puck;
    private Mallet mallet;
    private TextureTable textureTable;

    private boolean malletPressed = false;
    private Geometry.Point playerMalletPosition;

    private SimpleShaders simpleShaders;
    private TextureShaders textureShaders;
    private ColorShaderProgram colorShaderProgram;
    private TextureShaderProgram textureShaderProgram;

    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];

    /**
     * Allocate native memory for the vertices *that won't be GC'ed*.
     * After copying the data to memory, the data must be passed
     * to OpenGL through shaders. Shaders tell the GPU how to
     * process data.
     *
     * If code requires many ByteBuffers, refer to this:
     * http://en.wikipedia.org/wiki/Memory_pool
     */
    public GLRenderer(Context context, SimpleShaders simpleShaders, TextureShaders textureShaders)
    {
        this.context = context;
        this.simpleShaders = simpleShaders;
        this.textureShaders = textureShaders;
    }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        glClearColor(0,0,0,0);

        this.textureTable = new TextureTable(
                GLUtils.loadTexture(context, R.drawable.air_hockey_surface));

        this.mallet = new Mallet(.08f, .15f, 32);
        this.puck = new Puck(.06f, .02f, 32);

        this.colorShaderProgram = new ColorShaderProgram(simpleShaders);
        this.textureShaderProgram = new TextureShaderProgram(textureShaders);

        this.playerMalletPosition = new Geometry.Point(0, mallet.height / 2f, .4f);
    }

    @Override public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);

        perspectiveM(projectionMatrix, 0, 60, (float) width / (float) height, 1, 10);
        setLookAtM(viewMatrix, 0, 0, 1.2f, 2.2f, 0, 0, 0, 0, 1, 0);
    }

    @Override public void onDrawFrame(GL10 gl)
    {
        glClear(GL_COLOR_BUFFER_BIT);

        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);
        positionTable();
        textureShaderProgram.onDrawFrame();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix, textureTable.texture);
        textureTable.bind(textureShaderProgram);
        textureTable.draw();

        positionObjectInScene(0, mallet.height / 2f, -.4f);
        colorShaderProgram.onDrawFrame();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 1, 0, 0);
        mallet.bind(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(0, mallet.height / 2f, .4f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0, 0, 1);
        mallet.draw();

        positionObjectInScene(0, puck.height / 2f, 0);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, .1f, .1f, .1f);
        puck.draw(colorShaderProgram);
    }

    @Override public void handleTouch(float normalizedX, float normalizedY)
    {
        Geometry.Ray ray;
    }

    @Override public void handleDrag(float normalizedX, float normalizedY)
    {

    }

    private void positionTable()
    {
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90, 1, 0, 0);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z)
    {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    /**
     * First multiply by the inverse matrix, then undo the perspective divide.
     * @param x normalized x
     * @param y normalized y
     * @return a Ray representing the touch point
     */
    private Geometry.Ray convertNormalizedPointToRay(float x, float y)
    {
        final float[] nearPoint = {x, y, -1, 1};
        final float[] farPoint = {x, y, 1, 1};
        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPoint, 0);
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPoint, 0);
        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);

        return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay,
                new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])));
    }

    private static void divideByW(float[] vector)
    {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }
}