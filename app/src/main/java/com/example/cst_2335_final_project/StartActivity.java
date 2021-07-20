package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;


    private Button startBtn;
    private Button savedBtn;
    private Button pickedDateBtn;
    private Intent nextActivity;
    private Intent savedActivity;
    private Intent pickedDateActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startBtn = findViewById(R.id.startActivityBtn);
        savedBtn = findViewById(R.id.savedActivityBtn);
        pickedDateBtn = findViewById(R.id.pickedDateActivityBtn);
        //For now lets just go directly to the view image page, date picker to be the later intent to send to from start button

        nextActivity = new Intent(this, ImageViewActivity.class);
        startBtn.setOnClickListener(click -> startActivity(nextActivity));

        savedActivity = new Intent(this, SavedImages.class);
        savedBtn.setOnClickListener(click -> startActivity(savedActivity));

        pickedDateActivity = new Intent(this, PickDateActivity.class);
        pickedDateBtn.setOnClickListener(click -> startActivity(pickedDateActivity));




    }
}