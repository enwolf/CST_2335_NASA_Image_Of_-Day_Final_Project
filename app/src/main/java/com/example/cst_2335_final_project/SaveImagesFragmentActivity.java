package com.example.cst_2335_final_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class SaveImagesFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //loads fragment layout into this activity FrameLayout
        setContentView(R.layout.layout_saved_images_fragment_holder);

        //gets data being sent from SavedImages.java
        Bundle dataToSend = getIntent().getExtras();
        SavedImagesFragment listViewDetailFragment = new SavedImagesFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        //Passes data along to savedImagesFragment.java
        listViewDetailFragment.setArguments(dataToSend);
        supportFragmentManager.beginTransaction()
                .replace(R.id.savedImagesFragmentHolderXML, listViewDetailFragment)
                .commit();
    }
 }



