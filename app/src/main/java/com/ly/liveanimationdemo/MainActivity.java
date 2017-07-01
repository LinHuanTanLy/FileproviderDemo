package com.ly.liveanimationdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.liveanimationdemo.ui.AnimationForHeartActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTestAnimation;
    private TextView tvTestUri;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewAndListener();
    }

    /**
     * 初始化页面以及监听
     */
    private void initViewAndListener() {
        tvTestAnimation = (TextView) findViewById(R.id.tv_animation_heart);
        tvTestUri = (TextView) findViewById(R.id.tv_animation_uri);
        tvTestAnimation.setOnClickListener(this);
        tvTestUri.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Toast.makeText(this, "已经获取", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "已经获取", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_animation_heart:
                startActivity(new Intent(this, AnimationForHeartActivity.class));
                break;
            case R.id.tv_animation_uri:
                startActivity(new Intent(this, com.ly.liveanimationdemo.ui.MainActivity.class));
                break;
            default:
                break;
        }
    }
}
