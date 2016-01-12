package com.jacmobile.knockhockey.opengl;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

public class TextureShaderProgram extends ShaderProgram
{
    //Uniform locations
    private int uMatrixLocation;
    private int uTextureUnitLocation;
    //Attribute  locations
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    public TextureShaderProgram(LinkedShaders linkedShaders)
    {
        super(linkedShaders);
        this.uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        this.uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        this.aPositionLocation = glGetAttribLocation(program, A_POSITION);
        this.aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void onDrawFrame()
    {
        glUseProgram(program);
    }

    public void setUniforms(float[] matrix, int textureId)
    {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation()
    {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation()
    {
        return aTextureCoordinatesLocation;
    }
}
