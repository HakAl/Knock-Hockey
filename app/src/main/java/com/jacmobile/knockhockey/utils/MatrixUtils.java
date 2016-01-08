package com.jacmobile.knockhockey.utils;

public class MatrixUtils
{
    public static final float[][] ERROR_MATRIX = new float[0][0];
    public static final float[][] IDENTITY_MATRIX = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };

    public static float[][] translateMatrix(float[][] vector, float translateX, float translateY, float translateZ)
    {
        if (vector == null || vector[0].length != 4) return ERROR_MATRIX;

        float[][] translationMatrix = {
                {1, 0, 0, translateX},
                {0, 1, 0, translateY},
                {0, 0, 1, translateZ},
                {0, 0, 0, 1}
        };
        float[][] result = new float[vector.length][translationMatrix[0].length];

        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < translationMatrix[0].length; j++) {
                for (int k = 0; k < vector[0].length; k++)
                result[i][j] += vector[i][k] * translationMatrix[k][j];
            }
        }

        return translationMatrix;
    }
}
