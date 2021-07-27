package com.example.cst_2335_final_project;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private Toolbar toolbar;

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
        todayBtn.setOnClickListener(click -> startActivity(nextActivity));

        Intent pickedDateActivity = new Intent(this, PickDateActivity.class);
        PickDateBtn.setOnClickListener(click -> startActivity(pickedDateActivity));

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
        String message = null;
        //Finds menu items from XML file and handles a case for item selected.
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.toolBarMainMenuIcon:
                Intent mainMenu = new Intent(this, MainMenu.class);
                startActivity(mainMenu);
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
                Intent mainMenu = new Intent(this, MainMenu.class);
                startActivity(mainMenu);
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


    private void createAlertDialogHelpWindow(){


        View alert_dialog_layout = getLayoutInflater().inflate(R.layout.help_menu_alert_dialog_layout,null);

        TextView title = alert_dialog_layout.findViewById(R.id.helpMenuTitleXMl);
        TextView paragraphOne = alert_dialog_layout.findViewById(R.id.helpMenuItemOneXML);
        TextView paragraphTwo = alert_dialog_layout.findViewById(R.id.helpMenuItemTwoXML);
        TextView paragraphThree = alert_dialog_layout.findViewById(R.id.helpMenuItemThreeXML);

        title.setText(R.string.helpMenuTitle);
        paragraphOne.setText(R.string.mainMenuHelpMenuParaOne);
        paragraphTwo.setText(R.string.mainMenuHelpMenuParaTwo);
        paragraphThree.setText((R.string.mainMenuHelpMenuParaThree));


        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.mainMenuHelpMenuDialogTitle);
        alertBuilder.setView(alert_dialog_layout);
        alertBuilder.setNegativeButton("Close", (click, arg) -> { });
        alertBuilder.create().show();
    }

}