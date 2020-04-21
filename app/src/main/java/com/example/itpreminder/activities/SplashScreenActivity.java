package com.example.itpreminder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itpreminder.R;
import com.example.itpreminder.utils.Constant;


public class SplashScreenActivity extends AppCompatActivity {
    public static final String TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Animation fromBottom = AnimationUtils.loadAnimation(this, R.anim.splash_screen_animation);
        fromBottom.setDuration(Constant.ANIMATION_DURATION);

        ImageView ivSplashScreen = findViewById(R.id.imageview_splash_screen);
        ivSplashScreen.startAnimation(fromBottom);

        fromBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(SplashScreenActivity.TAG, SplashScreenActivity.TAG);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(SplashScreenActivity.TAG, SplashScreenActivity.TAG);
            }
        });
    }
}
