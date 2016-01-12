package com.jacmobile.knockhockey.opengl;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

public class ColorShaderProgram extends ShaderProgram
{
    private int uMatrixLocation;
    private int uColorLocation;
    private int aPositionLocation;

    public ColorShaderProgram(SimpleShaders linkedShaders)
    {
        super(linkedShaders);
        this.uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        this.uColorLocation = glGetUniformLocation(program, U_COLOR);
        this.aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    @Override public void onDrawFrame()
    {
        glUseProgram(program);
    }

    public void setUniforms(float[] matrix, float r, float g, float b)
    {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(uColorLocation, r, g, b, 1f);
    }

    public int getPositionLocation()
    {
        return aPositionLocation;
    }
}