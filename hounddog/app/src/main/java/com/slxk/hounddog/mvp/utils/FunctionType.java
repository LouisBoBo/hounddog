package com.slxk.hounddog.mvp.utils;

import android.text.TextUtils;

/**
 * 设备型号工具类
 */
public class FunctionType {

    public static final String C2 = "C2";
    public static final String SK7X = "SK7X";
    public static final String M2 = "M2";
    public static final String M1 = "M1";
    public static final String LW1 = "LW1";
    public static final String SK7L = "SK7L"; // 新增型号，功能同SK7X一样
    public static final String SK7_1 = "SK7_1"; // 新增型号，功能同SK7X一样
    public static final String X2 = "X2"; // 新增型号，功能同SK7X一样
    public static final String X7 = "X7"; // 新增型号，功能与M1的一样
    public static final String M3 = "M3"; // 功能与SK7X一样
    public static final String P7_A = "P7_A";

    public static final String D5S = "D5S"; // 新增型号，功能与D5一样，定位模式只有一种，实时定位中的定时定位模式
    public static final String D8 = "D8"; // 新增型号，功能与D5一样，定位模式只有一种，实时定位中的定时定位模式
    public static final String A9S = "A9S"; // 新增型号，功能与A9_P一样，定位模式只有一种，实时定位中的定时定位模式
    public static final String D5SN = "D5SN"; // 新增信号，功能跟D5一致，没有录音跟远程听音功能
    public static final String D8N = "D8N"; // 新增信号，功能跟D8一致，没有录音跟远程听音功能

    public static final String A7_P = "A7_P";
    public static final String A9_P = "A9_P";
    public static final String R1 = "R1"; // 新增型号，功能跟A9_P一致，另外没有ACC 断油电功能
    public static final String C16 = "C16"; // 新增型号，功能同M1一样，定位模式跟A9_P一样

    /**
     * 是否支持超级省电模式显示
     * @param devType
     * @return
     */
    public static boolean isModeForSuperPower(String devType){
        devType = devType.toUpperCase();
        if (devType.equals(SK7X) || devType.equals(M2)
                || devType.equals(M1) || devType.equals(LW1)
                || devType.equals(P7_A) || devType.equals(X2)
                || devType.equals(SK7L) || devType.equals(SK7_1)
                || devType.equals(X7) || devType.equals(M3)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 是否只支持实时定位中的定时模式
     * @param devType
     * @return
     */
    public static boolean isModeForRealTimeModeAndTime(String devType){
        devType = devType.toUpperCase();
        if (devType.equals(D5S) || devType.equals(D8) || devType.equals(A9S) || devType.equals(D5SN) || devType.equals(D8N)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 实时定位
     * @param devType
     * @return
     */
    public static boolean isRealTimeLocation(String devType){
        if (TextUtils.isEmpty(devType)){
            return false;
        }
        return devType.equals(A7_P) || devType.equals(A9_P) || devType.equals(R1) || devType.equals(C16) || devType.equals(A9S)
                || devType.equals(D5S) || devType.equals(D8) || devType.equals(D8N) || devType.equals(D5SN);
    }

}
