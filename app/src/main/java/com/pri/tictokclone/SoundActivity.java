package com.pri.tictokclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pri.tictokclone.Adapter.SoundAdapter;
import com.pri.tictokclone.Models.SoundModel;
import com.pri.tictokclone.Responses.ApiClient;
import com.pri.tictokclone.Responses.ApiInterface;
import com.pri.tictokclone.Responses.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoundActivity extends AppCompatActivity {

    private ImageView iv_close;

    private RecyclerView recyclerView;
    private List<SoundModel> soundModelList;
    private SoundAdapter soundAdapter;

    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        iv_close = findViewById(R.id.iv_close);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        soundModelList = new ArrayList<>();
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));

        Call<Users> call = apiInterface.performAllSounds();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.isSuccessful()){
                    soundModelList = response.body().getAllSounds();
                    soundAdapter = new SoundAdapter(soundModelList, getApplicationContext());
                    recyclerView.setAdapter(soundAdapter);
                    soundAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SoundActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(SoundActivity.this, "Network Error2!", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}