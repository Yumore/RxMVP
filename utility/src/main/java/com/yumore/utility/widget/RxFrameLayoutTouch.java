package com.yumore.utility.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author yumore
 * @date 2018/3/5
 * 解决滑动冲突
 */

public class RxFrameLayoutTouch extends FrameLayout {

    public RxFrameLayoutTouch(Context context) {
        super(context);
    }

    public RxFrameLayoutTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxFrameLayoutTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //拦截父类事件
            getParent().requestDisallowInterceptTouchEvent(true);

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }

}
