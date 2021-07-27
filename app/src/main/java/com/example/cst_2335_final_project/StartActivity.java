package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQUEST_CODE = 100;


    private Button startBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startBtn = findViewById(R.id.startActivityBtn);


        Intent mainMenu = new Intent(this, MainMenu.class);
        startBtn.setOnClickListener(click -> startActivity(mainMenu));




    }
}