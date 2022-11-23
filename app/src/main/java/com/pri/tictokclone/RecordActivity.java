package com.pri.tictokclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daasuu.gpuv.camerarecorder.CameraRecordListener;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorder;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import com.daasuu.gpuv.camerarecorder.LensFacing;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlSepiaFilter;
import com.pri.tictokclone.Adapter.FilterAdapter;
import com.pri.tictokclone.Filter.FilterType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    private FrameLayout view_container;
    private ImageButton ib_record;
    private Button btn_addSound;
    
    private GLSurfaceView glSurfaceView;
    private GPUCameraRecorder gpu;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected GlFilter glFilter;

//    boolean recordOn = false;

    String filepath;
    private ImageView iv_flip, iv_flash, iv_close;

    private ListView lv_filter;
    private String sound_url = null;
    private String sound_title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);

        iv_flip = findViewById(R.id.iv_flip);
        iv_flip.setTag(0);
        iv_flash = findViewById(R.id.iv_flash);
        iv_close = findViewById(R.id.iv_close);
        btn_addSound = findViewById(R.id.btn_addSound);

        ib_record = findViewById(R.id.ib_record);
        ib_record.setTag("stop");

        sound_url = getIntent().getStringExtra("sound_url");
        sound_title = getIntent().getStringExtra("sound_title");

        if(sound_url!=null){
            Toast.makeText(this, sound_url, Toast.LENGTH_SHORT).show();
        }

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCamera();
    }

    private void setupCamera(){
        glSurfaceView = new GLSurfaceView(getApplicationContext());
        view_container = findViewById(R.id.view_container);
        view_container.addView(glSurfaceView);

        gpu = new GPUCameraRecorderBuilder(this, glSurfaceView)
                .lensFacing(lensFacing)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> {
                            iv_flash.setEnabled(flashSupport);
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                        Log.e("export :", "saved ok");
                    }

                    @Override
                    public void onRecordStart() {
                        runOnUiThread(() -> {

                        });
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("GPUCameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if(iv_flip.getTag().equals(1)){
                            runOnUiThread(() -> {
                                setupCamera();
                            });
                        }
                        iv_flip.setTag(0);
                    }

                    @Override
                    public void onVideoFileReady() {

                    }
                })
                .build();
    }

    private void initListener(){

        ib_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ib_record.getTag().equals("stop")){
                    filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/"+new SimpleDateFormat("MM_dd_HHmmss").format(new Date())+"temp.mp4";
                    ib_record.setImageResource(R.drawable.ic_stop);
                    Toast.makeText(RecordActivity.this, "Record Start", Toast.LENGTH_SHORT).show();
                    gpu.start(filepath);
//                    recordOn = true;
                    ib_record.setTag("record");
                    lv_filter.setVisibility(View.GONE);
                } else {
                    gpu.stop();
//                    recordOn = false;
                    ib_record.setTag("stop");
                    ib_record.setImageResource(R.drawable.circle_foreground);
                    Toast.makeText(RecordActivity.this, "Record Stop", Toast.LENGTH_SHORT).show();
                    lv_filter.setVisibility(View.VISIBLE);
                }
            }
        });

        iv_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseCamera();
                if (lensFacing == LensFacing.BACK){
                    lensFacing = LensFacing.FRONT;
                } else {
                    lensFacing = LensFacing.BACK;
                }
                iv_flip.setTag(1);
            }
        });

        iv_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gpu != null && gpu.isFlashSupport()){
                    gpu.switchFlashMode();;
                    gpu.changeAutoFocus();
                    if(iv_flash.getTag().equals("off")){
                        iv_flash.setTag("on");
                        iv_flash.setImageResource(R.drawable.ic_flash);
                    } else {
                        iv_flash.setTag("off");
                        iv_flash.setImageResource(R.drawable.ic_flash_off);
                    }
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gpu != null){
                    gpu.stop();
                    gpu.release();
                    gpu = null;
                }

//                Intent intent = new Intent(RecordActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        lv_filter = findViewById(R.id.lv_filter);

        final List<FilterType> filterTypes = FilterType.createFilterList();
        lv_filter.setAdapter(new FilterAdapter(this, R.layout.row_white_text, filterTypes).whiteMode());
        lv_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gpu != null) {
                    gpu.setFilter(FilterType.createGlFilter(filterTypes.get(position), getApplicationContext()));
                }
            }
        });

        btn_addSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, SoundActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera(){
        if(glSurfaceView != null){
            glSurfaceView.onPause();
        }

        if(gpu != null){
            gpu.stop();
            gpu.release();
            gpu = null;
        }

        if(glSurfaceView != null){
            view_container.removeView(glSurfaceView);
            glSurfaceView = null;
        }
    }

    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        Uri uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                uri));
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(RecordActivity.this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        super.onBackPressed();
        Log.e("backpress", "back pressed");
        this.finish();
    }
}