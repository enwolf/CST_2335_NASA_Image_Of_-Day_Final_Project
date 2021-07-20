package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PickDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        //link java objects to xml views
        EditText enteredDate = findViewById(R.id.pickDateEditTextXML);
        Button viewImageBtn = findViewById(R.id.enteredDateButton);


        //sends picked date from EditText as extra to SavedImages Activity when clicked
        viewImageBtn.setOnClickListener(Click -> {

            String editTextValue = enteredDate.getText().toString();
            Intent savedActivity = new Intent(this, ImageViewActivity.class);

            savedActivity.putExtra("date",editTextValue);


            startActivity(savedActivity);


        });


    }
}