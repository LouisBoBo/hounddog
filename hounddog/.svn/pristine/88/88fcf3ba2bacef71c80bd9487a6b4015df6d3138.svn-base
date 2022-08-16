package com.slxk.hounddog.mvp.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 蓝牙工具类
 */
public class BluetoothUtils {

    /**
     * 时间格式
     *
     * @return
     */
    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss", Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * 时间格式
     *
     * @return
     */
    public static String getTodayDateYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return format.format(new Date());
    }

    /**
     * 消息处理及转义 7d02 -->7e  7d01 -->7d
     *
     * @param str
     * @return
     */
    public static String MsgEscaping(String str) {
        if (str == null || str.length() == 0) return "";
        if (str.startsWith("7e") && str.endsWith("7e") && str.length() >= 8) { //消息内容大于8 至少包含消息头，消息头2个字节
            //0、消息转义
            String strResult = str.substring(2, str.length() - 2);
            if (strResult.contains("7d02")) {
                strResult = strResult.replace("7d02", "7e");
            }
            if (strResult.contains("7d01")) {
                strResult = strResult.replace("7d01", "7d");
            }
            //1、获取消息头
            String strHead = strResult.substring(0, 4);
            try {
                //2、获取消息头 + 消息体
                String bodyHead = "";
                if (strResult.length() > 6) { // 包含消息头，消息体， 不包含校验位
                    bodyHead = strResult.substring(0, strResult.length() - 2);
                }
                //2、获取消息body
                String body = "";
                if (strResult.length() > 6) { // 包含消息头，消息体， 不包含校验位
                    body = strResult.substring(4, strResult.length() - 2);
                }
                //3、获取校验位
                String code = "";
                if (strResult.length() > 6) { // 2字节消息头 1字节校验码
                    code = strResult.substring(strResult.length() - 2);
                }
//                LogUtils.e("strResult=" + strResult);
//                LogUtils.e("strHead=" + strHead);
//                LogUtils.e("bodyHead=" + bodyHead);
//                LogUtils.e("body=" + body);
//                LogUtils.e("code=" + code);
                if (bodyHead.length() > 0 && code.length() > 0) {
                    String checkCode = checkCode(bodyHead);
                    if (checkCode.length() == 1) checkCode = "0" + checkCode;
//                    LogUtils.e("checkCode=" + checkCode);
                    if (checkCode.equals(code)) { // 校验位 一致时，整条消息才是正确的
                        return bodyHead;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 校验码
     *
     * @param para
     * @return
     */
    public static String checkCode(String para) {
        int length = para.length() / 2;
        String[] dateArr = new String[length];

        for (int i = 0; i < length; i++) {
            dateArr[i] = para.substring(i * 2, i * 2 + 2);
        }
        String code = "00";
        for (int i = 0; i < dateArr.length; i++) {
            code = xor(code, dateArr[i]);
        }
        return code;
    }

    private static String xor(String strHex_X, String strHex_Y) {
        //将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
//        Log.e("code", result);
        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    /**
     * 二进制转10进制
     *
     * @param value
     * @return
     */
    public static int onTypeConversion_1(String value) {
        return Integer.parseInt(value, 2);
    }

    /**
     * 10进制转二进制
     *
     * @param value
     * @return
     */
    public static String onTypeConversion_2(int value) {
        return Integer.toBinaryString(value);
    }

    /**
     * 10进制转16进制
     *
     * @param value
     * @return
     */
    public static String onTypeConversion_3(int value) {
        return Integer.toHexString(value);
    }

    /**
     * 16进制转10进制
     *
     * @param value
     * @return
     */
    public static int onTypeConversion_4(String value) {
        return Integer.parseInt(value, 16);
    }

    /**
     * 二进制转16进制
     *
     * @param value
     * @return
     */
    public static String onTypeConversion_5(String value) {
        int ten = Integer.parseInt(value, 2);
        return Integer.toHexString(ten);
    }

    /**
     * 16进制转二进制
     *
     * @param value
     * @return
     */
    public static String onTypeConversion_7(String value) {
        int ten = Integer.parseInt(value, 16);
        return Integer.toBinaryString(ten);
    }

    /**
     * 二进制数据补位，高位补0，返回16进制字符串
     *
     * @return
     */
    public static String onAppendValue_0(String value) {
        int length = value.length(); // 当前二进制位数
        int difference = 8 - length; // 需要补多少位
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < difference; i++) {
            str.append("0");
        }
        str.append(value);
        String result = onTypeConversion_5(str.toString());
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * 二进制数据补位，高位补1，返回16进制字符串
     *
     * @return
     */
    public static String onAppendValue_1(String value) {
        int length = value.length(); // 当前二进制位数
        int difference = 8 - length; // 需要补多少位
        StringBuilder str = new StringBuilder();
        if (difference > 0){
            str.append("1");
            for (int i = 1; i < difference; i++) {
                str.append("0");
            }
        }
        str.append(value);
        String result = onTypeConversion_5(str.toString());
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * 二进制数据补位，高位补0，返回十进制
     *
     * @param value 待转换的十六进制字符串
     * @return
     */
    public static int onAppendValue_2(String value) {
        String value_2 = onTypeConversion_7(value); // 转二进制
        int length = value_2.length(); // 当前二进制位数
        int difference = 8 - length; // 需要补多少位
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < difference; i++) {
            str.append("0");
        }
        str.append(value_2);
        return onTypeConversion_1(str.toString().substring(1));
    }

    /**
     * 设备上报数据，电量解析
     *
     * @param value
     * @return
     */
    public static int onPowerParse(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        String value_2 = onTypeConversion_7(value); // 转二进制
        if (value_2.length() == 8) {
            value_2 = value_2.substring(1);
        }
        int length = value_2.length(); // 当前二进制位数
        int difference = 8 - length; // 需要补多少位
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < difference; i++) {
            str.append("0");
        }
        str.append(value_2);
        return onTypeConversion_1(str.toString());
    }

    /**
     * 文件转byte二进制
     *
     * @param path 需要转byte的文件路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String path) throws Exception {
        FileInputStream fs = new FileInputStream(path);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static String bytesToHexString(byte b[]) {
        if (b == null) {
            return "";
        }
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (byte value : b) {
            stmp = Integer.toHexString(value & 0xff);
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase().trim();
    }

    /**
     * 字符串的16进制累加和，已知一段字符串和校验码，校验和累加是否合法
     *
     * @param data
     * @return
     */
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }

        // 用256求余最大是255，即16进制的FF
//        int mod = total % 256;
//        String hex = Integer.toHexString(mod);
//        len = hex.length();
//        // 如果不够校验位的长度，补0,这里用的是两位校验
//        if (len < 2) {
//            hex = "0" + hex;
//        }

        return onCalculateResult(total);
    }

    /**
     * 补足4字节，不足的后面补0，字节倒序上传
     *
     * @param dataLength 总长度
     */
    public static String onCalculateResult(int dataLength) {
        String strDataLength = onTypeConversion_3(dataLength);
        int difference = 8 - strDataLength.length(); // 需要补多少位
        for (int i = 0; i < difference; i++) {
            strDataLength = "0" + strDataLength;
        }
        return strDataLength.substring(6) + strDataLength.substring(4, 6)
                + strDataLength.substring(2, 4) + strDataLength.substring(0, 2);
    }

    /**
     * 补足2字节，不足的后面补0，字节倒序上传
     *
     * @param dataLength 总长度
     */
    public static String onCalculateResult2(int dataLength) {
        String strDataLength = onTypeConversion_3(dataLength);
        int difference = 4 - strDataLength.length(); // 需要补多少位
        for (int i = 0; i < difference; i++) {
            strDataLength = "0" + strDataLength;
        }
        return strDataLength.substring(2) + strDataLength.substring(0, 2);
    }

    /**
     * GB2312转换成 16进制
     * @param txt
     * @return
     */
    public static String toGBHex(String txt) {
        String hexStr = "";
        try {
            byte[] arr = txt.getBytes("GB2312");
            //将数组转为16进制字符串
            for (byte b : arr) {
                String str = byteToHex(b);
                hexStr = hexStr + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexStr;
    }


    public static String byteToHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

}
