package com.pri.tictokclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pri.tictokclone.Adapter.ClipAdapter;
import com.pri.tictokclone.MainRecyclerView.VerticalSpacingItemDecorator;
import com.pri.tictokclone.MainRecyclerView.VideoPlayerRecyclerAdapter;
import com.pri.tictokclone.MainRecyclerView.VideoPlayerRecyclerView;
import com.pri.tictokclone.Models.MediaObject;
import com.pri.tictokclone.Responses.ApiClient;
import com.pri.tictokclone.Responses.ApiInterface;
import com.pri.tictokclone.Responses.Users;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    private ArrayList<MediaObject> mediaObjectList = new ArrayList<>();
//    private ClipAdapter clipAdapter;
    private VideoPlayerRecyclerView recyclerView;
    public static ApiInterface apiInterface;
    private BottomNavigationView bottom_nav;

    TextView tv_following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        init();
    }

    private void init(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        bottom_nav = findViewById(R.id.bottom_nav);
        tv_following = findViewById(R.id.tv_following);

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

//        mediaObjectList = new ArrayList<>();
//        clipAdapter = new ClipAdapter(mediaObjectList, getApplicationContext());
//        recyclerView.setAdapter(clipAdapter);
//        clipAdapter.notifyDataSetChanged();

        bottom_nav.setOnItemSelectedListener(onItemSelectedListener);

        LoadAllPosts();

        tv_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FollowingActivity.class);
                startActivity(intent);
//                Animatoo.animateSwipeRight(HomeActivity.this);
//                finish();
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
                    if(response.body()!=null){
                        mediaObjectList = (ArrayList<MediaObject>) response.body().getAllPosts();
                        recyclerView.setMediaObjects(mediaObjectList);
                        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjectList, initGlide());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        recyclerView.setKeepScreenOn(true);
                        recyclerView.smoothScrollToPosition(mediaObjectList.size()+1);
                    }
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Network Error.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Network Error.2", Toast.LENGTH_SHORT).show();
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

    //블로그 글 업로드 안됨
    NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_add:
                    checkPermission();
                    return true;
                case R.id.nav_search:
                    Intent intent = new Intent(HomeActivity.this, DiscoverActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    return true;
                case R.id.nav_home:
                case R.id.nav_comment:
                    Intent intent1 = new Intent(HomeActivity.this, MessageActivity.class);
                    startActivity(intent1);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    return true;
                case R.id.nav_private:
                    Intent intent2 = new Intent(HomeActivity.this, AccountActivity.class);
                    startActivity(intent2);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    return true;
            }
            return false;
        }
    };

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.color.colorPrimaryDark)
                .error(R.color.colorPrimaryDark);
        return Glide.with(this).setDefaultRequestOptions(options);
    }


    //권한 관련 블로그 아직 업로드 안됨
    private void checkPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
//            GLSurfaceView glSurfaceView;
//            glSurfaceView = new GLSurfaceView(getApplicationContext());
            Intent intent = new Intent(HomeActivity.this, RecordActivity.class);
            startActivity(intent);
//            Animatoo.animateSlideUp(HomeActivity.this);
//                    finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HomeActivity.this, "permission has been granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "[WARN] permission is not granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}