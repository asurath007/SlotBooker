package com.slotbooker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.slotbooker.Login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private ImageView ic_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ic_logo = findViewById(R.id.ic_logo);

        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ic_logo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.splash_out));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ic_logo.setVisibility(View.GONE);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                }, 1000);
            }
        }, 2000);

//        checkUser();
//        SharedPreferences sp = getApplicationContext().getSharedPreferences("NAME",0);



        //startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }

//    public void checkUser() {
//        Boolean Check = Boolean.valueOf(UtilLogin.readSharedSetting(MainActivity.this, "PUBG", "true"));
//
//        Intent introIntent = new Intent(MainActivity.this, LoginActivity.class);
//        introIntent.putExtra("PUBG", Check);
//
//        if (Check) {
//            startActivity(introIntent);
//        }
//    }
}
