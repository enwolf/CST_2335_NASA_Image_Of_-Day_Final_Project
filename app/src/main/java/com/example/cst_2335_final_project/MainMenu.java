package com.example.cst_2335_final_project;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private Toolbar toolbar;
    private String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    private String[] stringDateArray = date.split("-");

    private  CharSequence text =  stringDateArray[0].toString()  + stringDateArray[1].toString() + stringDateArray[2].toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button todayBtn = findViewById(R.id.viewTodayImageMainMenuBtnXML);
        Button PickDateBtn = findViewById(R.id.pickDateMainMenuBtnXML);
        Button SavedImageBtn = findViewById(R.id.savedImagesMainMenuBtnXML);


        //Sets up Toolbar Menu
        toolbar = findViewById(R.id.mainMenuToolbar);
        toolbar.setTitle(R.string.toolbarTitleMainMenuActivity);
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

        Intent nextActivity = new Intent(this, ImageViewActivity.class);

        /*
            ERROR: For some reason when testing on the emulator (Nexus 5X API 23 Marshmello) calling a toast message
            will for some unknown reason crash the application completely if it is more then 5 character's long.

            Even though in previous builds I have been able to display larger toast messages with no problem before.

            To ensure that the app doesn't crash if being run on the same emulator I have commented out the + date that should be displayed when
            the selects with the today message.

            This issue does not happen at all when testing with my Pixel 2 XL or my Samsung Tab A8 device in devloper mode.

            Unfortunately I did not have time to troubleshoot this issue before the due date as I had been testing on my devices
            due to the instability  and unreliability of the Android Studio emulators

         */

        todayBtn.setOnClickListener(click -> {Toast.makeText(this, "today" /* + date*/, Toast.LENGTH_SHORT).show(); startActivity(nextActivity);});

        Intent pickedDateActivity = new Intent(this, PickDateActivity.class);
        PickDateBtn.setOnClickListener(Click -> startActivity(pickedDateActivity));

        Intent savedActivity = new Intent(this, SavedImages.class);
        SavedImageBtn.setOnClickListener(click -> startActivity(savedActivity));

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

        activityTitle.setText(R.string.mainMenuHelpMenuDialogTitle);
        info.setText(R.string.helpMenuTitle);

        paragraphOne.setText(R.string.mainMenuHelpMenuParaOne);
        paragraphTwo.setText(R.string.mainMenuHelpMenuParaTwo);


        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        alertBuilder.setView(alert_dialog_layout);
        alertBuilder.setNegativeButton(R.string.helpMenuCloseBtnText, (click, arg) -> { });
        alertBuilder.create().show();
    }

}