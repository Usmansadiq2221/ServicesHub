package com.devtwist.serviceshub.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.devtwist.serviceshub.Activities.LoginActivity;
import com.devtwist.serviceshub.Models.Userdata;
import com.devtwist.serviceshub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ImageView _innerLogo, _outerLogo;
    private CountDownTimer forwardAnimation, outerLogoIncrease,
            innerLogoDecrease, outerLogoDecrease,innerLogoIncrease,animationEnd;
    private boolean isLogedin, isProfileCreated;
    private Intent intent;
    private SharedPreferences preferences;
    private Bundle bundle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _innerLogo = findViewById(R.id._innerLogo2);
        _outerLogo = findViewById(R.id._outerLogo2);

        _innerLogo.setScaleX(0f);
        _innerLogo.setScaleY(0f);



        forwardAnimation = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(0.85f).scaleY(0.85f).setDuration(1500);
                _innerLogo.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoIncrease.start();
            }
        };

        outerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(1.15f).scaleY(1.15f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoDecrease.start();
            }
        };

        outerLogoDecrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(0.85f).scaleY(0.85f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                innerLogoDecrease.start();
            }
        };

        innerLogoDecrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _innerLogo.animate().scaleX(0.75f).scaleY(0.75f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                innerLogoIncrease.start();
            }
        };

        innerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _innerLogo.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                outerLogoIncrease.start();
            }
        };

        outerLogoIncrease = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(1.15f).scaleY(1.15f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                animationEnd.start();
            }
        };

        animationEnd = new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                _outerLogo.animate().scaleX(1f).scaleY(1f).setDuration(1500);
            }

            @Override
            public void onFinish() {
                bundle = getIntent().getExtras();
                if (bundle!=null){
                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                }
                else {
                    preferences = getSharedPreferences("MyData", MODE_PRIVATE);
                    if (preferences.contains("isLogedIn") && preferences.contains("isProfileCreated")) {
                        isLogedin = preferences.getBoolean("isLogedIn", false);
                        isProfileCreated = preferences.getBoolean("isProfileCreated", false);
                    }
                    if (isLogedin && isProfileCreated) {
                        intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (!isLogedin) {
                            intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

            }
        };


        forwardAnimation.start();

    }
}