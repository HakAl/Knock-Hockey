package com.jacmobile.knockhockey.opengl;

import static android.opengl.GLES20.glUseProgram;

public abstract class ShaderProgram
{
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected int program;
    private final LinkedShaders linkedShaders;

    public ShaderProgram(LinkedShaders linkedShaders)
    {
        this.linkedShaders = linkedShaders;
        this.program = linkedShaders.buildProgram();
    }

    protected int buildProgram()
    {
        glUseProgram(program);
        return program;
    }

    /**
     * @return the program from buildProgram()
     */
    public abstract void onFrame();
}