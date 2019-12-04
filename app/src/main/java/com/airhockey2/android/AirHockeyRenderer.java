/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.airhockey2.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockey2.android.util.LoggerConfig;
import com.airhockey2.android.util.ShaderHelper;
import com.airhockey2.android.util.TextResourceReader;
import com.xiaojun.opengl.R;

public class AirHockeyRenderer implements Renderer {
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";    
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;    
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = 
        (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    
    private final FloatBuffer vertexData;
    private final Context context;

    private int program;
    private int aPositionLocation;
    private int aColorLocation;

    public AirHockeyRenderer(Context context) {
        this.context = context;
        /*
        float[] tableVerticesWithTriangles = {
            // Triangle Fan
               0,     0,            
            -0.5f, -0.5f,             
             0.5f, -0.5f,
             0.5f,  0.5f,
            -0.5f,  0.5f,            
            -0.5f, -0.5f,

            // Line 1
            -0.5f, 0f, 
             0.5f, 0f,

            // Mallets
            0f, -0.25f,
            0f,  0.25f 
        };*/
                
        //
        // Vertex data is stored in the following manner:
        // 
        // The first two numbers are part of the position: X, Y
        // The next three numbers are part of the color: R, G, B
        //
        float[] tableVerticesWithTriangles = {   
            // Order of coordinates: X, Y, R, G, B
            
            // Triangle Fan
               0f,    0f,   1f,   1f,   1f,         
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,            
             0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
             0.5f,  0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f,  0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
             0.5f, 0f, 1f, 0f, 0f,

            // Mallets
            0f, -0.25f, 0f, 0f, 1f,
            0f,  0.25f, 1f, 0f, 0f
        };

        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_vertex_shader2);
        String fragmentShaderSource = TextResourceReader
            .readTextFileFromResource(context, R.raw.simple_fragment_shader2);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper
            .compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);
        
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexData);

        GLES20.glEnableVertexAttribArray(aPositionLocation);
        
        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_COLOR_LOCATION.
        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexData);

        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     * 
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Draw the table.        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        // Draw the center dividing line.        
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        // Draw the first mallet.        
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        // Draw the second mallet.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}