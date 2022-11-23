package com.pri.tictokclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pri.tictokclone.Models.SoundModel;
import com.pri.tictokclone.R;
import com.pri.tictokclone.RecordActivity;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {

    List<SoundModel> soundModelList;
    Context mContext;

    public SoundAdapter(List<SoundModel> soundModelList, Context mContext) {
        this.soundModelList = soundModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sound, parent, false);
        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        SoundModel soundModel = soundModelList.get(position);

        holder.tv_soundTitle.setText(soundModel.getSound_title());
        Glide.with(mContext).load(soundModel.getSound_img()).into(holder.iv_soundimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecordActivity.class);
                intent.putExtra("sound_url", soundModel.getSound_file());
                intent.putExtra("sound_title", soundModel.getSound_title());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundModelList.size();
    }

    public class SoundViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_soundTitle;
        private ImageView iv_soundimg;

        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soundTitle = itemView.findViewById(R.id.tv_soundTitle);
            iv_soundimg = itemView.findViewById(R.id.iv_soundimg);
        }
    }
}
