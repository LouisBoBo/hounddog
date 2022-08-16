package com.slxk.hounddog.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.MapDownloadModel;
import com.slxk.hounddog.db.MapXYModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerOfflineMapComponent;
import com.slxk.hounddog.mvp.contract.OfflineMapContract;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.presenter.OfflineMapPresenter;
import com.slxk.hounddog.mvp.ui.adapter.OfflineMapAdapter;
import com.slxk.hounddog.mvp.utils.OfflineMapDownLoadUtil;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 离线地图首页
 * <p>
 * Created by MVPArmsTemplate on 12/24/2021 09:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class OfflineMapActivity extends BaseActivity<OfflineMapPresenter> implements OfflineMapContract.View, OfflineMapAdapter.onOfflineMapChange {

    @BindView(R.id.toolbar_iv_right)
    ImageView toolbarIvRight;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.ll_not_offline_map)
    LinearLayout llNotOfflineMap;

    private ArrayList<MapXYModel> mMapXYModels;
    private ArrayList<MapDownloadModel> mMapDownloadModels;
    private OfflineMapAdapter mAdapter;

    private static final int Intent_Map_Download_Select = 1;

    private MyDao mMapDownloadDao;
    private MyDao mMapXYDao;
    private OfflineMapDownLoadUtil mMapDownLoadUtil;
    private String mDownloadTime = ""; // 当前下载任务的时间戳，判断哪个任务正在进行
    private MapDownloadModel mMapSelectModel; // 选中的任务
    private int mSelectPosition = 0; // 选中任务的索引位置
    private String mAllMapNames = ""; // 所有地图的名称，用,号隔开，用来避免出现重复名称的
    private Handler mHandler; // 用于循环

    public static Intent newInstance() {
        return new Intent(MyApplication.getMyApp(), OfflineMapActivity.class);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOfflineMapComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_offline_map;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.offline_map));
        toolbarIvRight.setVisibility(View.VISIBLE);
        toolbarIvRight.setImageResource(R.drawable.icon_add);
        mMapXYModels = new ArrayList<>();
        mMapDownloadModels = new ArrayList<>();
        try{
            mMapDownloadDao = new MyDao(MapDownloadModel.class);
            mMapXYDao = new MyDao(MapXYModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        mHandler = new Handler();

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new OfflineMapAdapter(R.layout.item_offline_map, mMapDownloadModels);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOfflineMapChange(this);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rl_download_progress){
                    if (Utils.isButtonQuickClick()){
                        if (mMapDownloadModels.get(position).isDownload()){
                            onStopMapDownload();
                        }else{
                            if (mMapDownloadModels.get(position).getProgress() < 100){
                                mSelectPosition = position;
                                onStartMapDownload(position);
                            }
                        }
                    }
                }
            }
        });

        getMapDownloadForDB(false, "");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initMapDownloadUtil();
            }
        }, 100);
    }

    /**
     * 初始化下载工具类
     */
    private void initMapDownloadUtil(){
        mMapDownLoadUtil = OfflineMapDownLoadUtil.getInstance();
        mMapDownLoadUtil.initMapDownLoad();
        mMapDownLoadUtil.setMapDownloadChange(new OfflineMapDownLoadUtil.onMapDownloadChange() {

            @Override
            public void onDownloadProgress(int downloadIndex, int progress) {
                onResetDownloadNowUI(downloadIndex, progress, false);
            }

            @Override
            public void onSynchronizeProgress(String downloadTime) {
                mDownloadTime = downloadTime;
                onScreenDownloadIndex();
            }

            @Override
            public void onDownloadSuccess(MapDownloadModel model) {
                onResetDownloadNowUI(model.getPosition(), model.getProgress(), true);
                mDownloadTime = "";
            }
        });
        mMapDownLoadUtil.onSynchronizeInfo();
    }

    /**
     * 获取下载任务
     * @param isStartDownload 是否开始自动下载
     * @param time 需要开启下载任务的时间
     */
    private void getMapDownloadForDB(boolean isStartDownload, String time){
        mMapDownloadModels.clear();
        if (getMapDownloadDataBase() != null){
            mMapDownloadModels.addAll(getMapDownloadDataBase());
        }
        mAdapter.notifyDataSetChanged();
        onShowDownloadMap();
        if (isStartDownload){
            onScreenDownloadMap(time);
        }
    }

    /**
     * 从数据库get数据-下载任务
     *
     * @return
     */
    private List<MapDownloadModel> getMapDownloadDataBase() {
        try {
            return (List<MapDownloadModel>) mMapDownloadDao.queryAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断从地图选择页回来之后，是否启动自动下载功能
     */
    private void onScreenDownloadMap(String time){
        if (mMapDownloadModels.size() > 0){
            if (mMapDownloadModels.size() == 1){
                mSelectPosition = 0;
                mMapSelectModel = mMapDownloadModels.get(0);
                mDownloadTime = mMapDownloadModels.get(0).getTime();
            }else{
                if (TextUtils.isEmpty(mDownloadTime)){
                    mDownloadTime = time;
                    for (int i = 0; i < mMapDownloadModels.size(); i++){
                        MapDownloadModel bean = mMapDownloadModels.get(i);
                        if (bean.getTime().equals(mDownloadTime)){
                            mSelectPosition = i;
                            mMapSelectModel = mMapDownloadModels.get(i);
                            break;
                        }
                    }
                }else{
                    mMapSelectModel = mMapDownloadModels.get(mSelectPosition);
                    mDownloadTime = mMapDownloadModels.get(mSelectPosition).getTime();
                }
            }
            if (mMapSelectModel != null){
                onStartMapDownload(mSelectPosition);
            }
            for (MapDownloadModel model : mMapDownloadModels){
                model.setDownload(false);
            }
            mMapDownloadModels.get(mSelectPosition).setDownload(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取下载的图片的xy坐标系
     */
    private void getMapXYModelForDB(){
        mMapXYModels.clear();
        if (getMapXYModelDataBase() != null){
            mMapXYModels.addAll(getMapXYModelDataBase());
            mDownloadTime = mMapSelectModel.getTime();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMapDownLoadUtil.startMapDownload(mMapXYModels, mMapSelectModel);
                }
            }, 100);
        }
    }

    /**
     * 从数据库get数据-下载任务
     *
     * @return
     */
    private List<MapXYModel> getMapXYModelDataBase() {
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put(MapXYModel.TIME, mMapSelectModel.getTime());
            return (List<MapXYModel>) mMapXYDao.query(mMapXYDao.queryBuilder(map1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDeleteMap(MapDownloadModel model) {
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.delete_map_download) + "(" + model.getMap_name() + ")");
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                submitDeleteMap(model);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void submitDeleteMap(MapDownloadModel model){
        try{
            mMapDownloadDao.delete(model);
            mMapXYDao.deleteMapXYData(model.getTime());
            mMapDownloadModels.remove(model);
            mAdapter.notifyDataSetChanged();
            if (model.isDownload()){
                onStopMapDownload();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        onShowDownloadMap();
        onScreenDownloadIndex();
    }

    /**
     * 显示下载地图UI
     */
    private void onShowDownloadMap(){
        if (mMapDownloadModels.size() > 0){
            llNotOfflineMap.setVisibility(View.GONE);
        }else{
            llNotOfflineMap.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重新定位正在下载的地图的索引位置
     */
    private void onScreenDownloadIndex(){
        mSelectPosition = -1;
        if (!TextUtils.isEmpty(mDownloadTime)){
            for (int i = 0; i < mMapDownloadModels.size(); i++){
                MapDownloadModel bean = mMapDownloadModels.get(i);
                if (bean.getTime().equals(mDownloadTime)){
                    mSelectPosition = i;
                    break;
                }
            }
        }
        if (mSelectPosition != -1){
            onStartMapDownload(mSelectPosition);
        }
    }

    /**
     * 开始下载任务
     * @param position
     */
    private void onStartMapDownload(int position){
        if (recyclerview != null){
            for (MapDownloadModel model : mMapDownloadModels){
                model.setDownload(false);
            }
            mMapDownloadModels.get(position).setDownload(true);
            mAdapter.notifyDataSetChanged();
            mMapSelectModel = mMapDownloadModels.get(position);
        }
        // 停止之前的下载任务，开始新的下载任务
        mMapDownLoadUtil.stopMapDownload();
        getMapXYModelForDB();
    }

    /**
     * 停止下载任务
     */
    private void onStopMapDownload(){
        mMapDownLoadUtil.stopMapDownload();
        if (recyclerview != null){
            mMapSelectModel.setDownload(false);
            for (MapDownloadModel model : mMapDownloadModels){
                model.setDownload(false);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新当前下载任务的UI
     * @param downloadIndex 下载位置
     * @param progress 下载进度
     */
    private void onResetDownloadNowUI(int downloadIndex, int progress, boolean isComplete){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onResetUI(downloadIndex, progress, isComplete);
            }
        });
    }

    private void onResetUI(int downloadIndex, int progress, boolean isComplete){
        if (recyclerview != null){
            if (mSelectPosition < mMapDownloadModels.size()){
                mMapDownloadModels.get(mSelectPosition).setPosition(downloadIndex);
                mMapDownloadModels.get(mSelectPosition).setProgress(progress);
                if (isComplete){
                    mMapDownloadModels.get(mSelectPosition).setDownload(false);
                }
                mAdapter.notifyItemChanged(mSelectPosition);
            }
        }
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

    @OnClick({R.id.toolbar_iv_right, R.id.iv_download, R.id.tv_download})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.toolbar_iv_right:
                case R.id.iv_download:
                case R.id.tv_download:
                    onMapDownloadSelect();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Intent_Map_Download_Select){
                if (data != null){
                    String time = data.getStringExtra("time");
                    getMapDownloadForDB(true, time);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onMapDownloadSelect(){
        mAllMapNames = "";
        for (MapDownloadModel model : mMapDownloadModels){
            if (TextUtils.isEmpty(mAllMapNames)){
                mAllMapNames = model.getMap_name();
            }else{
                mAllMapNames = mAllMapNames + "," + model.getMap_name();
            }
        }
        Intent intent = new Intent(this, OfflineMapDownLoadActivity.class);
        intent.putExtra("name", mAllMapNames);
        startActivityForResult(intent, Intent_Map_Download_Select);
    }

}
