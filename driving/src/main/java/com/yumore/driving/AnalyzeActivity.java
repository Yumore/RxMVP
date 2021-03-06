package com.yumore.driving;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.yumore.driving.adapter.TopicAdapter;
import com.yumore.driving.bean.AnwerInfo;
import com.yumore.driving.fragment.ReadFragment;
import com.yumore.driving.view.ReaderViewPager;
import com.yumore.sticky.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AnalyzeActivity extends AppCompatActivity {

    private SlidingUpPanelLayout mLayout;
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;
    private ImageView shadowView;
    private ReaderViewPager readerViewPager;
    private List<AnwerInfo.DataBean.SubDataBean> datas;
    private int prePosition2;
    private int curPosition2;
    private int prePosition;
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);


        initSlidingUoPanel();

        initList();

        ImageView imageView = findViewById(R.id.common_header_back_iv);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener((view) -> {
            finish();
        });
        TextView textView = findViewById(R.id.common_header_title_tv);
        textView.setText("题目解析");
        AnwerInfo anwerInfo = getAnwer();

        datas = anwerInfo.getData().getData();
        Log.i("data.size=", "" + datas.size());

        if (topicAdapter != null) {
            topicAdapter.setDataNum(datas.size());
        }

        initReadViewPager();

        Button bt_pre = findViewById(R.id.bt_pre);
        Button bt_next = findViewById(R.id.bt_next);

        bt_pre.setOnClickListener(v -> {
            int currentItem = readerViewPager.getCurrentItem();
            currentItem = currentItem - 1;
            if (currentItem > datas.size() - 1) {
                currentItem = datas.size() - 1;
            }
            readerViewPager.setCurrentItem(currentItem, true);
        });

        bt_next.setOnClickListener(v -> {
            int currentItem = readerViewPager.getCurrentItem();
            currentItem = currentItem + 1;
            if (currentItem < 0) {
                currentItem = 0;
            }
            readerViewPager.setCurrentItem(currentItem, true);
        });
    }

    private void initReadViewPager() {
        shadowView = findViewById(R.id.shadowView);
        readerViewPager = findViewById(R.id.readerViewPager);


        readerViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(position);

                return ReadFragment.newInstance(subDataBean);
            }

            @Override
            public int getCount() {
                return datas.size();
            }
        });


        readerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                shadowView.setTranslationX(readerViewPager.getWidth() - positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                curPosition2 = position;
                topicAdapter.notifyCurPosition(curPosition2);
                topicAdapter.notifyPrePosition(prePosition2);

                prePosition2 = curPosition2;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initList() {
        recyclerView = findViewById(R.id.list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);

        topicAdapter = new TopicAdapter(this);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(topicAdapter);


        topicAdapter.setOnTopicClickListener((holder, position) -> {
            curPosition = position;
            Log.i("点击了==>", position + "");
            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }

            readerViewPager.setCurrentItem(position);


            topicAdapter.notifyCurPosition(curPosition);
            topicAdapter.notifyPrePosition(prePosition);

            prePosition = curPosition;
        });


    }

    private void initSlidingUoPanel() {
        mLayout = findViewById(R.id.sliding_layout);

        int height = getWindowManager().getDefaultDisplay().getHeight();

        LinearLayout dragView = findViewById(R.id.dragView);
        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * 0.8f));
        dragView.setLayoutParams(params);


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("", "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(view -> mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED));
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    private AnwerInfo getAnwer() {

        try {
            InputStream in = getAssets().open("anwer.json");
            AnwerInfo anwerInfo = JSON.parseObject(inputStream2String(in), AnwerInfo.class);

            return anwerInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }

        return null;
    }
}
