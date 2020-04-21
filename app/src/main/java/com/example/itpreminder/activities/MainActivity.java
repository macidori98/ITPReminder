package com.example.itpreminder.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.Window;

import com.example.itpreminder.R;
import com.example.itpreminder.fragment.LoginFragment;
import com.example.itpreminder.utils.Constant;
import com.example.itpreminder.utils.FragmentNavigation;

import java.util.Objects;

import static com.example.itpreminder.utils.Constant.PERMISSIONS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        if(!Constant.hasPermissions(this, PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

        FragmentNavigation.getInstance(this).replaceFragment(new LoginFragment(), R.id.fragment_content);
    }

    @Override
    public void onBackPressed() {
        FragmentNavigation.getInstance(getApplicationContext()).onBackPressed(this);
    }
}
