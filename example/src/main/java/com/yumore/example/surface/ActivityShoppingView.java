package com.yumore.example.surface;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.utility.RxActivityTool;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.widget.RxShoppingView;
import com.yumore.utility.widget.RxTitle;

/**
 * @author yumore
 */
public class ActivityShoppingView extends ActivityBase {

    @BindView(R2.id.sv_1)
    RxShoppingView mSv1;
    @BindView(R2.id.sv_2)
    RxShoppingView mSv2;
    @BindView(R2.id.btn_take_out)
    Button mBtnTakeOut;
    @BindView(R2.id.activity_shopping_view)
    LinearLayout mActivityShoppingView;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_shopping_view);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);

    }

    @OnClick(R2.id.btn_take_out)
    public void onClick() {
        RxActivityTool.skipActivity(mContext, ActivityELMe.class);
    }
}
