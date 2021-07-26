package com.example.cst_2335_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class PickDateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int ACTIVITY_REQUEST_CODE = 200;
    private String datePickerDate;
    private Toolbar toolbar;

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

        //Sets up Toolbar Menu
        toolbar = findViewById(R.id.pickedDateToolBarXML);
        toolbar.setTitle(R.string.toolbarTitleDatePickerActivity);
        setSupportActionBar(toolbar);

        //Sets up NavigationDrawer side menu
        DrawerLayout drawer = findViewById(R.id.sideMenuDrawerLayoutXML);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.sideNavMenu);
        navigationView.setNavigationItemSelectedListener(this);
        //Without this two statements the navigation menu's menuItems were not responding to clicks events.
        navigationView.bringToFront();

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


    // Inflate the menu items for use in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

   /* onOptionsItemSelected()

       Parameter: MenuItem which is the <item> that was clicked in the toolbar.

       The case statement determines which button/icon was clicked and executes the appropriate action

       Which in this case for ever menu item that isn't the in the over flow menu will navigate to the
       chosen activity.

       The overflow menu item calls createAlertDialogHelpWindow() which generates the Alert Dialog window
       to be displayed with the current activity.

     */

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Finds menu items from XML file and handles a case for item selected.
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.toolBarMainMenuIcon:
                message = "You clicked home icon item";
                break;
            case R.id.toolBarTodayImageIcon:
                Intent imageViewActivity = new Intent(this, ImageViewActivity.class);
                startActivity(imageViewActivity);
                message = "You clicked on imageViewActivity menu item";
                break;
            case R.id.toolBarPickDateIcon:
                Intent pickDateActivity = new Intent(this, PickDateActivity.class);
                startActivity(pickDateActivity);
                message = "You clicked on pickDateActivity menu item";
                break;
            case R.id.toolBarSavedImageIcon:
                Intent savedImagesActivity = new Intent(this, SavedImages.class);
                startActivity(savedImagesActivity);
                message = "You clicked on savedImagesActivity menu item";
                break;
            case R.id.toolBarOverFlowHelpMenu:
                createAlertDialogHelpWindow();
                message = "You clicked on the overFlowHelpMenu menu item two";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }



    /* onNavigationItemSelected()

        Parameter: MenuItem which is the <item> that was clicked in the side menu.

        The case statement determines which button/icon was clicked and executes the appropriate action
        and sending the user to the correct activity.


     */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.sideMenuMainMenuXML:
                message = "Main Menu item Clicked.";
                break;
            case R.id.sideMenuTodayImageXML:
                message = "sideMenuTodayImageXML item Clicked.";
                Intent imageViewActivity = new Intent(this, ImageViewActivity.class);
                startActivity(imageViewActivity);
                break;
            case R.id.sideMenuPickDateIconXML:
                message = "sideMenuPickDateIconXML item Clicked.";
                Intent pickDateActivity = new Intent(this, PickDateActivity.class);
                startActivity(pickDateActivity);
                break;
            case R.id.sideMenuSavedImagesIconXML:
                message = "sideMenuSavedImagesIconXML item Clicked.";
                //this makes the back button on the device return to the first activity.
                this.finish();
                Intent savedImagesActivity = new Intent(this, SavedImages.class);
                startActivity(savedImagesActivity);
                break;

        }

        DrawerLayout drawerLayout = findViewById(R.id.sideMenuDrawerLayoutXML);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        return false;
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




    /* createAlertDialogHelpWindow()


       Sets alert_dialog_layout which holds the inflated layout for alert dialog window
       Create View objects which we will use to set text values.
       Set Text values for View's.
       Build alert window via AlertDialog.Builder and display it to the user.

    */

    private void createAlertDialogHelpWindow(){


        View alert_dialog_layout = getLayoutInflater().inflate(R.layout.help_menu_alert_dialog_layout,null);

        TextView title = alert_dialog_layout.findViewById(R.id.helpMenuTitleXMl);
        TextView paragraphOne = alert_dialog_layout.findViewById(R.id.helpMenuItemOneXML);
        TextView paragraphTwo = alert_dialog_layout.findViewById(R.id.helpMenuItemTwoXML);

        title.setText(R.string.helpMenuTitle);
        paragraphOne.setText(R.string.helpParagraphOne);
        paragraphTwo.setText(R.string.helpParagraphTwo);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.helpMenuDialogTitleImageViewActivity);
        alertBuilder.setView(alert_dialog_layout);
        alertBuilder.setNegativeButton("Close", (click, arg) -> { });
        alertBuilder.create().show();
    }


}