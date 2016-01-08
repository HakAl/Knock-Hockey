package com.jacmobile.knockhockey.utils;

public class LinkedShaders
{
    public String vertexShader;
    public String fragmentShader;

    public LinkedShaders(String vertexShaderString, String fragmentShaderString)
    {
        this.vertexShader = vertexShaderString;
        this.fragmentShader = fragmentShaderString;
    }
}
