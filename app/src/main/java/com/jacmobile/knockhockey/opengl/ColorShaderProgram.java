package com.jacmobile.knockhockey.opengl;

import static android.opengl.GLES20.*;

public class ColorShaderProgram extends ShaderProgram
{
    private int uMatrixLocation;
    private int aPositionLocation;
    private int aColorLocation;

    public ColorShaderProgram(LinkedShaders linkedShaders)
    {
        super(linkedShaders);
        this.uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        this.aPositionLocation = glGetAttribLocation(program, A_POSITION);
        this.aColorLocation = glGetAttribLocation(program, A_COLOR);
    }

    @Override public void onFrame()
    {
        buildProgram();
    }

    public void setUniformas(float[] matrix)
    {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionLocation()
    {
        return aPositionLocation;
    }

    public int getColorLocation()
    {
        return aColorLocation;
    }
}