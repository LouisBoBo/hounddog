/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jess.arms.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.BarUtils;
import com.jess.arms.R;
import com.jess.arms.base.delegate.IActivity;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.ActivityLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.MyProgressDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.jess.arms.utils.ThirdViewUtil.convertAutoView;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@link Activity} 的三方库, 那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity}, 然后再按照 {@link BaseActivity} 的格式, 将代码复制过去, 记住一定要实现{@link IActivity}
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki">请配合官方 Wiki 文档学习本框架</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog">更新日志, 升级必看!</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/Issues">常见 Issues, 踩坑必看!</a>
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">MVPArms 官方组件化方案 ArmsComponent, 进阶指南!</a>
 * Created by JessYan on 22/03/2016
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;

    // 加载框
    private ProgressDialog rxDialogLoading;
    private ProgressBar progressBar;

    // 加载框
    private MyProgressDialog mProgressDialog;

    private boolean isSetting = false; // 是否设置沉浸式
    // 小米
    public static final String PHONE_XIAOMI = "XIAOMI";
    // 红米
    public static final String PHONE_REDMI = "REDMI";
    // 华为
    public static final String PHONE_HUAWEI = "HUAWEI";
    // 华为荣耀
    public static final String HONOR = "HONOR";

    public P getPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            //noinspection unchecked
            mCache = ArmsUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = convertAutoView(name, context, attrs);
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            if (e instanceof InflateException) {
                throw e;
            }
            e.printStackTrace();
        }
        initData(savedInstanceState);

        // 是否设置沉浸式
        if (isSetting) {
            setStatusBarLightMode();
        } else {
            // 设置状态栏为浅色模式，设置状态栏背景颜色为白色，就会变成白底黑字显示
            BarUtils.setStatusBarLightMode(getWindow(), true);
        }
    }

    /**
     * 判断是否显示了底部导航栏，兼容底部导航栏的高度
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ObsoleteSdkInt")
    public void setNavigationBarShow(){
        String brand = android.os.Build.BRAND.toUpperCase(); // 获取厂商名称
        // 兼容底部虚拟导航栏（全面屏）
        if (brand.contains(PHONE_XIAOMI) || brand.contains(PHONE_REDMI)) {//如果是小米
            if (checkNavigationBarShow(this, getWindow())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (Settings.Global.getInt(getContentResolver(), "force_fsg_nav_bar", 0) == 0) {//判断小米是否显示导航栏
                        View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                        rootView.setPadding(0, 0, 0, BarUtils.getNavBarHeight());
                    }
                }
            }

//                    if (isHasNavigationBar(this)) {
//                        View rootView = ((ViewGroup) findViewById(android.R.id.content));
//                        rootView.setPadding(0, 0, 0, BarUtils.getNavBarHeight());
//                    }
        } else if (brand.contains(PHONE_HUAWEI) || brand.contains(HONOR)) {//如果是华为
//                    if (isNavigationBarShow_HUIWEI(this)) {//判断华为机器是否显示导航栏
//                            View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//                            rootView.setPadding(0, 0, 0, BarUtils.getNavBarHeight());
//                    }else{
//                        View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//                        rootView.setPadding(0, 0, 0, 0);
//                    }
            if (isHasNavigationBar(this)) {
                View rootView = ((ViewGroup) findViewById(android.R.id.content));
                rootView.setPadding(0, 0, 0, BarUtils.getNavBarHeight());
            }
        }else{
            if (checkNavigationBarShow(this, getWindow())) {
                View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                rootView.setPadding(0, 0, 0, BarUtils.getNavBarHeight());
            }
        }
    }

    /**
     * 默认设置沉浸式
     *
     * @param isSet
     */
    public void setStatusBarSetting(boolean isSet) {
        isSetting = isSet;
    }

    /**
     * 是否设置沉浸式
     */
    private void setStatusBarLightMode() {
        if (isSetting) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ArmsUtils.statuInScreen(this);
                BarUtils.setStatusBarLightMode(getWindow(), true);
                //设置标题，兼容沉侵式状态栏高度
                View title = findViewById(R.id.toolbar);
                if (title != null) {
                    BarUtils.addMarginTopEqualStatusBarHeight(title);
                }
            }
        }
    }

    /**
     * 判断是否有显示虚拟导航栏
     * @param context
     * @param window
     * @return
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean checkNavigationBarShow(@NonNull Context context, @NonNull Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            boolean show;
            Display display = window.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);

            View decorView = window.getDecorView();
            Configuration conf = context.getResources().getConfiguration();
            if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
                View contentView = decorView.findViewById(android.R.id.content);
                show = (point.x != contentView.getWidth());
            } else {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                show = (rect.bottom != point.y);
            }
            return show;
        }
        return false;
    }

    /**
     * 判断是否有显示虚拟导航栏
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ObsoleteSdkInt")
    public static boolean isHasNavigationBar(Activity activity) {
        Point size = new Point();
        Point realSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity.getWindowManager().getDefaultDisplay().getRealSize(realSize);
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();

            Method mGetRawH = null;
            Method mGetRawW = null;

            int realWidth = 0;
            int realHeight = 0;

            try {
                mGetRawW = Display.class.getMethod("getRawWidth");
                mGetRawH = Display.class.getMethod("getRawHeight");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            realSize.set(realWidth, realHeight);
        }
        if (realSize.equals(size)) {
            return false;
        } else {
            size.y = size.y +  BarUtils.getNavBarHeight();
            if (realSize.y < size.y){
                return false;
            }
            return true;
        }
    }

    /**
     * 设置字体为默认大小，不随系统字体大小改而改变
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //非默认值
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 设置字体为默认大小，不随系统字体大小改而改变
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 显示加载对话框
     */
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    createProgressBar();
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    createProgressBar();
                    rxDialogLoading.show();
                }
            }
        });
    }

    public void hideLoading() {
        if (progressBar != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        if (rxDialogLoading != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rxDialogLoading.dismiss();
                }
            });
        }
    }

    private void createProgressBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && progressBar == null) {
            //整个Activity布局的最终父布局,参见参考资料
            FrameLayout rootFrameLayout = (FrameLayout) findViewById(android.R.id.content);
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            progressBar = new ProgressBar(getApplicationContext());
            progressBar.setLayoutParams(layoutParams);
            progressBar.setVisibility(View.VISIBLE);
//        ClipDrawable progressClip = new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)), Gravity.LEFT, ClipDrawable.HORIZONTAL);
            //Setup LayerDrawable and assign to progressBar

//        progressBar.setProgressDrawable(progressClip);
            int color = getResources().getColor(R.color.colorPrimary);
            ColorStateList colorStateList = ColorStateList.valueOf(color);
            progressBar.setIndeterminateTintList(colorStateList);
            progressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
//        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading));
//        progressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
            rootFrameLayout.addView(progressBar);
        } else {
            rxDialogLoading = new ProgressDialog(this);
        }
    }

    /**
     * 显示加载对话框
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new MyProgressDialog(this, R.style.ProgressDialogStyle);
        }
        mProgressDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxDialogLoading != null) {
            rxDialogLoading.dismiss();
            rxDialogLoading = null;
        }
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }
        this.mPresenter = null;

        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 {@code true} (默认为 {@code true}), Arms 会自动注册 EventBus
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个 {@link Activity} 是否会使用 {@link Fragment}, 框架会根据这个属性判断是否注册 {@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回 {@code false}, 那意味着这个 {@link Activity} 不需要绑定 {@link Fragment}, 那你再在这个 {@link Activity} 中绑定继承于 {@link BaseFragment} 的 {@link Fragment} 将不起任何作用
     *
     * @return 返回 {@code true} (默认为 {@code true}), 则需要使用 {@link Fragment}
     */
    @Override
    public boolean useFragment() {
        return true;
    }

    /**
     * 收起软键盘
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
