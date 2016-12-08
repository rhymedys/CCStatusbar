package com.radius.rhymedys.cc.statusbarlibrary.utils;

import android.graphics.Color;

/**
 * Created by Rhymedys on 2016/12/6.
 * E-Mail:rhymedys@gmail.com
 */

public class ColorUtils {

    private static ColorUtils colorUtils = null;

    public static ColorUtils getInstance() {
        if (null == colorUtils) {
            synchronized (ColorUtils.class) {
                colorUtils = new ColorUtils();
            }
        }
        return colorUtils;
    }


    /**
     * 16进制转换RGB
     *
     * @param color
     * @return
     */
    public int[] calclate2RGB(String color) {
        if (null != color) {
            int mColor = 0;
            try {
                mColor = Color.parseColor(color);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mColor != 0) {
                int R = mColor >> 16 & 0xff;
                int G = mColor >> 8 & 0xff;
                int B = mColor & 0xff;
                return new int[]{R, G, B};
            }
        }

        return null;
    }


    public Color calculate2Color(int[] rgbColor) {
        if (null != rgbColor && rgbColor.length > 0) {
            int R = 0, G = 0, B = 0;
            if (rgbColor.length == 1) {
                R = rgbColor[0];
            }
            if (rgbColor.length == 2) {
                R = rgbColor[0];
                G = rgbColor[1];
            }
            if (rgbColor.length == 3) {
                R = rgbColor[0];
                G = rgbColor[1];
                B = rgbColor[2];
            }
            int rgb = Color.rgb(R, G, B);
        }
        return null;
    }

    /**
     * 判断是不是深颜色
     * 转化为会对图像
     *
     * @return
     */
    public boolean isDeepRGB(int[] colors) {
        boolean isLight = false;
        int grayLevel = (int) (colors[0] * 0.299 + colors[1] * 0.587 + colors[2] * 0.114);
        if (grayLevel >= 192) {
            isLight = true;
        }
        return isLight;
    }


    public boolean isDeepRGB(int color) {
        boolean isLight = false;
        if (color != 0) {
            int R = color >> 16 & 0xff;
            int G = color >> 8 & 0xff;
            int B = color & 0xff;
            int[] ints = {R, G, B};

            isLight = isDeepRGB(ints);
        }


        return isLight;
    }

}
