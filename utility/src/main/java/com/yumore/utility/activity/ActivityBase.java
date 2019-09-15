package com.yumore.utility.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.yumore.utility.utility.RxActivityTool;

/**
 * @author yumore
 */
public class ActivityBase extends FragmentActivity {

    public ActivityBase mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxActivityTool.addActivity(this);
//        DragAndDropPermissionsCompat
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
