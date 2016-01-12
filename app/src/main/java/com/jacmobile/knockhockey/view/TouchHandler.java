package com.jacmobile.knockhockey.view;
// TouchHandler
// com.jacmobile.knockhockey.view
//
// GiftCards Android App
//
// Created by acorll on 1/12/16.
// Copyright (c) 2007-2015 GiftCards.com.  All rights reserved.

public interface TouchHandler
{
    void handleTouch(float normalizedX, float normalizedY);
    void handleDrag(float normalizedX, float normalizedY);
}
