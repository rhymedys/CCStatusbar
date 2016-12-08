package com.radius.rhymedys.cc.statusbarlibrary.imp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.radius.rhymedys.cc.statusbarlibrary.StatusBar;
import com.radius.rhymedys.cc.statusbarlibrary.flavor.StatusBarCompatFlavorRom;

/**
 * Kitkat
 * Created by Rhymedys on 2016/12/6.
 * E-Mail:rhymedys@gmail.com
 */

public class StatusBarKitkatImpl  implements IStatusBar{
    private static final String STATUS_BAR_VIEW_TAG = "ghStatusBarView";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setStatusBarColor(Window window, int color, boolean lightStatusBar) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = decorViewGroup.findViewWithTag(STATUS_BAR_VIEW_TAG);
        if (statusBarView == null) {
            statusBarView = new View(window.getContext());
            statusBarView.setTag(STATUS_BAR_VIEW_TAG);
            int statusBarHeight = getStatusBarHeight(window.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            decorViewGroup.addView(statusBarView);
        }
        StatusBar.setFitsSystemWindows(window, true);
        StatusBarCompatFlavorRom.setLightStatusBar(window, color, lightStatusBar);
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
