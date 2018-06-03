package com.yumore.sample.mvp.view;

import com.yumore.frame.basic.BaseView;
import com.yumore.sample.entity.Story;

import java.util.List;

/**
 * @author Nathaniel
 * @version v1.0.0
 * @date 2018/3/8-15:55
 */

public interface TestView extends BaseView {
    /**
     * 设置数据
     *
     * @param dataList
     */
    void setData(List<Story> dataList);
}
