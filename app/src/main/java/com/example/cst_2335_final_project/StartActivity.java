package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;


    private Button startBtn;
    private Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startBtn = findViewById(R.id.startActivityBtn);

        //  For now lets just go directly to the view image page, date picker to be the later intent
        //  To send to from start button

        nextActivity = new Intent(this, ImageViewActivity.class);
        startBtn.setOnClickListener(click -> startActivity(nextActivity));




    }
}