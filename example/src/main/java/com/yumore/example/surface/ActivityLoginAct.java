package com.yumore.example.surface;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.activity.AndroidBug5497Workaround;
import com.yumore.utility.utility.RxAnimationTool;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.utility.RxKeyboardTool;


/**
 * @author yumore
 */
public class ActivityLoginAct extends ActivityBase {

    @BindView(R2.id.logo)
    ImageView mLogo;
    @BindView(R2.id.et_mobile)
    EditText mEtMobile;
    @BindView(R2.id.iv_clean_phone)
    ImageView mIvCleanPhone;
    @BindView(R2.id.et_password)
    EditText mEtPassword;
    @BindView(R2.id.clean_password)
    ImageView mCleanPassword;
    @BindView(R2.id.iv_show_pwd)
    ImageView mIvShowPwd;
    @BindView(R2.id.btn_login)
    Button mBtnLogin;
    @BindView(R2.id.regist)
    TextView mRegist;
    @BindView(R2.id.forget_password)
    TextView mForgetPassword;
    @BindView(R2.id.content)
    LinearLayout mContent;
    @BindView(R2.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R2.id.service)
    LinearLayout mService;
    @BindView(R2.id.root)
    RelativeLayout mRoot;

    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private float scale = 0.6f; //logo缩放比例
    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);
        RxBarTool.setTransparentStatusBar(this);//状态栏透明化
        RxBarTool.StatusBarLightMode(mContext);
        ButterKnife.bind(this);

        if (isFullScreen(this)) {
            AndroidBug5497Workaround.assistActivity(this);
        }
        initView();
        initEvent();
    }

    private void initView() {
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
    }

    private void initEvent() {
        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanPhone.getVisibility() == View.GONE) {
                    mIvCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPhone.setVisibility(View.GONE);
                }
            }
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPassword.getVisibility() == View.GONE) {
                    mCleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty()) {
                    return;
                }
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(mContext, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    mEtPassword.setSelection(s.length());
                }
            }
        });
        /**
         * 禁止键盘弹起的时候可以滚动
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mScrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                    int dist = mContent.getBottom() - bottom;
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        RxAnimationTool.zoomIn(mLogo, 0.6f, dist);
                    }
                    mService.setVisibility(View.INVISIBLE);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                        RxAnimationTool.zoomOut(mLogo, 0.6f);
                    }
                    mService.setVisibility(View.VISIBLE);
                }
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxKeyboardTool.hideSoftInput(mContext);
            }
        });
    }

    public boolean isFullScreen(AppCompatActivity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

    @OnClick({R2.id.iv_clean_phone, R2.id.clean_password, R2.id.iv_show_pwd})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_clean_phone) {
            mEtMobile.setText("");
        } else if (id == R.id.clean_password) {
            mEtPassword.setText("");
        } else if (id == R.id.iv_show_pwd) {
            if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mIvShowPwd.setImageResource(R.drawable.pass_visuable);
            } else {
                mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mIvShowPwd.setImageResource(R.drawable.pass_gone);
            }
            String pwd = mEtPassword.getText().toString();
            if (!TextUtils.isEmpty(pwd))
                mEtPassword.setSelection(pwd.length());
        }
    }
}
