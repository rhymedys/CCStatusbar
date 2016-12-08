package com.radius.rhymedys.cc.statusbarlibrary.flavor;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 适配
 * Created by Rhymedys on 2016/12/7.
 * E-Mail:rhymedys@gmail.com
 */

public class StatusBarCompatFlavorRom {
    interface ILightStatusBar {
        void setLightStatusBar(Window window, int color, boolean lightStatusBar);
    }

    static final ILightStatusBar IMPL;

    static {
        if (MIUILightStatusBarImpl.isMe()) {
            IMPL = new MIUILightStatusBarImpl();
        } else if (MeizuLightStatusBarImpl.isMe()) {
            IMPL = new MeizuLightStatusBarImpl();
        } else {
            IMPL = new ILightStatusBar() {
                @Override
                public void setLightStatusBar(Window window, int color, boolean lightStatusBar) {
                    if (lightStatusBar){
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
                }
            };
        }
    }

    public static void setLightStatusBar(Window window, int color, boolean lightStatusBar) {
        IMPL.setLightStatusBar(window, color,lightStatusBar);
    }

    static class MIUILightStatusBarImpl implements ILightStatusBar {
        static boolean isMe() {
            return "Xiaomi".equals(Build.MANUFACTURER);

        }

        public void setLightStatusBar(Window window, int color, boolean lightStatusBar) {
            Class<? extends Window> clazz = window.getClass();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(color);
                }
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, lightStatusBar ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class MeizuLightStatusBarImpl implements ILightStatusBar {
        static boolean isMe() {
            return Build.BRAND.contains("Meizu");
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void setLightStatusBar(Window window, int color, boolean lightStatusBar) {
            WindowManager.LayoutParams params = window.getAttributes();
            try {
                window.setStatusBarColor(color);
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(params);
                if (lightStatusBar) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(params, value);
                window.setAttributes(params);
                darkFlag.setAccessible(false);
                meizuFlags.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
