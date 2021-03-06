package com.yumore.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yumore.uitls.utils.BitmapUtils;
import com.yumore.video.R;
import com.yumore.video.bean.EffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * 滤镜列表适配器
 */
public class VideoEffectAdapter extends RecyclerView.Adapter<VideoEffectAdapter.ImageHolder> {

    private final Context mContext;
    private final List<EffectType> mEffectList = new ArrayList<>();
    private int mSelected = -1;
    private OnEffectChangeListener mEffectChangeListener;

    public VideoEffectAdapter(Context context, List<EffectType> effectTypeList) {
        mContext = context;
        mEffectList.addAll(effectTypeList);
    }

    /**
     * 切换特效数据
     *
     * @param effectTypeList
     */
    public void changeEffectData(List<EffectType> effectTypeList) {
        mEffectList.clear();
        if (effectTypeList != null) {
            mEffectList.addAll(effectTypeList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_effect_view, parent, false);
        ImageHolder viewHolder = new ImageHolder(view);
        viewHolder.effectRoot = view.findViewById(R.id.item_effect_root);
        viewHolder.effectName = view.findViewById(R.id.item_effect_name);
        viewHolder.effectImage = view.findViewById(R.id.item_effect_image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, final int position) {
        if (mEffectList.get(position).getThumb().startsWith("assets://")) {
            holder.effectImage.setImageBitmap(BitmapUtils.getImageFromAssetsFile(mContext,
                    mEffectList.get(position).getThumb().substring("assets://".length())));
        } else {
            holder.effectImage.setImageBitmap(BitmapUtils.getBitmapFromFile(mEffectList.get(position).getThumb()));
        }
        holder.effectName.setText(mEffectList.get(position).getName());
        holder.effectRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelected == position) {
                    return;
                }
                int lastSelected = mSelected;
                mSelected = position;
                notifyItemChanged(lastSelected, 0);
                notifyItemChanged(position, 0);
                if (mEffectChangeListener != null) {
                    mEffectChangeListener.onEffectChanged(mEffectList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mEffectList == null) ? 0 : mEffectList.size();
    }

    public void setOnEffectChangeListener(OnEffectChangeListener listener) {
        mEffectChangeListener = listener;
    }

    /**
     * 特效改变监听器
     */
    public interface OnEffectChangeListener {

        void onEffectChanged(EffectType effectType);
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        // 根布局
        public LinearLayout effectRoot;
        // 预览缩略图
        public ImageView effectImage;
        // 预览文字
        public TextView effectName;

        public ImageHolder(View itemView) {
            super(itemView);
        }
    }
}
