package com.ly.liveanimationdemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ly.liveanimationdemo.R;
import com.ly.liveanimationdemo.library.PeriscopeLayout;

/**
 * Created by Shinelon on 2017/6/29.
 */

public class AnimationForHeartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_heart);
        final PeriscopeLayout periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        findViewById(R.id.bt_animation_heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periscopeLayout.addHeart();
            }
        });
    }
}
