package com.slxk.hounddog.mvp.utils;

import android.graphics.Color;

/**
 * 颜色工具类
 */
public class ColorUtil {

    /**
     * @return 字符串
     * @color: 参数
     * 类型：int
     * 例如：-1272178
     */
    public static int colorIdToRGBA(String strColor) {
        int color= Color.parseColor(strColor);
        int alpha = color >>> 24;
        int r = (color & 0xff0000) >> 16;
        int g = (color & 0xff00) >> 8;
        int b = color & 0xff;

        return Color.argb(alpha, r, g, b);
    }

    /**
     * @return 字符串
     * @red 红色数值
     * @green 绿色数值
     * @blue 蓝色色数值
     */
    public static String rgbToHex(int red, int green, int blue) {

        String hr = Integer.toHexString(red);
        String hg = Integer.toHexString(green);
        String hb = Integer.toHexString(blue);

        return "#" + hr + hg + hb;
    }

    /**
     * @return 字符串
     * @color: 参数
     * 类型：int
     * 例如：-1272178
     */
    public static String colorToHex(int color) {

        String R, G, B;

        StringBuffer sb = new StringBuffer();

        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));

        //判断获取到的R,G,B值的长度 如果长度等于1 给R,G,B值的前边添0
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        sb.append("#");
        sb.append(R);
        sb.append(G);
        sb.append(B);

        return sb.toString();
    }

}
