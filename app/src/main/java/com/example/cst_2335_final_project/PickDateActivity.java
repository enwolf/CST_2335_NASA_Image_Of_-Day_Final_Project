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
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

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
        //Without this  statements the navigation menu's menuItems were not responding to clicks events.
        navigationView.bringToFront();

        datePickButton.setOnClickListener(Click -> { startActivity(ReadyDataPickerIntent(nextActivity,datePicker)); });

        viewTodayImageBtn.setOnClickListener(Click -> { sendEnteredDate(nextActivity,enteredDate);  });

    }

    /* sendEnteredDate()

        Parameters:

        EditText the date/value entered by the user through the <EditText> View.
        Intent nextActivity, the activity to be loaded.

        Attaches EditText Value as Extra
        Starts Activity based on the passed Intent along with its extras.

     */

    public void sendEnteredDate(Intent nextActivity, EditText date){

        nextActivity.putExtra("date", date.getText().toString());
        startActivity(nextActivity);
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
         //Finds menu items from XML file and handles a case for item selected.
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.toolBarMainMenuIcon:
                Intent mainMenu = new Intent(this, MainMenu.class);
                startActivity(mainMenu);
                break;
            case R.id.toolBarTodayImageIcon:
                Intent imageViewActivity = new Intent(this, ImageViewActivity.class);
                startActivity(imageViewActivity);
                break;
            case R.id.toolBarPickDateIcon:
                Intent pickDateActivity = new Intent(this, PickDateActivity.class);
                startActivity(pickDateActivity);
                break;
            case R.id.toolBarSavedImageIcon:
                Intent savedImagesActivity = new Intent(this, SavedImages.class);
                startActivity(savedImagesActivity);
                break;
            case R.id.toolBarOverFlowHelpMenu:
                createAlertDialogHelpWindow();
                break;
        }

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
                Intent mainMenu = new Intent(this, MainMenu.class);
                startActivity(mainMenu);
                break;
            case R.id.sideMenuTodayImageXML:
                Intent imageViewActivity = new Intent(this, ImageViewActivity.class);
                startActivity(imageViewActivity);
                break;
            case R.id.sideMenuPickDateIconXML:
                Intent pickDateActivity = new Intent(this, PickDateActivity.class);
                startActivity(pickDateActivity);
                break;
            case R.id.sideMenuSavedImagesIconXML:
                //this makes the back button on the device return to the first activity.
                this.finish();
                Intent savedImagesActivity = new Intent(this, SavedImages.class);
                startActivity(savedImagesActivity);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.sideMenuDrawerLayoutXML);
        drawerLayout.closeDrawer(GravityCompat.START);

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

        TextView activityTitle = alert_dialog_layout.findViewById(R.id.helpMenuActivityTitleXML);
        TextView info = alert_dialog_layout.findViewById(R.id.helpMenuTitleXMl);
        TextView paragraphOne = alert_dialog_layout.findViewById(R.id.helpMenuItemOneXML);
        TextView paragraphTwo = alert_dialog_layout.findViewById(R.id.helpMenuItemTwoXML);

        activityTitle.setText(R.string.datePickerHelpMenuDialogTitle);
        info.setText(R.string.helpMenuTitle);

        paragraphOne.setText(R.string.pickedDateHelpMenuParaOne);
        paragraphTwo.setText(R.string.pickedDateHelpMenuParaTwo);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertBuilder.setView(alert_dialog_layout);
        alertBuilder.setNegativeButton(R.string.helpMenuCloseBtnText, (click, arg) -> { });
        alertBuilder.create().show();


    }


}