package com.slxk.hounddog.mvp.weiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.mvp.ui.view.data.haibin.Calendar;
import com.slxk.hounddog.mvp.ui.view.data.haibin.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateSelectPopupWindow extends PopupWindow implements CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnYearChangeListener, CalendarView.OnMonthChangeListener {

    private Context context;
    private CallbackAction callbackAction = null;

    private TextView txtYear;
    private TextView txtMonth;
    private Button btnConfirm;
    private Button btnCancel;
    private TextView tvDataHint;
    private TextView tvSelectPromptHint;
    private TextView tvMonth;
    private TextView tvYear;

    private CalendarView calendarView = null;

    private Map<String, Calendar> calendarMap = new HashMap<>();

    public interface CallbackAction{
        void OnSelectDate(List<Calendar> calendarList);
    }

    @SuppressLint("DefaultLocale")
    public DateSelectPopupWindow(final Context context, ArrayList<Calendar> trackDateList, Calendar currentDay){
        super(context);
        this.context = context;

        View root = View.inflate(context, R.layout.popup_window_date_select, null);

        txtYear = root.findViewById(R.id.id_year);
        txtMonth = root.findViewById(R.id.id_month);
        btnConfirm = root.findViewById(R.id.btn_confirm);
        calendarView = root.findViewById(R.id.id_calendarView);
        btnCancel = root.findViewById(R.id.btn_cancel);
        tvDataHint = root.findViewById(R.id.tv_data_hint);
        tvSelectPromptHint = root.findViewById(R.id.tv_select_prompt_hint);
        tvMonth = root.findViewById(R.id.tv_month);
        tvYear = root.findViewById(R.id.tv_year);

        calendarView.setOnCalendarMultiSelectListener(this);
        calendarView.setOnYearChangeListener(this);
        calendarView.setOnMonthChangeListener(this);

        if(trackDateList.size() > 0){
            for (Calendar calendar: trackDateList) {
                calendarMap.put(calendar.toString(), calendar);
            }
        }

        calendarView.setSchemeDate(calendarMap);
        calendarView.scrollToCalendar(currentDay.getYear(), currentDay.getMonth(), currentDay.getDay());
        txtYear.setText(String.valueOf(currentDay.getYear()));
        txtMonth.setText(String.valueOf(currentDay.getMonth()));

        //设置SelectPicPopupWindow的View
        this.setContentView(root);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明  去除黑色边框
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.color_black_40));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Calendar> calendarList = calendarView.getMultiSelectCalendars();
                if(calendarList.size() > 0){
                    java.util.Calendar startCalendar = java.util.Calendar.getInstance();
                    startCalendar.set(
                            calendarList.get(0).getYear(),
                            calendarList.get(0).getMonth() - 1,
                            calendarList.get(0).getDay(),
                            0,
                            0,
                            0);

                    java.util.Calendar endCalendar = java.util.Calendar.getInstance();
                    endCalendar.set(
                            calendarList.get(calendarList.size() - 1).getYear(),
                            calendarList.get(calendarList.size() - 1).getMonth() - 1,
                            calendarList.get(calendarList.size() - 1).getDay(),
                            23,
                            59,
                            59);

                    if(endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis() > 1000 * 60 * 60 * 24 * 7){
                        ToastUtils.showShort(context.getString(R.string.max_7));
                        return;
                    }

                    callbackAction.OnSelectDate(calendarList);
                }else{
                    ToastUtils.showShort(context.getString(R.string.query_date));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 兼容 android 7.0之后设置showAsDropDown失效问题
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, a[1] + anchor.getHeight() + yoff);
        } else {
            super.showAsDropDown(anchor, xoff, yoff);
        }
    }

    public void setCallbackAction(CallbackAction callbackAction){
        this.callbackAction = callbackAction;
    }

    @Override
    public void onYearChange(int year) {
        txtYear.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month) {
        txtYear.setText(String.valueOf(year));
        txtMonth.setText(String.valueOf(month));
    }

    @Override
    public void onCalendarMultiSelectOutOfRange(Calendar calendar) {

    }

    @Override
    public void onMultiSelectOutOfSize(Calendar calendar, int maxSize) {
        ToastUtils.showShort(context.getString(R.string.max_7));
    }

    @Override
    public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {
    }
}
