package com.yumore.driving;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yumore.driving.adapter.TopicAdapter;
import com.yumore.driving.bean.AnwerInfo;
import com.yumore.driving.bean.QuestionEntry;
import com.yumore.driving.view.FlipperLayout;
import com.yumore.provider.utility.EmptyUtils;
import com.yumore.sticky.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity implements FlipperLayout.OnSlidePageListener {

    private SlidingUpPanelLayout mLayout;
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;
    private List<AnwerInfo.DataBean.SubDataBean> datas;
    private FlipperLayout rootLayout;
    private int index = 1;
    private int prePosition;
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ImageView imageView = findViewById(R.id.common_header_back_iv);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(view -> {
            finish();
        });

        TextView tv_title = findViewById(R.id.common_header_title_tv);
        tv_title.setText("答题");
        tv_title.setOnClickListener(v -> {
            try {
                InputStream inputStream = getAssets().open("test_1.json");
                List<QuestionEntry> questionEntries = JSON.parseArray(inputStream2String(inputStream), QuestionEntry.class);
                int size = questionEntries.size();
                Log.i("AA", size + "");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("data.size=", e.toString());
            }
        });

        findViewById(R.id.bt_next).setOnClickListener(v -> rootLayout.autoNextPage());

        findViewById(R.id.bt_pre).setOnClickListener(v -> rootLayout.autoPrePage());

        findViewById(R.id.subject_finish_tv).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), OutcomeActivity.class));
        });
        AnwerInfo anwerInfo = getAnwer();
        if (EmptyUtils.isEmpty(anwerInfo)) {
            datas = new ArrayList<>();
        } else {
            datas = anwerInfo.getData().getData();
        }
        initPage();
        initSlidingUoPanel();
        initList();
    }

    private void initPage() {
        if (datas.size() > 0) {
            rootLayout = findViewById(R.id.container);
            rootLayout.removeAllViews();
            rootLayout.setIndex(1);


            View recoverView = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);
            View view1 = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);
            View view2 = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);

            rootLayout.initFlipperViews(SubjectActivity.this, view2, view1, recoverView);

            final TextView readView1 = view1.findViewById(R.id.tv_anwer);
            final TextView readView2 = view2.findViewById(R.id.tv_anwer);

            if (datas.size() == 1) {
                //填充第一页的文本
                setText(readView1, datas.get(0));
            } else if (datas.size() >= 2) {
                //填充第一页的文本
                setText(readView1, datas.get(0));

                //填充第二页的文本
                setText(readView2, datas.get(1));
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void setText(TextView textView, AnwerInfo.DataBean.SubDataBean subDataBean) {
        textView.setText(subDataBean.getQuestionid() + ". " + subDataBean.getQuestion()
                + "\n\nA." + subDataBean.getOptiona()
                + "\nB." + subDataBean.getOptionb()
                + "\nC." + subDataBean.getOptionc()
                + "\nD." + subDataBean.getOptiond()
                + "\n\n\n答案解析：" + subDataBean.getExplain()
        );
    }

    private void initLastPage() {

        rootLayout = findViewById(R.id.container);
        rootLayout.removeAllViews();
        int position = datas.size() - 1;
        rootLayout.setIndex(position + 1);

        View recoverView = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);
        View view1 = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);
        View view2 = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);

        rootLayout.initFlipperViews(SubjectActivity.this, view2, view1, recoverView);

        final TextView recoverReadView = recoverView.findViewById(R.id.tv_anwer);
        final TextView readView1 = view1.findViewById(R.id.tv_anwer);
        final TextView readView2 = view2.findViewById(R.id.tv_anwer);

        AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(position - 1);

        //填充第一页的文本
        setText(recoverReadView, subDataBean);

        subDataBean = datas.get(position);
        //填充最后一页一页的文本
        setText(readView1, subDataBean);


    }

    @Override
    public View createView(int direction, int index) {
        Log.i("createView-index=", index + "");
        View newView = null;
        if (direction == FlipperLayout.OnSlidePageListener.MOVE_TO_LEFT && index < datas.size()) { //下一页
            AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(index);

            newView = LayoutInflater.from(this).inflate(R.layout.item_answer_recycler_list, null);
            TextView readView = newView.findViewById(R.id.tv_anwer);
            setText(readView, subDataBean);


        } else if (direction == FlipperLayout.MOVE_TO_RIGHT && index >= 2) {//上一页
            AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(index - 2);
            newView = LayoutInflater.from(this).inflate(R.layout.item_answer_recycler_list, null);
            TextView readView = newView.findViewById(R.id.tv_anwer);
            setText(readView, subDataBean);

        }

        return newView;
    }

    @Override
    public void currentPosition(int index) {
        Log.i("@@@", index + "");
        curPosition = index - 1;
        topicAdapter.notifyCurPosition(curPosition);
        topicAdapter.notifyPrePosition(prePosition);
        recyclerView.smoothScrollToPosition(curPosition);

        prePosition = curPosition;
    }

    @Override
    public boolean currentIsLastPage() {
        index = rootLayout.getIndex();
        return datas.size() != index;
    }

    @Override
    public boolean whetherHasNextPage() {
        index = rootLayout.getIndex();
//        Log.i("whetherHasNextPage size= ",datas.size()+"=="+index);

        return datas.size() != index;
    }

    private void choosePage(int recover, int one, int two) {

        rootLayout = findViewById(R.id.container);
        rootLayout.removeAllViews();
        rootLayout.setIndex(two);

        View recoverView = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);
        View view1 = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);
        View view2 = LayoutInflater.from(SubjectActivity.this).inflate(R.layout.item_answer_recycler_list, null);

        rootLayout.initFlipperViews(SubjectActivity.this, view2, view1, recoverView);

        final TextView recoverReadView = recoverView.findViewById(R.id.tv_anwer);
        final TextView readView1 = view1.findViewById(R.id.tv_anwer);
        final TextView readView2 = view2.findViewById(R.id.tv_anwer);


        AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(recover);
        //左边一个
        setText(recoverReadView, subDataBean);

        subDataBean = datas.get(one);
        //填充第一页的文本
        setText(readView1, subDataBean);

        subDataBean = datas.get(two);
        //填充第二页的文本
        setText(readView2, subDataBean);

    }

    private void initList() {
        recyclerView = findViewById(R.id.list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);

        topicAdapter = new TopicAdapter(this);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(topicAdapter);

        if (topicAdapter != null) {
            topicAdapter.setDataNum(datas.size());
        }


        assert topicAdapter != null;
        topicAdapter.setOnTopicClickListener((holder, position) -> {
            curPosition = position;
            Log.i("点击了==>", position + "");
            if (position == 0) {
                initPage();
            } else if (position == datas.size() - 1) {
                initLastPage();
            } else {
                choosePage(position - 1, position, position + 1);
            }


            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }

            topicAdapter.notifyCurPosition(curPosition);
            topicAdapter.notifyPrePosition(prePosition);

            prePosition = curPosition;
        });


    }


    private void initSlidingUoPanel() {
        mLayout = findViewById(R.id.sliding_layout);
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
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
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
            InputStream in = getAssets().open("answer.json");
            return JSON.parseObject(inputStream2String(in), AnwerInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }
        return null;
    }
}
