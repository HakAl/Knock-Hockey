package com.jacmobile.knockhockey.view;

public interface TouchHandler
{
    void handleTouch(float normalizedX, float normalizedY);
    void handleDrag(float normalizedX, float normalizedY);
}
