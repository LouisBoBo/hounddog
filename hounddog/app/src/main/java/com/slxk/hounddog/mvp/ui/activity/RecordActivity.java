package com.slxk.hounddog.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.db.RecordModel;
import com.slxk.hounddog.db.RecordTimeModel;
import com.slxk.hounddog.di.component.DaggerRecordComponent;
import com.slxk.hounddog.mvp.contract.RecordContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.RecordConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordScheduleResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordShortResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.RecordConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordDeletePutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordPutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordSchedulePutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordShortPutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordShortResultPutBean;
import com.slxk.hounddog.mvp.presenter.RecordPresenter;
import com.slxk.hounddog.mvp.ui.adapter.RecordAdapter;
import com.slxk.hounddog.mvp.utils.AmrToWavNSUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.MediaManager;
import com.slxk.hounddog.mvp.utils.RecordDownload;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;
import com.slxk.hounddog.mvp.weiget.RecordSharePopupwindow;
import com.slxk.hounddog.mvp.weiget.RecordTaskProgressDialog;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 录音
 * <p>
 * Created by MVPArmsTemplate on 12/23/2021 16:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class RecordActivity extends BaseActivity<RecordPresenter> implements RecordContract.View , LifecycleObserver, SensorEventListener {

    @BindView(R.id.toolbar_right)
    TextView toolbarRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srlView;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;

    private static final int PERMISSON_REQUESTCODE = 1; // 获取权限回调码
    // 判断是否需要检测权限，防止不停的弹框
    private boolean isNeedCheck = true;
    private boolean isShowAuth = true; // 判断只弹一次权限提示
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // 录音数据
    private ArrayList<RecordModel> recordDataList; // 获取到的录音数据
    private RecordAdapter mAdapter;
    private int mWidth; // 屏幕的宽度
    private ArrayList<RecordResultBean.DataBean> listFile; // 新的录音获取方式，url数据
    private ArrayList<RecordModel> deleteBeans; // 需要删除的录音数据
    private ArrayList<Long> deleteRecordTime; // 需要删除的录音时间戳,如果不填，表示删除全部

    // 录音历史时间，用来获取历史录音数据
    private ArrayList<RecordTimeModel> recordTimeModels; // 获取到的录音数据
    private RecordTimeModel historyTimeModel; // 历史时间model

    private int secondTime = 30; // 短录音下发时间，固定30秒
    private int recordTotal = 0; // 录音剩余总数
    private int limitSize = 20; // 限制获取数量
    private long startTime = 0; // 开始时间
    private long endTime = 0; // 结束时间
    private String playRecordName = ""; // 录音文件路径
    private boolean isExpire; // 是否过期-录音增值服务
    private int isRecordSupport; // 短录音，实时录音是否支持，-1，不支持，1支持，2只读
    private int isVorswitchSupport; // 声控开关,0关,1开,-1不支持，2只读
    private boolean isRecording = false; //设备是否是短录音状态
    private boolean isRealTimeRecording = false; // 设备是否是实时录音状态
    private boolean isRecordEnabled = false; // 是否是查询短录音下发指令，是否未执行开始录音，是的话，按钮不可点击，背景浅色
    private long shortRecordWaitTime = 0; //短录音等待时间，用来跟超时时间做对比
    private int timeOutTwo = 120; // 请求短录音进度超时时间
    private static final int mShortCountDownTime = 5; // 短录音获取录音进度倒计时
    private int mShortCountDown = mShortCountDownTime; // 短录音获取录音进度倒计时递减时长
    private static final int mShortResultTime = 2; // 获取开始短录音指令下发结果倒计时
    private int mShortResult = mShortResultTime; // 获取开始短录音指令下发结果倒计时
    private static final int mShortResultTimeMax = 60; // 获取开始短录音指令下发结果倒计时-最大值，超过就提示下发录音失败
    private int mShortTimeMax = 0; // 获取开始短录音指令下发结果倒计时-最大值，超过就提示下发录音失败

    private boolean isEditDelete = false; // 是否编辑状态
    private int mSelectCount = 0; // 选中的文件个数
    private boolean isAllSelect = false; // 是否全选录音
    private boolean isRefreshNow = false; // 是否正在下拉加载更多数据
    private boolean isRecordActivity = true; // 是否是首次进入页面，是的话，存储历史录音时间，用来判断获取实时录音时是否存储历史录音时间

    private boolean isFirstGetRecord = true; // 是否是首次加载实时数据，每次进入页面
    private String mSchedule = "0"; // 服务器返回的录音进度
    private Timer requestOnTimer; // 倒计时
    private boolean isShowTip = false; // 获取录音开始结果接口是否弹出提示语

    private RecordDownload realTimeRecordDownload; // 实时录音下载
    private RecordDownload historyRecordDownload; // 历史录音下载

    private RecordSharePopupwindow mPopupwindow; // 录音分享

    // 录音删除
    private RecordTaskProgressDialog taskProgressDialog; // 录音删除进度
    private static final int deleteMaxNumber = 300; // 本地数据库一次最大删除数量
    private int deleteNumber = 0; // 当前已删除的数量
    private int deleteTotalNumber = 0; // 删除的总数
    private ArrayList<RecordModel> deleteRecordProgress; // 批量删除的录音

    private MyDao mRecordBeanDao; // 录音信息操作dao
    private MyDao mRecordTimeBeanDao; // 录音历史时间节点信息操作dao

    private String mImei; // 设备imei号
    private String mSimei; // 设备imei号
    private static final String Intent_Imei_Key = "imei_key";
    private static final String Intent_Simei_Key = "simei_key";

    //声音管理器
    static AudioManager mAudioManager;

    private PowerManager.WakeLock wakeLock;
    private PowerManager mPowerManager;

    //以下开始传感器监听
    static SensorManager mSensorManager;
    static Sensor sensor;

    public static Intent newInstance(String imei, String simei) {
        Intent intent = new Intent(MyApplication.getMyApp(), RecordActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Simei_Key, simei);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRecordComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_record;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        checkPermissions(needPermissions);
        setTitle(getString(R.string.record));
        toolbarRight.setVisibility(View.VISIBLE);
        onRecordEditShow();

        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mPowerManager = (PowerManager) this.getSystemService(POWER_SERVICE);

        recordDataList = new ArrayList<>();
        listFile = new ArrayList<>();
        deleteBeans = new ArrayList<>();
        deleteRecordTime = new ArrayList<>();
        recordTimeModels = new ArrayList<>();
        deleteRecordProgress = new ArrayList<>();
        mWidth = ScreenUtils.getScreenWidth();
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        mSimei = getIntent().getStringExtra(Intent_Simei_Key);

        try {
            mRecordBeanDao = new MyDao(RecordModel.class);
            mRecordTimeBeanDao = new MyDao(RecordTimeModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        srlView.setColorSchemeResources(R.color.color_00C8F8, R.color.color_00C8F8);
        srlView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefreshNow) {
                    ToastUtils.showShort(getString(R.string.record_download_for_history));
                    srlView.setRefreshing(false);
                    return;
                }

                onGetTimeDBData();
            }
        });

        mAdapter = new RecordAdapter(R.layout.item_record, recordDataList, mWidth);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_record: // 播放录音
                        if (isEditDelete) {
                            onRecordSelect(position);
                        } else {
                            onPlayRecord(position, recordDataList.get(position).getPath());
                        }
                        break;
                    case R.id.iv_select: // 选中录音
                        onRecordSelect(position);
                        break;
                    case R.id.rl_record_parent: // 选中录音
                        if (isEditDelete) {
                            onRecordSelect(position);
                        }
                        break;
                }
            }
        });
        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                // 分享
                onRecordShareClick(view, recordDataList.get(position));
                return false;
            }
        });

        onGetDBData(1);

        btnRecord.postDelayed(new Runnable() {
            @Override
            public void run() {
                getRecordConfig();
            }
        }, 300);
    }

    /**
     * 显示编辑取消按钮
     */
    private void onRecordEditShow(){
        toolbarRight.setText(isEditDelete ? getString(R.string.cancel) : getString(R.string.edit));
    }

    /**
     * 录音是否选中
     *
     * @param position 当前位置
     */
    private void onRecordSelect(int position) {
        if (recordDataList.get(position).isSelect()) {
            mSelectCount--;
        } else {
            mSelectCount++;
        }
        recordDataList.get(position).setSelect(!recordDataList.get(position).isSelect());
        mAdapter.notifyItemChanged(position);
        onComputeDeleteCount();
    }

    @Override
    protected void onResume() {
        if (!TextUtils.isEmpty(playRecordName)) {
            MediaManager.resume();
        }

        mShortResult = mShortResultTime;
        isFirstGetRecord = true;
        isRecordActivity = true;
        isShowTip = false;
        btnRecord.postDelayed(new Runnable() {
            @Override
            public void run() {
                getShortRecordResult();
                handlerStartRecordResult.sendEmptyMessage(1);
            }
        }, 600);
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            if (!TextUtils.isEmpty(playRecordName)) {
                MediaManager.pause();
            }
            if (requestOnTimer != null) {
                requestOnTimer.cancel();
                requestOnTimer = null;
            }

            if (realTimeRecordDownload != null) {
                realTimeRecordDownload.cancelDownload();
                realTimeRecordDownload = null;
            }

            if (historyRecordDownload != null) {
                historyRecordDownload.cancelDownload();
                historyRecordDownload = null;
            }

            handlerShort.removeCallbacksAndMessages(null);
            handlerStartRecordResult.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (!TextUtils.isEmpty(playRecordName)) {
            MediaManager.pause();
        }
        if (requestOnTimer != null) {
            requestOnTimer.cancel();
            requestOnTimer = null;
        }

        if (realTimeRecordDownload != null) {
            realTimeRecordDownload.cancelDownload();
            realTimeRecordDownload = null;
        }

        if (historyRecordDownload != null) {
            historyRecordDownload.cancelDownload();
            historyRecordDownload = null;
        }

        handlerShort.removeCallbacksAndMessages(null);
        handlerStartRecordResult.removeCallbacksAndMessages(null);
        handlerCallback.removeCallbacksAndMessages(null);
        handlerCallbackForHistory.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**
     * 按钮点击状态
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateRecordState() {
        if (isRecording) {
            btnRecord.setText(getString(R.string.recording));
            btnRecord.setEnabled(false);
            btnRecord.setBackground(getResources().getDrawable(R.drawable.bg_73e1fb_fillet_60_6));
        } else {
            btnRecord.setEnabled(true);
            btnRecord.setText(getString(R.string.start_record));
            btnRecord.setBackground(getResources().getDrawable(R.drawable.bg_00c8f8_fillet_6));
        }
    }

    /**
     * 录音按钮是否可点击，查询短录音指令下发结果时调用，如果正在查询，则按钮背景变浅
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void onRecordStateIsEnabled() {
        if (isRecordEnabled) {
            btnRecord.setEnabled(false);
            btnRecord.setBackground(getResources().getDrawable(R.drawable.bg_73e1fb_fillet_60_6));
        } else {
            btnRecord.setEnabled(true);
            btnRecord.setBackground(getResources().getDrawable(R.drawable.bg_00c8f8_fillet_6));
        }
    }

    /**
     * 从本地数据库获取历史录音时间
     */
    private void onGetTimeDBData() {
        recordTimeModels.clear();
        historyTimeModel = null;
        endTime = 0;
        try {
            List<RecordTimeModel> listRecord = getRecordTimeDataBase(mImei);
            if (listRecord != null && listRecord.size() > 0) {
                recordTimeModels.addAll(listRecord);
                onListSortForTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (recordTimeModels.size() > 0) {
            historyTimeModel = recordTimeModels.get(recordTimeModels.size() - 1);
        }

        isRefreshNow = true;
        getRecordData(true);
    }

    /**
     * 从数据库get数据-根据主键key  获取录音历史时间
     *
     * @param imei
     * @return
     */
    public List<RecordTimeModel> getRecordTimeDataBase(String imei) {
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put(RecordTimeModel.KEY, "time" + imei);
            return (List<RecordTimeModel>) mRecordTimeBeanDao.query(mRecordTimeBeanDao.queryBuilder(map1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件 - 历史录音时间
     *
     * @param recordModel
     */
    public void onDeleteRecordTimeForDB(RecordTimeModel recordModel) {
        try {
            mRecordTimeBeanDao.delete(recordModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 冒泡排序法排序 - 排序时间
     */
    private void onListSortForTime() {
        if (recordTimeModels != null) {
            RecordTimeModel tmpObj = null;
            for (int i = 0; i < recordTimeModels.size(); ++i) {
                for (int j = i + 1; j < recordTimeModels.size(); ++j) {
                    long iTick = recordTimeModels.get(i).getRecordTime();
                    long jTick = recordTimeModels.get(j).getRecordTime();
                    if (jTick < iTick) {
                        tmpObj = recordTimeModels.get(i);
                        recordTimeModels.set(i, recordTimeModels.get(j));
                        recordTimeModels.set(j, tmpObj);
                    }
                }
            }
        }
    }

    /**
     * 从本地数据库获取数据
     *
     * @param type 1：定位到最后一条数据，2：定位到第一条数据
     */
    private void onGetDBData(int type) {
        recordDataList.clear();
        try {
            List<RecordModel> listRecord = getRecordDataBase(mImei);
            if (listRecord != null && listRecord.size() > 0) {
                recordDataList.addAll(listRecord);
                onListSort();
                if (recyclerView != null) {
                    mAdapter.notifyDataSetChanged();
                    if (recordDataList.size() > 0) {
                        if (type == 1) {
                            recyclerView.scrollToPosition(recordDataList.size() - 1);
                        } else {
                            recyclerView.scrollToPosition(0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库get数据-根据主键key
     *
     * @param imei
     * @return
     */
    public List<RecordModel> getRecordDataBase(String imei) {
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put(RecordModel.KEY, "record" + imei);
            return (List<RecordModel>) mRecordBeanDao.query(mRecordBeanDao.queryBuilder(map1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量删除文件
     *
     * @param recordModel
     */
    public void onDeleteRecordForDB(List<RecordModel> recordModel) {
        try {
            mRecordBeanDao.delete(recordModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 冒泡排序法排序
     */
    private void onListSort() {
        if (recordDataList != null) {
            RecordModel tmpObj = null;
            for (int i = 0; i < recordDataList.size(); ++i) {
                for (int j = i + 1; j < recordDataList.size(); ++j) {
                    long iTick = recordDataList.get(i).getRecordTime();
                    long jTick = recordDataList.get(j).getRecordTime();
                    if (jTick < iTick) {
                        tmpObj = recordDataList.get(i);
                        recordDataList.set(i, recordDataList.get(j));
                        recordDataList.set(j, tmpObj);
                    }
                }
            }
        }
    }

    /**
     * 获取录音相关配置参数
     */
    private void getRecordConfig() {
        RecordConfigPutBean.ParamsBean paramsBean = new RecordConfigPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);

        RecordConfigPutBean bean = new RecordConfigPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Record_Config);
        bean.setModule(ModuleValueService.Module_For_Record_Config);

        showProgressDialog();

        if (getPresenter() != null) {
            getPresenter().getRecordConfig(bean);
        }
    }

    /**
     * 获取录音数据
     *
     * @param isHistoryRecord 是否是历史录音数据
     */
    private void getRecordData(boolean isHistoryRecord) {
        startTime = SPUtils.getInstance().getLong(ConstantValue.Record_Last_Time + mImei, 0);

        RecordPutBean.ParamsBean paramsBean = new RecordPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);
        paramsBean.setLimit_size(limitSize);
        if (isHistoryRecord) {
            if (historyTimeModel != null) {
                endTime = historyTimeModel.getRecordTime();
            }
            if (endTime == 0) {
                if (recordDataList.size() > 0) {
                    endTime = recordDataList.get(0).getRecordTime() - 1;
                } else {
                    endTime = System.currentTimeMillis();
                    endTime = Long.parseLong(String.valueOf(endTime).substring(0, 10));
                }
            }
        } else {
            endTime = System.currentTimeMillis();
            endTime = Long.parseLong(String.valueOf(endTime).substring(0, 10));
        }
        paramsBean.setEndtime(endTime);
        if (!isHistoryRecord && startTime != 0) {
            paramsBean.setBegintime(startTime);
        }

        RecordPutBean bean = new RecordPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Record_List);
        bean.setModule(ModuleValueService.Module_For_Record_List);

        if (getPresenter() != null) {
            getPresenter().getRecordData(bean, isHistoryRecord);
        }
    }

    /**
     * 获取开始短录音指令下发结果
     */
    @SuppressLint("HandlerLeak")
    private Handler handlerStartRecordResult = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mShortResult--;
            if (mShortResult <= 0) {
                getShortRecordResult();
                mShortResult = mShortResultTime;
                mShortTimeMax = mShortTimeMax + mShortResult;
            }
            handlerStartRecordResult.sendEmptyMessageDelayed(1, 1000);
        }
    };

    /**
     * 短录音获取录音进度
     */
    @SuppressLint("HandlerLeak")
    private Handler handlerShort = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mShortCountDown--;
            if (mShortCountDown <= 0) {
                getShortRecordProcess();
                mShortCountDown = mShortCountDownTime;
            }
            handlerShort.sendEmptyMessageDelayed(1, 1000);
        }
    };

    /**
     * 开始播放录音
     *
     * @param index 当前索引位置
     * @param path
     */
    private void onPlayRecord(final int index, String path) {
        if (playRecordName.equals(path)) {
            playRecordName = "";
        } else {
            playRecordName = path;
        }
        MediaManager.release();
        if (!TextUtils.isEmpty(playRecordName)) {
            File file = new File(playRecordName);
            if (file.exists()) {
                registerProximitySensorListener();

                MediaManager.playSound(file.getPath(),new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playRecordName = "";
                        if (index < recordDataList.size() - 1) {
                            //播放下一条数据
                            onPlayRecord(index + 1, recordDataList.get(index + 1).getPath());
                        } else {
                            onRecordPlayNow();
                        }
                    }
                });

            } else {
                ToastUtils.showShort(getString(R.string.data_delete));
            }
        }

        // 判断是否播放过了，如果没有播放，播放之后，设置已播放
        RecordModel record = recordDataList.get(index);
        if (!record.isPlayed()) {
            record.setPlayed(true);
            try {
                mRecordBeanDao.update(record);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        onRecordPlayNow();
    }

    /**
     * 筛选播放录音的位置
     */
    private void onRecordPlayNow() {
        if (!TextUtils.isEmpty(playRecordName)) {
            for (RecordModel bean : recordDataList) {
                if (bean.getPath().equals(playRecordName)) {
                    bean.setPlayNow(true);
                } else {
                    bean.setPlayNow(false);
                }
            }
        } else {
            for (RecordModel bean : recordDataList) {
                bean.setPlayNow(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 全选
     */
    @SuppressLint("SetTextI18n")
    private void onRecordAllSelect() {
        isAllSelect = !isAllSelect;
        for (RecordModel bean : recordDataList) {
            bean.setSelect(isAllSelect);
        }
        mAdapter.notifyDataSetChanged();
        if (isAllSelect) {
            mSelectCount = recordDataList.size();
        } else {
            mSelectCount = 0;
        }
        onComputeDeleteCount();
    }

    /**
     * 计算选中的录音数量
     */
    @SuppressLint("SetTextI18n")
    private void onComputeDeleteCount() {
        if (mSelectCount < 0) {
            mSelectCount = 0;
        }
        if (mSelectCount == 0) {
            tvDelete.setText(getString(R.string.delete));
        } else {
            tvDelete.setText(getString(R.string.delete) + "(" + mSelectCount + ")");
        }
        if (mSelectCount == recordDataList.size()) {
            tvSelectAll.setText(getString(R.string.select_all_cancel));
            isAllSelect = true;
        } else {
            tvSelectAll.setText(getString(R.string.select_all));
            isAllSelect = false;
        }
    }

    /**
     * 分享
     *
     * @param view
     * @param model
     */
    private void onRecordShareClick(View view, final RecordModel model) {
        if (mPopupwindow != null && mPopupwindow.isShowing()) {
            mPopupwindow.dismiss();
        } else {
            mPopupwindow = new RecordSharePopupwindow(this);
            mPopupwindow.setRecordShareChange(new RecordSharePopupwindow.onRecordShareChange() {
                @Override
                public void onRecordShareClick() {
                    onRecordShare(model);
                    mPopupwindow.dismiss();
                }
            });
            mPopupwindow.showAsDropDown(view);
        }
    }

    /**
     * 新分享
     *
     * @param model
     */
    @SuppressLint("StaticFieldLeak")
    private void onRecordShare(final RecordModel model) {
        if (Utils.isPkgInstalled(this, "com.tencent.mm")) {
            try {
                File target = new File(model.getPath());
                if (!target.exists()) {
                    ToastUtils.showShort(getString(R.string.data_delete));
                    return;
                }

                Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                Platform.ShareParams shareParams = new Platform.ShareParams();
                shareParams.setText(getString(R.string.app_name_share));
                shareParams.setTitle(getString(R.string.record_share) + ".wav");

                shareParams.setShareType(Platform.SHARE_FILE);
                shareParams.setFilePath(model.getPath());
                platform.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                        File target = new File(targetPath);
//                        if(target.exists()){
//                            target.delete();
//                        }
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                    }
                });
                platform.share(shareParams);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.showShort(getString(R.string.no_install_wx));
        }
    }

    /**
     * @param path          源文件
     * @param deleteAmrFile true 需要删除源文件， false 分享录音时无需删除
     * @return
     */
    private String AmrToWavConversion(final String path, final boolean deleteAmrFile) {
        final String targetPath = path.replace(".amr", ".wav");
        File target = new File(targetPath);
        //已经存在，表示已经降噪过，直接返回文件路径
        if (target.exists()) {
            return targetPath;
        }
        AmrToWavNSUtil.getAmrToWavNSUtilInstance().setResultListener(
                new AmrToWavNSUtil.getResultListener() {
                    @Override
                    public void setResult(String str) {
                        File fileAmr = new File(path);
                        if (fileAmr.exists() && deleteAmrFile) {
                            fileAmr.delete();
                        }
                    }
                }).onAmrToWavConversion(path, targetPath);
        return targetPath;
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.toolbar_right, R.id.btn_record, R.id.tv_select_all, R.id.tv_delete})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.toolbar_right:
                    isEditDelete = !isEditDelete;
                    onDeleteSelect();
                    break;
                case R.id.btn_record:
                    submitShortRecord();
                    break;
                case R.id.tv_select_all:
                    onRecordAllSelect();
                    break;
                case R.id.tv_delete:
                    onDeleteRecord();
                    break;
            }
        }
    }

    /**
     * 切换编辑状态显示
     */
    private void onDeleteSelect() {
        if (!TextUtils.isEmpty(playRecordName)) {
            playRecordName = "";
            MediaManager.release();
            onRecordPlayNow();
        }
        if (isEditDelete && recordDataList.size() == 0) {
            isEditDelete = false;
            ToastUtils.showShort(getString(R.string.error_no_data_record));
            return;
        }

        if (isEditDelete) {
            btnRecord.setVisibility(View.GONE);
            llDelete.setVisibility(View.VISIBLE);
        } else {
            llDelete.setVisibility(View.GONE);
            btnRecord.setVisibility(View.VISIBLE);
        }
        for (RecordModel bean : recordDataList) {
            bean.setDelete(isEditDelete);
            if (!isEditDelete) {
                bean.setSelect(false);
            }
        }
        if (!isEditDelete) {
            isAllSelect = false;
            mSelectCount = 0;
        }
        mAdapter.notifyDataSetChanged();
        onRecordEditShow();
    }

    /**
     * 删除录音
     */
    private void onDeleteRecord() {
        deleteBeans.clear();
        deleteRecordTime.clear();
        deleteNumber = 0;
        if (isAllSelect) {
            deleteBeans.addAll(recordDataList);
        } else {
            for (RecordModel bean : recordDataList) {
                if (bean.isSelect()) {
                    deleteBeans.add(bean);
                    deleteRecordTime.add(bean.getRecordTime());
                }
            }
        }

        if (deleteBeans.size() > 0) {
            AlertBean bean = new AlertBean();
            bean.setTitle(getString(R.string.tip));
            if (isAllSelect) {
                bean.setAlert(getString(R.string.delete_all_record_hint));
            } else {
                bean.setAlert(getString(R.string.delete_record_hint));
            }
            AlertAppDialog dialog = new AlertAppDialog();
            dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
                @Override
                public void onConfirm() {
                    submitDeleteRecord();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            ToastUtils.showShort(getString(R.string.record_delete_select_hint));
        }
    }

    /**
     * 提交删除录音
     */
    private void submitDeleteRecord() {
        RecordDeletePutBean.ParamsBean paramsBean = new RecordDeletePutBean.ParamsBean();
        paramsBean.setSimei(mSimei);
        paramsBean.setRecordtime(deleteRecordTime);

        RecordDeletePutBean bean = new RecordDeletePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Record_Delete);
        bean.setModule(ModuleValueService.Module_For_Record_Delete);

        showProgressDialog();

        if (getPresenter() != null) {
            getPresenter().submitDeleteRecord(bean);
        }
    }

    /**
     * 下发短录音
     */
    private void submitShortRecord() {
        isRecording = true;

        RecordShortPutBean.ParamsBean paramsBean = new RecordShortPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);
        paramsBean.setTime_second(secondTime);

        RecordShortPutBean bean = new RecordShortPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Record_Short);
        bean.setModule(ModuleValueService.Module_For_Record_Short);

        showProgressDialog();

        if (getPresenter() != null) {
            getPresenter().submitShortRecord(bean);
        }
    }

    /**
     * 获取开始短录音结果
     */
    private void getShortRecordResult() {
        RecordShortResultPutBean.ParamsBean paramsBean = new RecordShortResultPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);

        RecordShortResultPutBean bean = new RecordShortResultPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Record_Short_Result);
        bean.setModule(ModuleValueService.Module_For_Record_Short_Result);

        if (getPresenter() != null) {
            getPresenter().getShortRecordResult(bean);
        }
    }

    /**
     * 获取短录音进度
     */
    private void getShortRecordProcess() {
        RecordSchedulePutBean.ParamsBean paramsBean = new RecordSchedulePutBean.ParamsBean();
        paramsBean.setSimei(mSimei);

        RecordSchedulePutBean bean = new RecordSchedulePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Record_Short_Schedule);
        bean.setModule(ModuleValueService.Module_For_Record_Short_Schedule);

        if (getPresenter() != null) {
            getPresenter().getRecordSchedule(bean);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handlerCallbackForHistory = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x0100:
                    //历史录音
                    RecordModel model = (RecordModel) msg.obj;
                    model.setDelete(isEditDelete);
                    recordDataList.add(0, model);
                    if (recyclerView != null) {
                        mAdapter.notifyDataSetChanged();
                        if (recordDataList.size() > 0) {
                            recyclerView.scrollToPosition(0);
                        }
                    }
                    break;
                case 0x1000:
                    //刷新录音下载成功
                    if (isRefreshNow) {
                        ToastUtils.showShort(getString(R.string.record_download_success));
                    }
                    onComputeDeleteCount();
                    onRecordPosition();
                    isRefreshNow = false;
//                    onGetDBData(2);
                    break;
                case 0x1100:
                    //刷新录音下载失败
                    if (isRefreshNow) {
                        ToastUtils.showShort(getString(R.string.download_record_fail));
                    }
                    isRefreshNow = false;
                    break;

            }
        }
    };

    /**
     * 定位历史录音数据最后一条的位置
     */
    private void onRecordPosition() {
        onListSort();
        if (recordDataList.size() > 0) {
            int position = 0;
            long recordTime = recordDataList.get(0).getRecordTime();
            for (int i = 0; i < recordDataList.size(); i++) {
                if (recordTime == recordDataList.get(i).getRecordTime()) {
                    position = i;
                    break;
                }
            }
            mAdapter.notifyDataSetChanged();
            int finalPosition = position;
            btnRecord.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(finalPosition);
                    }
                }
            }, 100);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handlerCallback = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x0200:
                    //实时录音
                    RecordModel model = (RecordModel) msg.obj;
                    model.setDelete(isEditDelete);
                    recordDataList.add(model);
                    if (recyclerView != null) {
                        mAdapter.notifyDataSetChanged();
                        if (isFirstGetRecord) {
                            if (recordDataList.size() > 0) {
                                recyclerView.scrollToPosition(recordDataList.size() - 1);
                            }
                        }
                    }
                    break;
                case 0x2000:
                    //实时录音下载成功
                    if (isFirstGetRecord) {
                        ToastUtils.showShort(getString(R.string.record_download_success));
                    }
                    onComputeDeleteCount();
                    isFirstGetRecord = false;
                    isRecordActivity = false;
                    break;
                case 0x2100:
                    //实时录音下载失败
                    isFirstGetRecord = false;
                    isRecordActivity = false;
                    break;
            }
        }
    };

    @Override
    public void getRecordConfigSuccess(RecordConfigResultBean recordConfigResultBean) {
        isExpire = recordConfigResultBean.isExpire();
        isRecordSupport = recordConfigResultBean.getRecord();
        isVorswitchSupport = recordConfigResultBean.getVorswitch();
    }

    @Override
    public void getRecordDataSuccess(RecordResultBean recordResultBean, boolean isHistoryData) {
        recordTotal = recordResultBean.getTotal();
        listFile.clear();
        if (isHistoryData) {
            if (recordResultBean.getData() != null) {
                for (int i = 0; i < recordResultBean.getData().size(); i++) {
                    RecordResultBean.DataBean bean = recordResultBean.getData().get(i);
                    String url = Api.APP_DOMAIN + "/mapi?sid=" + ConstantValue.getApiUrlSid() + "&func=" + ModuleValueService.Fuc_For_Record_DownLoad
                            + "&module=" + ModuleValueService.Module_For_Record + "&media_id=" + bean.getUrl();
                    if (!TextUtils.isEmpty(mSimei)) {
                        url = url + "&simei=" + mSimei;
                    }
                    bean.setUrl(url);
                    listFile.add(bean);
                }
            }

            if (listFile.size() > 0) {
                if (historyTimeModel != null) {
                    onDeleteRecordTimeForDB(historyTimeModel);
                }
                if (historyRecordDownload != null) {
                    historyRecordDownload.cancelDownload();
                    historyRecordDownload = null;
                }
                historyRecordDownload = new RecordDownload(this, mImei, handlerCallbackForHistory, mRecordBeanDao, mRecordTimeBeanDao);
                historyRecordDownload.setRecordData(listFile, true, isRecordActivity, recordTotal, limitSize);
            } else {
                isRefreshNow = false;
                ToastUtils.showShort(getString(R.string.no_more_record_data));
            }
        } else {
            if (recordResultBean.getData() != null) {
                for (int i = recordResultBean.getData().size() - 1; i >= 0; i--) {
                    RecordResultBean.DataBean bean = recordResultBean.getData().get(i);
                    String url = Api.APP_DOMAIN + "/mapi?sid=" + ConstantValue.getApiUrlSid() + "&func=" + ModuleValueService.Fuc_For_Record_DownLoad
                            + "&module=" + ModuleValueService.Module_For_Record + "&media_id=" + bean.getUrl();
                    if (!TextUtils.isEmpty(mSimei)) {
                        url = url + "&simei=" + mSimei;
                    }
                    bean.setUrl(url);
                    listFile.add(bean);
                }
            }
            if (listFile.size() > 0) {
                if (realTimeRecordDownload != null) {
                    realTimeRecordDownload.cancelDownload();
                    realTimeRecordDownload = null;
                }
                realTimeRecordDownload = new RecordDownload(this, mImei, handlerCallback, mRecordBeanDao, mRecordTimeBeanDao);
                realTimeRecordDownload.setRecordData(listFile, false, isRecordActivity, recordTotal, limitSize);
            } else {
                isFirstGetRecord = false;
                isRecordActivity = false;
            }
        }

        // 处理服务器和设备录音异常问题
        if (recordResultBean.getState() == -1 || recordResultBean.getState() == 4) {
            handlerStartRecordResult.removeCallbacksAndMessages(null);
            mShortTimeMax = 0;
            isRecordEnabled = false;
            onRecordStateIsEnabled();
            isRealTimeRecording = false;
            isRecording = false;
            updateRecordState();
            isVorswitchSupport = 0;
            if (requestOnTimer != null) {
                requestOnTimer.cancel();
                requestOnTimer = null;
            }
        }
    }

    @Override
    public void finishRefresh() {
        srlView.setRefreshing(false);
    }

    @Override
    public void dismissDialog() {
        dismissProgressDialog();
    }

    @Override
    public void submitShortRecordSuccess(BaseBean baseBean) {
        if (baseBean.isSuccess()) {
            isRecordEnabled = true;
            onRecordStateIsEnabled();
            handlerStartRecordResult.sendEmptyMessage(1);
        } else {
            mShortTimeMax = 0;
            isRecordEnabled = false;
            onRecordStateIsEnabled();
            isRealTimeRecording = false;
            isRecording = false;
            updateRecordState();
        }
    }

    @Override
    public void getShortRecordResultSuccess(RecordShortResultBean recordShortResultBean) {
// 0-成功 1-当前用户已开始录音 2-正在录音 但不是当前用户 3-该设备已开启声控录音 4-没有指令执行 5-指令未执行，6-实时录音开启
        if (recordShortResultBean.getState() == 4) {
            handlerStartRecordResult.removeCallbacksAndMessages(null);
            mShortTimeMax = 0;
            isRecordEnabled = false;
            onRecordStateIsEnabled();
            isRealTimeRecording = false;
            isRecording = false;
            updateRecordState();
            if (isFirstGetRecord || isVorswitchSupport == 1) {
                getRecordData(false);
            }
        } else if (recordShortResultBean.getState() == 5) {
            isRecording = true;
            if (mShortTimeMax >= mShortResultTimeMax) {
                handlerStartRecordResult.removeCallbacksAndMessages(null);
                mShortTimeMax = 0;
                isRealTimeRecording = false;
                isRecording = false;
                updateRecordState();
                ToastUtils.showShort(getString(R.string.recording_error));
            } else {
                isRecordEnabled = true;
                onRecordStateIsEnabled();
            }
        } else if (recordShortResultBean.getState() == 6) {
            handlerStartRecordResult.removeCallbacksAndMessages(null);
            mShortTimeMax = 0;
            isRealTimeRecording = true;
            if (isFirstGetRecord || isVorswitchSupport == 1) {
                getRecordData(false);
            }
        } else {
            handlerStartRecordResult.removeCallbacksAndMessages(null);
            mShortTimeMax = 0;
            if (recordShortResultBean.getState() == 0) {
                //短录音开启成功，关闭定时获取录音定时器
                if (requestOnTimer != null) {
                    requestOnTimer.cancel();
                    requestOnTimer = null;
                }

                isRealTimeRecording = false;
                isRecording = true;
                updateRecordState();
                shortRecordWaitTime = System.currentTimeMillis();
                mShortCountDown = mShortCountDownTime;
                handlerShort.sendEmptyMessage(1);
            } else {
                if (isShowTip) {
                    ToastUtils.showShort(getString(R.string.recording_now));
                }
                isRealTimeRecording = false;
                isRecording = false;
                updateRecordState();
                isRecordEnabled = false;
                onRecordStateIsEnabled();
                if (isFirstGetRecord || isVorswitchSupport == 1) {
                    getRecordData(false);
                }
            }
        }
        isShowTip = true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getRecordScheduleSuccess(RecordScheduleResultBean recordScheduleResultBean) {
        if (TextUtils.isEmpty(recordScheduleResultBean.getSchedule()) || recordScheduleResultBean.getSchedule().equals("-2")) {
            handlerShort.removeCallbacksAndMessages(null);
            isRealTimeRecording = false;
            isRecording = false;
            updateRecordState();
            onRecordScheduleError(1);
            return;
        }
        if (recordScheduleResultBean.getSchedule().equals("100")) {
            //录音文件上传完成 下载短录音文件
            isRealTimeRecording = false;
            isRecording = false;
            updateRecordState();
            handlerShort.removeCallbacksAndMessages(null);
            isFirstGetRecord = true;
            getRecordData(false);
        } else {
            // 判断获取的进度值跟上一次存储的进度值是否一样
            if (!mSchedule.equals(recordScheduleResultBean.getSchedule())) {
                mSchedule = recordScheduleResultBean.getSchedule(); // 重置进度值
                timeOutTwo = 120; // 重置倒计时时间
                shortRecordWaitTime = System.currentTimeMillis(); // 重置下发成功时间
            }

            if ((System.currentTimeMillis() - shortRecordWaitTime) > (timeOutTwo * 1000)) {
                handlerShort.removeCallbacksAndMessages(null);
                isRealTimeRecording = false;
                isRecording = false;
                updateRecordState();
                onRecordScheduleError(1);
            } else {
                if (recordScheduleResultBean.getSchedule().equals("-1") || recordScheduleResultBean.getSchedule().equals("0")) {
                    btnRecord.setText(getString(R.string.recording));
                } else {
                    btnRecord.setText(getString(R.string.record_process) + recordScheduleResultBean.getSchedule() + "%");
                }
            }
        }
    }

    @Override
    public void submitDeleteRecordSuccess(BaseBean baseBean) {
        ToastUtils.showShort(getString(R.string.delete_success));
        tvDelete.setText(getString(R.string.delete));
        tvSelectAll.setText(getString(R.string.select_all));
        if (deleteBeans.size() > deleteMaxNumber) {
            deleteTotalNumber = deleteBeans.size();
            deleteNumber = 0;
            taskProgressDialog = null;
            taskProgressDialog = new RecordTaskProgressDialog();
            taskProgressDialog.show(getSupportFragmentManager(), new RecordTaskProgressDialog.onProgressBarChange() {
                @Override
                public void onTaskProgressFinish() {
                    deleteRecordHandler.removeCallbacksAndMessages(null);
                    onRefreshData();
                }
            });
            deleteRecordHandler.sendEmptyMessage(1);
        } else {
            onDeleteRecordForDB(deleteBeans);
            onRefreshData();
        }
    }

    /**
     * 删除之后重置数据
     */
    private void onRefreshData() {
        if (isAllSelect) {
            recordDataList.clear();
        } else {
            recordDataList.removeAll(deleteBeans);
        }
        mAdapter.notifyDataSetChanged();
        mSelectCount = 0;
        isAllSelect = false;
    }

    @SuppressLint("HandlerLeak")
    private Handler deleteRecordHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            onDeleteRecordProgress();
        }
    };

    /**
     * 批量删除
     */
    private void onDeleteRecordProgress() {
        deleteRecordProgress.clear();
        for (int i = 0; i < deleteBeans.size(); i++) {
            deleteRecordProgress.add(deleteBeans.get(i));
            if (i > deleteMaxNumber) {
                break;
            }
        }
        deleteBeans.removeAll(deleteRecordProgress);
        recordDataList.removeAll(deleteRecordProgress);
        deleteNumber = deleteNumber + deleteRecordProgress.size();
        onDeleteRecordForDB(deleteRecordProgress);
        if (taskProgressDialog != null) {
            double progress = ((double) deleteNumber / deleteTotalNumber) * 100;
            int intProgress = (int) progress;
            boolean isFinish = intProgress >= 100;
            if (intProgress > 100) {
                intProgress = 100;
            }
            taskProgressDialog.setProgressBar(intProgress, isFinish);
        }
        deleteRecordHandler.sendEmptyMessageDelayed(1, 1000);
    }

    /**
     * 录音错误，弹出提示
     *
     * @param type 0：还未开始录就已经失败  1：已经录了一部分了，发生未知错误
     */
    private void onRecordScheduleError(int type) {
        // 获取不完整的录音数据
        getRecordData(false);

        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.record_schedule_error));
        bean.setConfirmTip(getString(R.string.confirm));
        bean.setCancelTip(getString(R.string.cancel));
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    // ------------------- 分割线：发起动态权限申请  ------------------------

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        ArrayList<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private ArrayList<String> findDeniedPermissions(String[] permissions) {
        ArrayList<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                isNeedCheck = true;
            } else {
                isNeedCheck = false;
            }
            if (isShowAuth && isNeedCheck) {
                showMissingPermissionDialog();
            }
            isShowAuth = false;
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip);
        builder.setMessage(R.string.not_permission);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }



    /**
     * 注册距离感应器监听器，监测用户是否靠近手机听筒
     */
    public void registerProximitySensorListener() {
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        if (mSensorManager == null) {
            return;
        }
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mAudioManager == null) {
            return;
        }
        if (isHeadphonesPlugged()) {
            // 如果耳机已插入，设置距离传感器失效
            return;
        }

        if (MediaManager.mPlayer.isPlaying()) {
            // 如果音频正在播放
            float distance = event.values[0];
            if (distance >= sensor.getMaximumRange()) {
                // 用户远离听筒，音频外放，亮屏
                changeToSpeaker();
            } else {
                MediaManager.mPlayer.start();
                // 用户贴近听筒，切换音频到听筒输出，并且熄屏防误触
                changeToReceiver();
                mAudioManager.setSpeakerphoneOn(false);
            }
        } else {
            // 音频播放完了
            changeToSpeaker();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private boolean isHeadphonesPlugged() {
        if (mAudioManager == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AudioDeviceInfo[] audioDevices = mAudioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for (AudioDeviceInfo deviceInfo : audioDevices) {
                if (deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                        || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                    return true;
                }
            }
            return false;
        } else {
            return mAudioManager.isWiredHeadsetOn();
        }
    }


    /**
     * 切换到外放
     */
    public void changeToSpeaker() {
        setScreenOn();
        if (mAudioManager == null) {
            return;
        }
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切换到耳机模式
     */
    public void changeToHeadset() {
        if (mAudioManager == null) {
            return;
        }
        mAudioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到听筒
     */
    public void changeToReceiver() {
        setScreenOff();
        if (mAudioManager == null) {
            return;
        }
        mAudioManager.setSpeakerphoneOn(false);
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void setScreenOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (wakeLock == null) {
                wakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, TAG);
            }
            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        }
    }

    private void setScreenOn() {
        if (wakeLock != null) {
            wakeLock.setReferenceCounted(false);
            wakeLock.release();
            wakeLock = null;
        }
    }
}
