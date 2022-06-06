package com.example.gaolf.simplecamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by gaolf on 17/1/5.
 */

public class MainActivity extends Activity {

    private static final String INPUT_CAMERA_PREVIEW_TYPE = "INPUT_CAMERA_PREVIEW_TYPE";
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS = 10;
    private CameraContainerView cameraContainerView;

    public static Intent createIntent(Context context, int cameraPreviewType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(INPUT_CAMERA_PREVIEW_TYPE, cameraPreviewType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.main_activity);

        if (allPermissionsGranted()) {
            cameraContainerView = (CameraContainerView) findViewById(R.id.main_activity_camera);
            findViewById(R.id.main_activity_switch_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(createIntent(MainActivity.this, CameraContainerView.CAMERA_PREVIEW_TYPE_SURFACE_VIEW));
                }
            });

            int cameraPreviewType = getIntent().getIntExtra(INPUT_CAMERA_PREVIEW_TYPE, CameraContainerView.CAMERA_PREVIEW_TYPE_GL_SURFACE_VIEW);
            cameraContainerView.setCameraPreviewType(cameraPreviewType);
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 打开Camera
        cameraContainerView.onShow();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 关闭Camera
        cameraContainerView.onHide();
    }


    private boolean allPermissionsGranted() {
        //check if req permissions have been granted
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
