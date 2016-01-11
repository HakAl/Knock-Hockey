package com.jacmobile.knockhockey.model;

public class Config
{
    // A float in Java has 32 bits of precision, while a byte has 8.
    public static final int BYTES_PER_FLOAT = 4;

    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int COLOR_COMPONENT_COUNT = 2;
    public static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    public static final int TABLE_STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    public static final int MALLET_STRIDE = (POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
}