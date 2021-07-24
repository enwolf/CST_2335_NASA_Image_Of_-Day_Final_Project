package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class PickDateActivity extends AppCompatActivity {


    String datePickerDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);


        Intent nextActivity = new Intent(this, ImageViewActivity.class);
        //link java objects to xml views
        EditText enteredDate = findViewById(R.id.pickDateEditTextXML);
        Button viewTodayImageBtn = findViewById(R.id.enteredDateEditTextButton);
        Button datePickButton = findViewById(R.id.enteredPickedDateButtonXML);
        DatePicker datePicker = findViewById(R.id.datePickerXML);

        viewTodayImageBtn.setOnClickListener(Click -> {

            String editTextValue = enteredDate.getText().toString();
            nextActivity.putExtra("date",editTextValue);
            startActivity(nextActivity);


        });

        datePickButton.setOnClickListener(Click -> {

            startActivity(ReadyDataPickerIntent(nextActivity,datePicker));
            Toast.makeText(this, datePickerDate, Toast.LENGTH_SHORT).show();

        });


    }


    /* ReadyDataPickerIntent()

       @Para Intent intent the intent object we will be adding extra's to and returning.
       @Para DatePicker datePicker object from which we will extract year/month/day data selected by the users.

       String year hold year returned from datePicker
       String month hold month returned from datePicker
       String dat hold dat returned from datePicker

       Formats month to account for 0-11 indexing of month values, and adds leading 0 if month is < 10.
       Formats Day to add leading zeros if day is < 10.

       Concatenates year + month + date with - to get the string we will pass into our URL in the next activity.
       passes datePickerDate, year, monty, day as extra and packages with intent.
       returns the intent with included extra's

     */


    private Intent ReadyDataPickerIntent (Intent intent, DatePicker datePicker) {

        String year;
        String month;
        String day;

        year = String.valueOf(datePicker.getYear());

        if (datePicker.getMonth() < 10)
            month = "0" + (datePicker.getMonth() + 1);
        else
            month = String.valueOf(datePicker.getMonth() + 1);

        if (datePicker.getDayOfMonth() < 10)
            day = "0" + datePicker.getDayOfMonth();
        else
            day = String.valueOf(datePicker.getDayOfMonth());

        datePickerDate = year + "-" + month + "-" + day;

        intent.putExtra("datePickerDate", datePickerDate);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);

        return intent;
    }

}