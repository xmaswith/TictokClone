package com.pri.tictokclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pri.tictokclone.MainRecyclerView.VerticalSpacingItemDecorator;
import com.pri.tictokclone.MainRecyclerView.VideoPlayerRecyclerAdapter;
import com.pri.tictokclone.MainRecyclerView.VideoPlayerRecyclerView;
import com.pri.tictokclone.Models.MediaObject;
import com.pri.tictokclone.Responses.ApiClient;
import com.pri.tictokclone.Responses.ApiInterface;
import com.pri.tictokclone.Responses.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingActivity extends AppCompatActivity {

    private ArrayList<MediaObject> mediaObjectList = new ArrayList<>();
    private VideoPlayerRecyclerView recyclerView;
    public static ApiInterface apiInterface;

    private BottomNavigationView bottom_nav;

    TextView tv_foryou;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        init();
    }

    private void init(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        bottom_nav = findViewById(R.id.bottom_nav);
        tv_foryou = findViewById(R.id.tv_foryou);

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

        bottom_nav.setOnItemSelectedListener(onItemSelectedListener);

        LoadAllPosts();

        tv_foryou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowingActivity.this, HomeActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                Animatoo.animateSwipeLeft(getApplicationContext());
                finish();
            }
        });
    }

    private void LoadAllPosts() {
        Call<Users> call = apiInterface.performAllPosts();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful())
                {
                    mediaObjectList = (ArrayList<MediaObject>) response.body().getAllPosts();
                    recyclerView.setMediaObjects(mediaObjectList);
                    VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjectList, initGlide());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setKeepScreenOn(true);
                    recyclerView.smoothScrollToPosition(mediaObjectList.size()+1);
                }
                else
                {
                    Toast.makeText(FollowingActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(FollowingActivity.this, "Network Error.2", Toast.LENGTH_SHORT).show();
            }
        });
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

    NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }
    };

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.colorPrimaryDark)
                .error(R.color.colorPrimaryDark);
        return Glide.with(this).setDefaultRequestOptions(options);
    }
}