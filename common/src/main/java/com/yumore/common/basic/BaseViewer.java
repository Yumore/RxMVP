package com.yumore.common.basic;

import android.content.Context;
import android.os.Bundle;

/**
 * BaseView
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 2018/3/8 - 15:08
 */
public interface BaseViewer {
    /**
     * 获取当前页面的context对象
     *
     * @return Context
     */
    Context getContext();

    /**
     * 在初始化之前调用
     *
     * @param savedInstanceState savedInstanceState
     */
    void beforeInit(Bundle savedInstanceState);

    /**
     * 初始化
     */
    void initialize();

    /**
     * 获取当前页面的布局id
     *
     * @return layout id
     */
    int getLayoutId();

    /**
     * 初始化控件
     */
    void initView();

    /**
     * 加载数据
     */
    void loadData();

    /**
     * 给view绑定事件
     */
    void bindView();

    /**
     * 展示dialog
     *
     * @param message message
     */
    void showLoading(String message);

    /**
     * 取消dialog
     */
    void dismissLoading();

    /**
     * 展示错误提示信息
     *
     * @param message message
     */
    void showMessage(String message);

    /**
     * 没有更多
     *
     * @param withoutMore withoutMore
     */

    void withoutMore(boolean withoutMore);
}
