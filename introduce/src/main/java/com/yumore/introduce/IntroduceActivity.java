package com.yumore.introduce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.provider.ISampleProvider;
import com.yumore.provider.RouterConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * @author nathaniel
 */
@Route(path = RouterConstants.INTRODUCE_HOME)
public class IntroduceActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final int[] IMAGE_RESOURCES = {
            R.drawable.icon_indicator_00,
            R.drawable.icon_indicator_01,
            R.drawable.icon_indicator_02,
            R.drawable.icon_indicator_03,
            R.drawable.icon_indicator_04,
            R.drawable.icon_indicator_05,
            R.drawable.icon_indicator_06,
            R.drawable.icon_indicator_07,
            R.drawable.icon_indicator_08,
            R.drawable.icon_indicator_09,
            R.drawable.icon_indicator_10,
            R.drawable.icon_indicator_11,
            R.drawable.icon_indicator_12,
            R.drawable.icon_indicator_13
    };
    private static final int HANDLER_MESSAGE = 1, DELAY_MILLIS = 5 * 1000;
    private static final int DOT_SIZE = 8, DOT_MARGIN = 16;
    private List<Fragment> fragmentList;
    private TextView textView;
    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams normalParams, focusParams;
    private Handler handler;
    private int currentPosition;
    private ViewPager viewPager;
    private int[] imageResources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        viewPager = findViewById(R.id.traction_viewPager_vp);
        linearLayout = findViewById(R.id.traction_dots_layout);
        fragmentList = new ArrayList<>();
        imageResources = IMAGE_RESOURCES;
        for (int imageResource : imageResources) {
            fragmentList.add(IntroduceFragment.newInstance(imageResource));
        }
        FragmentAdapter tractionAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(tractionAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
        normalParams = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), DOT_SIZE), dip2px(getApplicationContext(), DOT_SIZE));
        normalParams.leftMargin = dip2px(getApplicationContext(), DOT_SIZE);
        focusParams = new LinearLayout.LayoutParams(dip2px(getApplicationContext(), DOT_MARGIN), dip2px(getApplicationContext(), DOT_SIZE));
        focusParams.leftMargin = dip2px(getApplicationContext(), DOT_SIZE);
        View dotView;
        for (int i = 0; i < imageResources.length; i++) {
            dotView = new View(this);
            if (i == 0) {
                dotView.setLayoutParams(focusParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_focus);
            } else {
                dotView.setLayoutParams(normalParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_normal);
            }
            linearLayout.addView(dotView);
        }
        linearLayout.setGravity(Gravity.CENTER);
        viewPager.addOnPageChangeListener(this);
        textView = findViewById(R.id.enter_button_tv);
        textView.setOnClickListener(this);
        initHandler();
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE, DELAY_MILLIS);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message message) {
                if (message.what == 1) {
                    if (currentPosition == fragmentList.size() - 1) {
                        currentPosition = 0;
                        viewPager.setCurrentItem(0, false);
                    } else {
                        currentPosition++;
                        viewPager.setCurrentItem(currentPosition, true);
                    }
                    handler.sendEmptyMessageDelayed(HANDLER_MESSAGE, DELAY_MILLIS);
                } else {
                    super.handleMessage(message);
                }
            }
        };
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View dotView = linearLayout.getChildAt(i);
            if (i == position) {
                dotView.setLayoutParams(focusParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_focus);
            } else {
                dotView.setLayoutParams(normalParams);
                dotView.setBackgroundResource(R.drawable.icon_dot_normal);
            }

        }
        textView.setVisibility(position == imageResources.length - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View view) {
        ISampleProvider sampleProvider = ARouter.getInstance().navigation(ISampleProvider.class);
        sampleProvider.setIntroduceEnable(true);
        ARouter.getInstance().build(RouterConstants.TRACTION_HOME).navigation();
        finish();
    }
}
