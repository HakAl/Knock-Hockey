package com.jacmobile.knockhockey.utils;

import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Resources;
import android.os.Build;

import com.jacmobile.knockhockey.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

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
    public static boolean supportsGLEs2(ConfigurationInfo configurationInfo)
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
     * A float in Java has 32 bits of precision, while a byte has 8.
     */
    public static final int BYTES_PER_FLOAT = 4;

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
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Logger.log("Results of validating program: "+validateStatus[0]
                    + "\nGL info log: "+glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}
