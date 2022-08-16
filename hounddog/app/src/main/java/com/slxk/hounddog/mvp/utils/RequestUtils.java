package com.slxk.hounddog.mvp.utils;

import com.jess.arms.mvp.IListView;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.RxLifecycleUtils;
import com.slxk.hounddog.mvp.model.entity.BaseListBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

public class RequestUtils {
    public static <K, T extends Observable<K>> T request(T request, IView rootView) {
        Observable<K> compose = request.doOnSubscribe(disposable -> {
            rootView.showLoading();
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        rootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(rootView));
        return ((T) compose);
    }

    /**
     * 请求列表数据
     *
     * @param request
     * @param errorHandler
     * @param rootView
     * @param <K>
     * @param <T>
     */
    public static <K extends BaseListBean, T extends Observable<K>> void requestList(T request, RxErrorHandler errorHandler, IListView<K> rootView) {
        request.doOnSubscribe(disposable -> {
            rootView.showLoading();
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        rootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(rootView))
                .subscribe(new ErrorHandleSubscriber<K>(errorHandler) {
                    @Override
                    public void onNext(K data) {
                        rootView.finishRresh();
                        if (data.isSuccess()) {
                            if (data.getData().isIsLastPage()) {
                                rootView.endLoadMore();
                                rootView.setNoMore();
                            } else {
                                rootView.endLoadMore();//隐藏上拉加载更多的进度条
                            }
                            rootView.loadData(data);
                        } else {
                            rootView.showMessage(data.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        rootView.finishRresh();
                    }
                });
    }

    /**
     * 获取更多数据
     *
     * @param request
     * @param errorHandler
     * @param rootView
     * @param <K>
     * @param <T>
     */
    public static <K extends BaseListBean, T extends Observable<K>> void requestMoreList(T request, RxErrorHandler errorHandler, IListView<K> rootView, ListErrorListener listener) {
        request.doOnSubscribe(disposable -> {
            rootView.showLoading();
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        rootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(rootView))
                .subscribe(new ErrorHandleSubscriber<K>(errorHandler) {
                    @Override
                    public void onNext(K data) {
                        if (data.isSuccess()) {
                            if (data.getData() != null) {
                                if (data.getData() != null)
                                    rootView.loadMoreData(data);
                                if (data.getData().isIsLastPage()||data.getData().getSize()==0) {
                                    rootView.endLoadMore();
                                    rootView.setNoMore();
                                } else {
                                    rootView.endLoadMore();//隐藏上拉加载更多的进度条
                                }
                            } else {
                                rootView.endLoadFail();//隐藏上拉加载更多的进度条
                            }
                        } else {
                            rootView.endLoadFail();
//                            page--;
                            if (listener != null) {
                                listener.onError();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        rootView.endLoadFail();
//                        page--;
                        if (listener != null) {
                            listener.onError();
                        }
                    }
                });
    }

    public interface ListErrorListener {
        void onError();
    }


}
