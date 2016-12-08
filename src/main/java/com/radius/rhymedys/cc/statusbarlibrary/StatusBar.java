    package com.radius.rhymedys.cc.statusbarlibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.radius.rhymedys.cc.statusbarlibrary.flavor.StatusBarExclude;
import com.radius.rhymedys.cc.statusbarlibrary.imp.IStatusBar;
import com.radius.rhymedys.cc.statusbarlibrary.imp.StatusBarKitkatImpl;
import com.radius.rhymedys.cc.statusbarlibrary.imp.StatusBarLollipopImpl;
import com.radius.rhymedys.cc.statusbarlibrary.imp.StatusBarMImpl;
import com.radius.rhymedys.cc.statusbarlibrary.utils.ColorUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Rhymedys on 2016/12/7.
 * E-Mail:rhymedys@gmail.com
 */

public class StatusBar {

    static final IStatusBar IMPL;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMPL = new StatusBarMImpl();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !isEMUI()) {
            IMPL = new StatusBarLollipopImpl();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMPL = new StatusBarKitkatImpl();
        } else {
            IMPL = new IStatusBar() {
                @Override
                public void setStatusBarColor(Window window, int color, boolean lightStatusBar) {

                }
            };
        }
    }

    private static boolean isEMUI() {
        File file = new File(Environment.getRootDirectory(), "build.prop");
        if (file.exists()) {
            Properties properties = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                properties.load(fis);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return properties.containsKey("ro.build.hw_emui_api_level");
        }
        return false;
    }

    public static void setStatusBarColor(Activity activity, String color) {
        int intColor=0;
        ColorUtils colorUtils = ColorUtils.getInstance();
        int[] RGB = colorUtils.calclate2RGB(color);
        boolean isLightColor = colorUtils.isDeepRGB(RGB);
        try {
            intColor=Color.parseColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intColor!=0){
            setStatusBarColor(activity, intColor, isLightColor);
        }
    }

    /**
     * Set system status bar color.
     *
     * @param activity
     * @param color          status bar color
     * @param lightStatusBar if the status bar color is light. Only effective in some devices.
     */
    public static void setStatusBarColor(Activity activity, int color, boolean lightStatusBar) {
        setStatusBarColor(activity.getWindow(), color, lightStatusBar);
    }

    /**
     * Set the system status bar color
     * @param window the window
     * @param color status bar color
     * @param lightStatusBar if the status bar color is light. Only effective in some devices.
     */
    public static void setStatusBarColor(Window window, int color, boolean lightStatusBar) {
        if ((window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) > 0
                || StatusBarExclude.exclude) {
            return;
        }
        IMPL.setStatusBarColor(window, color, lightStatusBar);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void setFitsSystemWindows(Window window, boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                mChildView.setFitsSystemWindows(fitSystemWindows);
            }
        }
    }
}
