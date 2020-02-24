package lightningstike.engine.managers;

import lightningstike.engine.io.Window;
import lightningstike.engine.util.Function;

import java.util.ArrayList;

public class WindowManager {
    private static ArrayList<Window> windows = new ArrayList<>();
    private static ArrayList<Function> renderers = new ArrayList<>();

    public static void add(Window win, Function render) {
        windows.add(win);
        InputManager.init(win.winID);
        renderers.add(render);
    }

    public static void update() {
        for(int i = 0; i < windows.size(); i++) {
            windows.get(i).update(renderers.get(i));
        }
    }

    public static boolean shouldClose() {
        boolean flag = true;
        for(Window win:windows) {
            if (!win.shouldClose()) {
                flag = false;
            }
        }
        return flag;
    }

    public static ArrayList<Window> getWindows() {
        return windows;
    }
}