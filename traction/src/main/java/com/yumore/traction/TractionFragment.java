package com.yumore.traction;


import android.content.Context;
import android.media.MediaPlayer;
import android.widget.VideoView;

import androidx.annotation.NonNull;

/**
 * @author Nathaniel
 */
public class TractionFragment extends AbstractFragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private VideoView videoView;
    private int currentPage;
    private boolean paused;

    private OnFragmentToActivity<Integer> onFragmentToActivity;
    private Context context;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentToActivity) {
            onFragmentToActivity = (OnFragmentToActivity<Integer>) context;
        }
        this.context = context;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_traction;
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {
        if (getArguments() == null) {
            return;
        }
        videoView = findViewById(R.id.videoView);
        int videoRes = getArguments().getInt("res");
        currentPage = getArguments().getInt("page");
        videoView.setOnPreparedListener(this);
        videoView.setVideoPath("android.resource://" + context.getPackageName() + "/" + videoRes);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (videoView != null) {
            videoView.requestFocus();
            videoView.seekTo(0);
            videoView.start();
            videoView.setOnCompletionListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paused) {
            if (videoView != null) {
                videoView.seekTo(currentPage);
                videoView.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            currentPage = videoView.getCurrentPosition();
        }
        paused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (null != onFragmentToActivity) {
            onFragmentToActivity.onCallback(OnFragmentToActivity.ACTION_NEXT_PAGE, currentPage);
        }
    }
}