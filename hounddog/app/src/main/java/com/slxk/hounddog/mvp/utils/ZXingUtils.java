package com.slxk.hounddog.mvp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 */
public class ZXingUtils {

    public static Bitmap createQRCode(int size, String content) {
        Bitmap bitmap = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, String.valueOf(0));
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            //二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK;
                    } else {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createBarCode(int w, int h, String content) {
        Bitmap bitmap = null;
        try {
            //条形码CODE_128
            BarcodeFormat fomt = BarcodeFormat.CODE_128;
            BitMatrix matrix = new MultiFormatWriter().encode(content, fomt, w, h);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixel = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (matrix.get(j, i)) {
                        pixel[i * width + j] = Color.BLACK;
                    } else {
                        pixel[i * width + j] = Color.WHITE;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixel, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
