package com.kisen.kdownload;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kisen.kdownload.download.KHttp;
import com.kisen.kdownload.download.callback.FileCallback;
import com.kisen.kdownload.download.net.Call;
import com.kisen.kdownload.download.util.Util;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FileCallback {

    private final String url = "http://sw.bos.baidu.com/sw-search-sp/software/e25c4cc36a934/QQ_8.9.6.22427_setup.exe";
    private ProgressBar progress;
    private ImageView image;
    private Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTask();
        readImage();
    }

    private void initTask() {
        progress = findViewById(R.id.progress);
        image = findViewById(R.id.image);
    }

    private void readImage() {
        File externalCacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(externalCacheDir, Util.encode(url, "temp"));
        if (file.exists()) {
            String localPath = file.getAbsolutePath();
            setupImage(localPath);
        }
    }

    private void setupImage(String path) {
        if (!TextUtils.isEmpty(path))
            image.setImageURI(Uri.fromFile(new File(path)));
    }

    public void start(View view) {
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        call = KHttp.call(this, url, folderPath, this);
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

    @Override
    public void onProgress(Call call, int size, int totalSize) {
        this.progress.setProgress((int) (size * 10000f / totalSize));
    }

    @Override
    public void onPause(Call call, int size, int totalSize) {

    }

    @Override
    public void onComplete(final Call call) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupImage(call.getPath());
            }
        });
    }

    @Override
    public void onError(Call call, Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
