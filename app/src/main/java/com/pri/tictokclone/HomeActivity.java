package com.pri.tictokclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pri.tictokclone.Adapter.ClipAdapter;
import com.pri.tictokclone.MainRecyclerView.VerticalSpacingItemDecorator;
import com.pri.tictokclone.MainRecyclerView.VideoPlayerRecyclerView;
import com.pri.tictokclone.Models.MediaObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private List<MediaObject> mediaObjectList;
    private ClipAdapter clipAdapter;
    private VideoPlayerRecyclerView recyclerView;

    private BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        bottom_nav = findViewById(R.id.bottom_nav);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(0);
        recyclerView.addItemDecoration(itemDecorator);

        //리싸이클러뷰 아답터를 드래그할때 자동으로 첫라인 맞춰주는 기능
        SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recyclerView);

        mediaObjectList = new ArrayList<>();
        clipAdapter = new ClipAdapter(mediaObjectList, getApplicationContext());
        recyclerView.setAdapter(clipAdapter);
        clipAdapter.notifyDataSetChanged();

        bottom_nav.setOnItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        if(recyclerView!=null){
            recyclerView.releasePlayer();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(recyclerView!=null){
            recyclerView.releasePlayer();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.colorPrimaryDark)
                .error(R.color.colorPrimaryDark);
        return Glide.with(this).setDefaultRequestOptions(options);
    }
}