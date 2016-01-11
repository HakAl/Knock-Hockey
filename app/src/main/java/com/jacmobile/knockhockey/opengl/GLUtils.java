package com.jacmobile.knockhockey.opengl;

import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.jacmobile.knockhockey.BuildConfig;
import com.jacmobile.knockhockey.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glValidateProgram;
import static android.opengl.GLUtils.texImage2D;

public class GLUtils
{
    public static final String TAG = GLUtils.class.getSimpleName();

    private static final int GL_VERSION_2 = 0x20000;

    /**
     * configurationInfo.reqGlEsVersion >= GL_VERSION_2 is fine for all
     * devices. Extra logic supports the emulator.
     *
     * @param configurationInfo ActivityManager.getConfigInfo
     * @return true if GLES is supported
     */
    public static boolean supportsGLES2(ConfigurationInfo configurationInfo)
    {
        return !BuildConfig.DEBUG ? configurationInfo.reqGlEsVersion >= GL_VERSION_2
                : configurationInfo.reqGlEsVersion >= GL_VERSION_2 ||
                 (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));
    }

    /**
     * Used to read in .glsl files from res/raw
     *
     * @param context   a Context that can getResources()
     * @param resId     shader resId
     * @return          a String representation of the file
     */
    public static String readTextFileFromResource(Context context, int resId)
    {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                sb.append(nextLine);
                sb.append('\n');
            }
        } catch (IOException | Resources.NotFoundException e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        }

        return sb.toString();
    }

    public static int compileVertexShader(String shader)
    {
        return compileShader(GL_VERTEX_SHADER, shader);
    }

    public static int compileFragmentShader(String shader)
    {
        return compileShader(GL_FRAGMENT_SHADER, shader);
    }

    public static int compileShader(int type, String shader)
    {
        final int shaderObjectId = glCreateShader(type);

        if (shaderObjectId == 0) {
            Logger.log("Unable to initialize shader.");

            return 0;
        } else {
            glShaderSource(shaderObjectId, shader);
            glCompileShader(shaderObjectId);

            final int[] compileStatus = new int[1];
            glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
            Logger.log("Results of compiled .glsl source:\n" + shader + "\n:" + glGetShaderInfoLog(shaderObjectId));

            if (compileStatus[0] == 0) {
                //compilation failed, delete shader object
                glDeleteShader(shaderObjectId);
                Logger.log("Unable to compile shader.");

                return 0;
            }
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId)
    {
        final int programObjectId = glCreateProgram();

        if (programObjectId == 0) {
            Logger.log("Unable to create .glsl program.");

            return 0;
        }

        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Logger.log("Result of linking the program:\n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            //linking failed, delete program
            glDeleteProgram(programObjectId);
            Logger.log("Linking program failed");
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId)
    {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];

        if (BuildConfig.DEBUG) {
            glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
            Logger.log("Results of validating program: " + validateStatus[0]
                    + "\nGL info log: " + glGetProgramInfoLog(programObjectId));
        }

        return validateStatus[0] != 0;
    }

    public static int loadTexture(Context context, int resId)
    {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Logger.log("Error creating a texture.");
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//no scaling

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            Logger.log("Error generating Bitmap from resource.");
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static final float getAspectRatio(int width, int height)
    {
        return width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
    }

    //    https://code.google.com/p/android/issues/detail?id=35646
    public static void perspectiveM(float[] m, float yFovDegrees, float aspectRatio, float n, float f)
    {
        final float angleInRadians = (float) (yFovDegrees * Math.PI / 180);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        m[0] = a / aspectRatio;
        m[1] = 0;
        m[2] = 0;
        m[3] = 0;

        m[4] = 0;
        m[5] = a;
        m[6] = 0;
        m[7] = 0;

        m[8] = 0;
        m[9] = 0;
        m[10] = -((f + n) / (f - n));
        m[11] = -1;

        m[12] = 0;
        m[13] = a;
        m[14] = -((2 * f * n) / (f - n));
        m[15] = 0;
    }
}
