package com.slxk.hounddog.mvp.utils;

import android.text.TextUtils;

import com.slxk.hounddog.R;

/**
 * 快捷匹配数据结果工具类
 */
public class ResultDataUtils {

    // 推送类型
    public final static String Push_HuaWei = "e_hpush"; // 华为推送
    public final static String Push_JiGuang = "e_jpush"; // 极光推送
    public final static String Push_XiaoMi = "e_xmpush"; // 小米推送
    public final static String Push_OPPO = "e_oppopush"; // OPPO推送
    public final static String Push_VIVO = "e_vivopush"; // VIVO推送
    // Sim卡类型
    public final static String Sim_Type_zdst = "zdst"; //中点尚通的卡
    // 账号权限类型
    public final static String Account_Type_User = "e_role_user"; // 普通用户权限
    public final static String Account_Type_Manager = "e_role_manager"; // 管理员权限
    // 登录账号类型
    public final static String Login_type_Account = "e_account_login"; // 用户登录
    public final static String Login_type_Device = "e_device_login"; // 设备号登录
    public final static String Login_type_Phone_Account = "e_phone_account_login"; // 手机号绑定的账号登录
    public final static String Login_type_Phone_Device = "e_phone_device_login"; // 手机号绑定的设备号登录
    // 账号类型
    public final static String Account_User = "e_type_user"; // 终端组织
    public final static String Account_Busness = "e_type_busness"; // 销售商
    // 账号或设备缺失信息
    public final static String Lack_Password = "e_lack_pass"; // 缺少密码
    public final static String Lack_Phone = "e_lack_phone"; // 缺少绑定电话
    // 删除设备类型
    public final static String Device_Delete_Move = "e_del_no_move"; // 清除设备，但是不移动
    public final static String Device_Delete_Parent = "e_del_to_parent"; // 移动到设备的上一级组织中,将移入新建的分组中
    public final static String Device_Delete_Myself = "e_del_to_myself"; // 清除设备，并回到当前操作账号的组织下,将移入新建的分组中
    public final static String Device_Delete_Root = "e_del_to_root"; // 清除设备，并回到入库状态
    // 设备状态
    public final static String Device_State_Line_On = "e_line_on"; // 设备在线
    public final static String Device_State_Line_Sleep = "e_line_sleep"; // 设备休眠
    public final static String Device_State_Line_Down = "e_line_down"; // 设备离线
    // App更新下载方式
    public final static String App_Update_Interior = "e_dt_interior"; // 内部下载
    public final static String App_Update_Three = "e_dt_th3"; // 第三方浏览器下载
    // 定位模式
    public final static String Device_Mode_Nomal = "e_mode_nomal"; // 1-常用模式
    public final static String Device_Mode_Nomal_X7 = "e_mode_nomal_x7"; // 常用模式
    public final static String Device_Mode_Looploc = "e_mode_looploc"; // 2-周期定位模式
    public final static String Device_Mode_Fly = "e_mode_fly"; // 3-飞行模式
    public final static String Device_Mode_Sup_Save_Power = "e_mode_sup_save_power"; // 4-超级省电模式
    public final static String Device_Mode_Auto_Save_Power = "e_mode_auto_save_power"; // 5-智能省电模式
    public final static String Device_Mode_Sleep = "e_mode_sleep"; // 6-待机模式
    public final static String Device_Mode_Save_Power = "e_mode_save_power"; // 7-省电模式
    public final static String Device_Mode_Call_One = "e_mode_call_one"; // 8-点名模式
    public final static String Device_Mode_Sup_Save_Power_C2 = "e_mode_sup_save_power_c2"; // 9-超级省电模式(c2)
    // 周期定位
    public final static String Loop_Location_Mode_Week = "e_type_week"; // 周期定位类型，按周
    public final static String Loop_Location_Mode_Day = "e_type_day"; // 周期定位类型，按天
    // 实时定位，定时，定距
    public final static String Device_Mode_Real_Time_Time = "e_rtls_time"; // 定时
    public final static String Device_Mode_Real_Time_Distance = "e_rtls_space"; // 定距
    // 围栏报警类型
    public final static String Fence_Alarm_In = "e_fence_in"; // 进围栏
    public final static String Fence_Alarm_Out = "e_fence_out"; // 出围栏
    public final static String Fence_Alarm_In_Out = "e_fence_in_out"; // 进出围栏
    public final static String Fence_Alarm_Close = "e_fence_close"; // 关闭
    // 围栏类型
    public final static String Fence_Round = "e_type_circle"; // 圆形围栏
    public final static String Fence_Polygonal = "e_type_polygon"; // 多边形围栏
    public final static String Fence_City = "e_type_city"; // 地级市围栏
    // 远程开关机
    public final static String Remote_Switch_Open = "e_remote_open"; // 开机
    public final static String Remote_Switch_Close = "e_remote_close"; // 关机
    // 报警消息
    public final static String Alarm_Shake = "e_alarm_shake"; // 震动报警
    public final static String Alarm_Speed = "e_alarm_speed"; // 超速报警
    public final static String Alarm_Illegal_Open = "e_alarm_illegal_open"; // 非法开门
    public final static String Alarm_Illegal_Start = "e_alarm_illegal_start"; // 非法启动
    public final static String Alarm_Out_Fence = "e_alarm_out_fence"; // 出围栏
    public final static String Alarm_In_Fence = "e_alarm_in_fence"; // 进围栏
    public final static String Alarm_Light_Off = "e_alarm_light_off"; // 设备脱落报警
    public final static String Alarm_Lowpower = "e_alarm_lowpower"; // 设备低电量报警
    public final static String Alarm_Sensitivity = "e_alarm_sensitivity"; // 灵敏度报警
    public final static String Alarm_Power_Cut_Off = "e_alarm_power_cut_off"; // 断油断电报警
    public final static String Alarm_Acc_Off = "e_alarm_acc_off"; // ACC关闭报警
    public final static String Alarm_Dismantle = "e_alarm_dismantle"; // 防拆报警
    public final static String Alarm_Line_Dismantle = "e_alarm_line_dismantle"; // 有线的断电报警(防拆报警)
    public final static String Alarm_Recovery_Light = "e_alarm_recovery_light"; // 恢复光感脱落
    public final static String Alarm_Off_Line = "e_alarm_off_line"; // 离线报警
    public final static String Alarm_Satellite = "e_alarm_satellite"; // 卫星盲区报警
    public final static String Alarm_Start_Up = "e_alarm_start_up"; // 开机报警
    public final static String Alarm_Close_Down = "e_alarm_close_down"; // 关机报警
    public final static String Alarm_Displacement = "e_alarm_displacement"; // 位移报警
    public final static String Alarm_Pseudo_Base_Station = "e_alarm_pseudo_base_station"; // 伪基站报警
    public final static String Alarm_Speeding_End = "e_alarm_speeding_end"; // 解除超速
    public final static String Alarm_Start_Dev = "e_alarm_start_dev"; // 终端启动报警
    public final static String Alarm_Stop_Dev = "e_alarm_stop_dev"; // 停车报警
    public final static String Alarm_Remove_Lowpower = "e_alarm_remove_lowpower"; // 解除低电量报警
    public final static String Alarm_Remove_Out = "e_alarm_remove_out"; // 解除防拆报警
    public final static String Alarm_SOS = "e_alarm_sos"; // SOS报警
    // 特殊报警类型，报警方式
    public final static String Special_Alarm_None = "e_pa_none"; // 不通知
    public final static String Special_Alarm_Sms = "e_pa_sms"; // 短信通知
    public final static String Special_Alarm_Phone = "e_pa_phone"; // 电话通知
    public final static String Special_Alarm_All = "e_pa_all"; // 电话+短信通知
    // 电源状态accele
    public final static String Acc_Power_Charge = "e_acc_power_charge"; // 充电中
    public final static String Acc_Power_Interuupt = "e_acc_power_interuupt"; // 断电
    // Acc状态
    public final static String Acc_Close = "e_acc_close"; // acc关闭
    public final static String Acc_Open = "e_acc_open"; // acc开启
    public final static String Acc_Empty = "e_acc_empty"; // acc为空
    // 定时开关机
    public final static String Time_Switch_Every_Day = "e_loctype_eachday"; // 每天
    public final static String Time_Switch_Every_Month = "e_loctype_eachmonth"; // 每月
    public final static String Time_Switch_Current_Month = "e_loctype_currentmonth"; // 当月
    public static final String Time_Switch_Type_Close = "e_loctype_close"; // 关闭
    // 录音增值服务状态
    public final static String Record_Service_Invalid = "e_record_invalid"; // 不支持
    public final static String Record_Service_Close = "e_record_close"; // 未开通
    public final static String Record_Service_Three_Min = "e_record_min_3"; // 3分钟
    public final static String Record_Service_Five_Min = "e_record_min_5"; // 5分钟
    // 设备配置信息获取数据，type状态
    public final static String Config_All = "e_config_all"; // 其它所有
    public final static String Config_Alarm = "e_config_config"; // 报警相关
    public final static String Config_Phone = "e_config_phone"; // 电话相关
    public final static String Config_Other = "e_config_other"; // 其它所有
    // 定位点类型
    public static final String Location_Base_Station = "e_lbs"; // 基站
    public static final String Location_GPS = "e_gps"; // GPS
    public static final String Location_WIFI = "e_wifi"; // wifi
    public static final String Location_Static_Base_Station = "e_static_lbs";//静止基站
    public static final String Location_Static_Gps = "e_static_gps";//静止gps
    public static final String Location_Static_WIFI = "e_static_wifi";//静止wifi
    // 支持的定位模式类型
    public static final String Mode_Stand_By_Invalid = "e_mode_invalid"; // 什么模式都不支持
    public static final String Mode_Stand_By_Location = "e_mode_loc"; // 支持多选的模式
    public static final String Mode_Stand_By_Real_Time = "e_mode_rtls"; // 支持实时定位，定时定距
    // 设备支持的功能
    public static final String Function_Sleep = "e_cmd_sleep"; // 一键休眠
    public static final String Function_Wakeup = "e_cmd_wakeup"; // 一键唤醒
    public static final String Function_Restart = "e_cmd_restart"; // 一键重启
    public static final String Function_Reset = "e_cmd_reset"; // 恢复出厂设置
    public static final String Function_Finddev = "e_cmd_finddev"; // 找寻设备
    public static final String Function_Query_Location = "e_cmd_query_location"; // 查询位置信息
    public static final String Function_Alarm_Ack = "e_cmd_alarm_ack"; // 确认报警

    // 定位点类型-历史轨迹使用
    public static final int Location_Base_Station_Track = 0; // 基站
    public static final int Location_GPS_Track = 1; // GPS
    public static final int Location_WIFI_Track = 2; // wifi
    public static final int Location_Static_Base_Station_Track = 3;//静止基站
    public static final int Location_Static_Gps_Track = 4;//静止gps
    public static final int Location_Static_WIFI_Track = 5;//静止wifi

    // 用户权限管理
    public static final String Family_Auth_0 = "e_ua_move_dev"; // 转移设备
    public static final String Family_Auth_1 = "e_ua_del_dev"; // 冻结设备
    public static final String Family_Auth_2 = "e_ua_add_dev"; // 添加设备
    public static final String Family_Auth_3 = "e_ua_modify_dev"; // 修改设备信息
    public static final String Family_Auth_4 = "e_ua_load_new_dev"; // 添加新设备
    public static final String Family_Auth_5 = "e_ua_recycle_dev"; // 回收设备
    public static final String Family_Auth_6 = "e_ua_del_from_family"; // 从组织中删除,并且从数据库中删除
    public static final String Family_Auth_7 = "e_ua_modify_relay"; // 修改继电器开关
    public static final String Family_Auth_8 = "e_ua_del_move_family"; // 从组织中删除回到当前账号下
    public static final String Family_Auth_9 = "e_ua_del_move_to_root"; // 从组织中删除回到root名下
    public static final String Family_Auth_10 = "e_ua_del_move_to_parent"; // 从组织中删除回到上一级
    public static final String Family_Auth_11 = "e_ua_group_opt"; // 分组管理
    public static final String Family_Auth_12 = "e_ua_modify_family"; // 修改组织权限
    public static final String Family_Auth_13 = "e_ua_group_del"; // 删除分组
    public static final String Family_Auth_14 = "e_ua_add_fence"; // 添加围栏
    public static final String Family_Auth_15 = "e_ua_del_fence"; // 删除围栏
    public static final String Family_Auth_16 = "e_ua_modify_fence"; // 修改围栏
    public static final String Family_Auth_17 = "e_ua_add_user"; // 添加账户
    public static final String Family_Auth_18 = "e_ua_del_user"; // 删除账户
    public static final String Family_Auth_19 = "e_ua_add_next_user"; // 添加下级
    public static final String Family_Auth_20 = "e_ua_del_next_user"; // 删除下级
    public static final String Family_Auth_21 = "e_ua_modify_role"; // 设置权限
    public static final String Family_Auth_22 = "e_ua_modify_user_info"; // 修改用户信息
    public static final String Family_Auth_23 = "e_ua_del_alarm"; // 删除报警
    public static final String Family_Auth_24 = "e_ua_load_iccid"; // 导入iccid
    public static final String Family_Auth_25 = "e_ua_del_opt_log"; // 删除操作log
    public static final String Family_Auth_26 = "e_ua_change_host"; // 切换ip
    public static final String Family_Auth_27 = "e_ua_modify_log_switch"; // 控制设备日志开关

    // 消息中心，消息等级类型
    public static final String Message_Alarm_Notice = "e_alarm_notice"; // 告警信息
    public static final String Message_News_Notice = "e_news_notice"; // 新闻信息
    public static final String Message_Important_Notice = "e_important_notice"; // 重要信息
    public static final String Message_Nomal_Notice = "e_nomal_notice"; // 普通消息

    /**
     * 设备当前定位类型
     *
     * @param type 定位类型 0-基站 1-GPS 2-WIFI 3-静止基站 4-静止GPS 5-静止WIFI
     * @return
     */
    public static int getLocationType(String type) {
        if (!TextUtils.isEmpty(type)){
            if (type.equals(Location_GPS) || type.equals(Location_Static_Gps)) {
                return R.drawable.icon_gps;
            } else {
                return R.drawable.icon_base_station;
            }
        }else{
            return R.drawable.icon_gps;
        }
    }

}
