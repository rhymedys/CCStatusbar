package com.radius.rhymedys.cc.statusbarlibrary.flavor;

import android.os.Build;

/**
 * Created by Rhymedys on 2016/12/6.
 * E-Mail:rhymedys@gmail.com
 */

public class StatusBarExclude {
   public static boolean exclude = false;


    public static void excludeIncompatibleFlyMe() {
        try {
            Build.class.getMethod("hasSmartBar");
        } catch (NoSuchMethodException e) {
            exclude |= Build.BRAND.contains("Meizu");
        }}
}
