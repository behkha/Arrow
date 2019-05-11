package com.ratintech.arrowexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.ratintech.arrow.Arrow;
import com.ratintech.arrow.ArrowDownload;
import com.ratintech.arrow.ArrowDownloadBuilder;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else
            download();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0) {
                    boolean permissionsDenied = false;
                    for (String permission : permissions)
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                            permissionsDenied = true;
                    if (!permissionsDenied)
                        download();
                }
                break;
        }
    }

    private void download(){
        final ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        String directory = Environment.getExternalStorageDirectory().getPath() + File.separator + "Arrow" + File.separator + "Videos" + File.separator;
        Arrow.download(new ArrowDownloadBuilder()
                .setUrl("http://file.capitannews.ir/news/videos/OXKH6kR0ll2drHxKFIWMewMMfLspjX7m18V1h24iLrg5VlxK.mp4")
                .setDirectory(directory)
                .setTag(TAG)
                .setDownloadListener(new ArrowDownload.DownloadListener() {
                    @Override
                    public void onDownloadStarted() {
                        Log.i(TAG, "onDownloadStarted: ");
                    }

                    @Override
                    public void onDownloadFinished(@Nullable File file) {
                        progressBar.setProgress(0);
                        Log.i(TAG, "onDownloadFinished: " + String.valueOf(file == null));
                        if (file != null)
                            Log.i(TAG, "onDownloadFinished: " + file.getName());
                    }

                    @Override
                    public void onDownloadProgress(int progress) {
                        Log.i(TAG, "onDownloadProgress: " + String.valueOf(progress));
                        progressBar.setProgress(progress);
                    }

                    @Override
                    public void onDownloadInterrupted() {
                        Log.i(TAG, "onDownloadInterrupted: ");
                        progressBar.setProgress(0);
                    }
                })
                .build());
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        Arrow.cancelAll();
        super.onStop();
    }
}
