package com.kisen.kdownloadMain;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kisen.kdownload.KDownload;
import com.kisen.kdownload.KHttp;
import com.kisen.kdownload.callback.FileCallback;
import com.kisen.kdownload.net.Call;

import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://imtt.dd.qq.com/16891/7E2C4708E11245E24736C37503BA9EF2.apk?fsname=com.tencent.tmgp.pubgmhd_0.11.3_6500.apk";
    private ProgressBar progress;
    private TextView tvProgress;
    private Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KHttp.configLog(true);
        initTask();
    }

    private void initTask() {
        progress = findViewById(R.id.progress);
        tvProgress = findViewById(R.id.tv_progress);
    }

    public void start(View view) {
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        call = KDownload.getInstance().call(this, url, folderPath, new FileCallback() {
            @Override
            public void onProgress(Call call, int size, int totalSize) {
                float progress = size * 1f / totalSize;
                MainActivity.this.progress.setProgress((int) (progress * 10000));
                DecimalFormat df = new DecimalFormat("###.##");
                tvProgress.setText(df.format(progress * 100) + "%");
            }

            @Override
            public void onPause(Call call, int size, int totalSize) {

            }

            @Override
            public void onComplete(final Call call) {
                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Call call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        call.start();
    }

    public void stop(View view) {
        call.stop();
    }

    public void delete(View view) {
        if (call == null || TextUtils.isEmpty(call.getPath()))
            return;
        File file = new File(call.getPath());
        if (file.exists()) {
            file.delete();
        }
    }
}
