package io;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import util.Function;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public static long winID;

    public Vector4f color;

    public Window(Vector4f color) {
        this.color = color;
    }

    public Window init() {

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        winID = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if (winID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(winID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(winID, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    winID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(winID);
        glfwSwapInterval(1);

        glfwShowWindow(winID);

        GL.createCapabilities();

        glClearColor(color.x, color.y, color.z, color.w);

        return this;
    }

    public void end() {
        glfwFreeCallbacks(winID);
        glfwDestroyWindow(winID);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public Window update(Function draw) {
        glfwMakeContextCurrent(winID);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        draw.run();
        glfwSwapBuffers(winID);

        glfwPollEvents();

        return this;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(winID);
    }
}