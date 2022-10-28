package com.pri.tictokclone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pri.tictokclone.Models.MediaObject;
import com.pri.tictokclone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClipAdapter extends RecyclerView.Adapter<ClipAdapter.ClipViewHolder> {

    List<MediaObject> mediaObjectList;
    Context mContext;

    public ClipAdapter(List<MediaObject> mediaObjectList, Context mContext) {
        this.mediaObjectList = mediaObjectList;
        this.mContext = mContext;
    }

    public class ClipViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civ_album;
        public ClipViewHolder(@NonNull View itemView) {
            super(itemView);
            civ_album = (CircleImageView) itemView.findViewById(R.id.civ_album);
        }
    }

    @NonNull
    @Override
    public ClipAdapter.ClipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main, parent, false);
        return new ClipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClipAdapter.ClipViewHolder holder, int position) {
        MediaObject mediaObject = mediaObjectList.get(position);
        Glide.with(mContext).load(mediaObject.getThumbnail()).into(holder.civ_album);
    }

    @Override
    public int getItemCount() {
        return mediaObjectList.size();
    }

}
