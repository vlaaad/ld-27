package com.vlaaad.tenseconds.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.vlaaad.tenseconds.TenSeconds;

/**
 * Created 24.08.13 by vlaaad
 */
public class DesktopLauncher {

    public static void main(String[] args) {
        new LwjglApplication(new TenSeconds(), "Ten seconds", 180, 320, true);
    }


}
