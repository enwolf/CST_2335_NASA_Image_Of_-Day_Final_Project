package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQUEST_CODE = 100;

    private final Animation fadeInOut = new AlphaAnimation(1.0f, 0.0f);
    private Button startBtn;
    private TextView clickToStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //startBtn = findViewById(R.id.startActivityBtn);
        clickToStart = findViewById(R.id.startButtonTextView);


        Intent mainMenu = new Intent(this, MainMenu.class);
        clickToStart.setOnClickListener(click -> startActivity(mainMenu));



        fadeInOut.setRepeatCount(Animation.INFINITE);
        fadeInOut.setRepeatMode(Animation.REVERSE);
        fadeInOut.setDuration(1300);
        clickToStart.startAnimation(fadeInOut);

    }
}