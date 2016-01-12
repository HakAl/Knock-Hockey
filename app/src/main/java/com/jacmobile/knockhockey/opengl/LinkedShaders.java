package com.jacmobile.knockhockey.opengl;

public abstract class LinkedShaders
{
    public int program;
    public String vertexShader;
    public String fragmentShader;

    public LinkedShaders(String vertexShader, String fragmentShader)
    {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public int buildProgram()
    {
        this.program = GLUtils.linkProgram(
                GLUtils.compileVertexShader(vertexShader),
                GLUtils.compileFragmentShader(fragmentShader));
        return program;
    }
}