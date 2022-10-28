package com.pri.tictokclone.MainRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.pri.tictokclone.Models.MediaObject;
import com.pri.tictokclone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {
    FrameLayout media_container;
    TextView tv_description;
    ImageView thumbnail, volumeControl;
    ProgressBar pb_horizontal;
    View parent;
    RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        tv_description = itemView.findViewById(R.id.tv_description);
        pb_horizontal = itemView.findViewById(R.id.pb_horizontal);
        volumeControl = itemView.findViewById(R.id.volume_control);
    }

    public void onBind(MediaObject mediaObject, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        tv_description.setText(mediaObject.getDescription()+",\n"+mediaObject.getDate());

        this.requestManager.load(mediaObject.getThumbnail()).into(thumbnail);
    }

}
