package com.yumore.camera.presenter;


import androidx.fragment.app.Fragment;

/**
 * 预览画面的presenter
 *
 * @author CainHuang
 * @date 2019/7/3
 */
public class PreviewPresenter<T extends Fragment> extends IPresenter<T> {

    public PreviewPresenter(T target) {
        super(target);
    }

}
