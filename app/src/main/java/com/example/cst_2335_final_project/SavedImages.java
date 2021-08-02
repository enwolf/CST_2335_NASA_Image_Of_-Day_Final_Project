/*
@author: Robin Phillis
@project: CST_2335_Final_Project

*/


package com.example.cst_2335_final_project;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedImages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    //Activity request code for use with onActivityResults.
    public static final int ACTIVITY_REQUEST_CODE = 400;

    // String labels for Bundle Data, used for passing and retrieving stored values
    public final static String IMAGE_DATA_ID = "ID";
    public final static String IMAGE_DATA_DATE = "DATE";
    public final static String IMAGE_DATA_TITLE = "TITLE";
    public final static String IMAGE_DATA_EXPLANATION = "EXPLANATION";
    public final static String IMAGE_DATA_URL = "URL";
    public final static String IMAGE_DATA_HD_URL = "HD_URL";
    public final static String IMAGE_DATA_FILENAME = "FILENAME";
    public final static String IMAGE_DATA_IMAGE_FILE_PATH = "FILEPATH";


    private SQLiteDatabase dbObject;
    private ArrayList<ImageData> imageDataArrayList = new ArrayList<>(Arrays.asList());
    private ArrayListAdapter imageDataArrayListAdapter;
    private File dirPath;
    private Toolbar toolbar;



    /* SavedImages.java / activity_saved_images.xml onCreate()

       After loading our activity.xml file we initialize imageDataArrayListAdapter to be a new ArrayListAdapter.
       Then set dirPath to the path that is used when saving images to application cache sub directory ImageFolder
       Links listView with view in xml layout file then attach imageDataArrayListAdapter to the list view
       The we call loadDataFromDatabase() to populate any saved data into our listview.
       Set setOnItemClickListener and setOnItemLongClickListener to listview and depending on the type of click
       creates the correct Alert Dialog or loads a fragment in response. A long click will load the create alert dialog to
       delete item from database/listViewArray and a short click will display ImageData Objects details in a fragment..


    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_images);

        imageDataArrayListAdapter  = new ArrayListAdapter();
        dirPath = new File(getCacheDir(), "imageFolder");

        Log.i("Test dirPath ", dirPath.toString());
        Log.i("Test dirPath ", dirPath.getAbsolutePath());



        ListView listViewImageData = findViewById(R.id.savedImageListViewXML);
        listViewImageData.setAdapter(imageDataArrayListAdapter);

        //Sets up Toolbar Menu
        toolbar = findViewById(R.id.savedImagesToolBarXML);
        toolbar.setTitle(R.string.toolbarTitleSavedImagesActivity);
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


        loadDataFromDatabase();


        listViewImageData.setOnItemClickListener((list, view, indexOfElement, databaseID) -> {

            //this is where we will open our fragment.
            createFragment(indexOfElement);

            //createItemDetailAlertDialog(indexOfElement);
        });


        listViewImageData.setOnItemLongClickListener( (AdapterView, View, indexOfElement, databaseID) -> {
            createDeleteItemAlertDialog(indexOfElement);

            return true;
        });


    }

    /* createFragment()

       Parameter: int indexOfElement, the index of the element clicked/pressed on in the ListView
                  used to retrieve data values of ImageData Object at this index.

       Creates Bundle object dataToSend, which stores values as a key/value item. we use the class
       wide string variables as our key/label's and the specific ImageDate Objects values.

       Creates intent for the activity which will load the fragment in its FrameLayout, and includes
       the bundle object as an extra to be sent.

     */


    private void createFragment(int indexOfElement){

        Bundle dataToSend = new Bundle();

        dataToSend.putLong(IMAGE_DATA_ID,  imageDataArrayList.get(indexOfElement).getId());
        dataToSend.putString(IMAGE_DATA_DATE,  imageDataArrayList.get(indexOfElement).getDate());
        dataToSend.putString(IMAGE_DATA_TITLE,  imageDataArrayList.get(indexOfElement).getTitle());
        dataToSend.putString(IMAGE_DATA_EXPLANATION,  imageDataArrayList.get(indexOfElement).getExplanation());
        dataToSend.putString(IMAGE_DATA_URL,  imageDataArrayList.get(indexOfElement).getUrlString());
        dataToSend.putString(IMAGE_DATA_HD_URL,  imageDataArrayList.get(indexOfElement).getHdUrlString());
        dataToSend.putString(IMAGE_DATA_FILENAME,  imageDataArrayList.get(indexOfElement).getFileName());
        dataToSend.putString(IMAGE_DATA_IMAGE_FILE_PATH,  dirPath.toString());


        /*
        if(isTablet){

            detailsPaneFragment = new DetailsFragment();
            detailsPaneFragment.setArguments(msgDataToSend);

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutFragment, detailsPaneFragment)
                    .commit();
        }else{

        }

        */


            Intent savedImagesFragmentActivity  = new Intent(SavedImages.this, SaveImagesFragmentActivity.class);
            savedImagesFragmentActivity.putExtras(dataToSend);
            startActivity(savedImagesFragmentActivity);



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


    /* ArrayListAdapter class is a custom BaseAdapter class for use with ListView.

       getCount()
       Behavior: returns number of items in ArrayList

       getItem()
       Behavior: returns object stored at index of arrayList

       getView() This is were we inflate the layout for our listView item to be displayed/formatted

       Behavior:

       First we inflate newView with listview_layout.xml which is the layout used to display the data
       stored by the ImageData Object in our ArrayList and then initialize our current ImageData object
       so we can pull the stored data into our fileName variable and inflated view's ImageView and TextViews
       Once TextView text is set we call loadImageFromDirectoryIntoView() to load the saved image file
       into the inflated ImageView and newView is returned.

       Method Variables:
       View newView: view to be returned and displayed in listView.
       ImageData: currentImageDataObject, is the current object at index position of list view.
       String fileName: is set to value of file name stored in ImageData object, will be used to save
       to application directory.
       TextView titleText, urlText, dateText used to set text values of TextViews



     */

    private class ArrayListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageDataArrayList.size();
        }

        @Override
        public Object getItem(int indexOfElement) {
            return imageDataArrayList.get(indexOfElement);
        }

        @Override
        public long getItemId(int indexOfElement) {
            return (long) indexOfElement;
        }

        @Override
        public View getView(int indexOfElement, View currentView, ViewGroup parent) {

            View newView = currentView;

            //Inflate view with layout
            newView = getLayoutInflater().inflate(R.layout.listview_layout, parent, false);
            //Pulls ImageData Object from current index to retrieve its file name.
            ImageData currentImageDataObject = (ImageData) getItem(indexOfElement);
            String fileName = currentImageDataObject.getFileName();

            //Link View with XML
            TextView titleText = (TextView) newView.findViewById(R.id.listViewLayoutTitleXML);
            TextView dateText  = (TextView) newView.findViewById(R.id.listViewLayoutDateXML);


            //Set Text values
            titleText.setText(currentImageDataObject.getTitle());
            dateText.setText(currentImageDataObject.getDate());


            //load saved image
            loadImageFromDirectoryIntoView(newView, fileName, dirPath);

            return newView;
        }
    }

    /* loadImageFromDirectoryIntoView()

       Parameters:
       View vew, view that will have the image displayed
       String fileName: image file name to be loaded into view
       File path: path to directory image will be loaded from

       Behavior: This method takes a view to load an image file into, the file name to be loaded,
       and the path to that fileName and then sets the imageFile to be set within and ImageView.

       Method variables:
       File imageFile - takes String and File from method parameters to create a file representing
       the image we would like to load.
       Bitmap imageToLoad - Takes our imageFile and stores it as a BitMap Object
       ImageView imageFrame - imageView to load our Bitmap object into.

       Exception: FileNotFoundException thrown if no file found to load into view.

     */

    private void loadImageFromDirectoryIntoView(View view, String fileName, File path){

      File imageFile = new File(path, fileName);

      try{
            Bitmap imageToLoad = BitmapFactory.decodeStream(new FileInputStream(imageFile));

            ImageView imageFrame = (ImageView) view.findViewById(R.id.listViewLayoutImageXML);
            imageFrame.setImageBitmap(imageToLoad);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /* Method loadDataFromDatabase() loads saved data from database so it can be displayed in ListView

       Behavior: Creates a connection to writable database to be used by the SQL Lite Database Object
       An Array[] of String is populate with DB column names (Final public values stored in DatabaseOpener.java)
       for the database and then create a Cursor object which will return all rows saved with in the database one row at a time.
       To extract the values so we can work with them we first create a number of int variables
       to store the index of each column from the Cursor. We then loop through the Cursor once for each result stored within
       and pass them into our imageDataArrayList by creating a new ImageData Object with the Cursor data passed in as
       parameter.

       printCursor() is called for debugging purposes

       Method Variables:

       DatabaseOpener databaseOpener is used to open a data base connection for our DB Object

       String [] databaseColumnsArray: stores array of database column names.

       Cursor queryResults: cursor object to store data retrieved from database.

       int idColIndex, dateColIndex, explanationColIndex, hdUrlColIndex, titleColIndex, urlColIndex, filenameColIndex
       are used to hold index values for column we are pulling data from with in our database.



     */

    private void loadDataFromDatabase(){

        DatabaseOpener databaseOpener = new DatabaseOpener(this);
        dbObject = databaseOpener.getWritableDatabase();

        //Access DatabaseOpener superclass or class object to get the public variables
        //That we set with our Column Name values

        String [] databaseColumnsArray = { DatabaseOpener.COL_ID,
                                           DatabaseOpener.COL_DATE,
                                           DatabaseOpener.COL_EXPLANATION,
                                           DatabaseOpener.COL_HD_URL,
                                           DatabaseOpener.COL_TITLE,
                                           DatabaseOpener.COL_URL,
                                           DatabaseOpener.COL_FILENAME   };


        //Loads all rows into Cursor
        Cursor queryResults = dbObject.query(false, DatabaseOpener.TABLE_NAME, databaseColumnsArray,null,null,null,null,null,null);

        //Get index of columns from cursor again using DatabaseOpener class.
        int idColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_ID);
        int dateColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_DATE);
        int explanationColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_EXPLANATION);
        int hdUrlColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_HD_URL);
        int titleColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_TITLE);
        int urlColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_URL);
        int filenameColIndex = queryResults.getColumnIndex(DatabaseOpener.COL_FILENAME);

        //loop while Cursor has next row.
        while(queryResults.moveToNext())
        {
            //add data stored in our columns based on there index in our variables.
            long id = queryResults.getLong(idColIndex);
            String date = queryResults.getString(dateColIndex);
            String explanation = queryResults.getString(explanationColIndex);
            String hdUrl = queryResults.getString(hdUrlColIndex);
            String title = queryResults.getString(titleColIndex);
            String url = queryResults.getString(urlColIndex);
            String fileName = queryResults.getString(filenameColIndex);

            //creates and adds ImageData object to array list with current row values.
            imageDataArrayList.add(new ImageData(id,date,explanation,hdUrl,title,url,fileName));

        }
        //used for debugging
        printCursor(queryResults, dbObject.getVersion());

    }

    /* printCursor() prints information for debugging.

       Parameters:
       Cursor myCursor: cursor object that will have its values printed
       int databaseVersion: database version we are current working on.

       Behavior: This method prints information about Cursor object and its stored values as well
       as database vVersion into logcat.

       Method Variables:
       String [] colNames Stores column names in myCursor
       List<String> colNamesList takes String[] and loads them into ArrayList
       String colOne, colTwo, colThree, colFour, colFive, colSix, colSeven used store and print
       current column values into logcat.

     */

    private void printCursor(Cursor myCursor, int databaseVersion){

        String [] colNames = myCursor.getColumnNames();
        List<String> colNamesList = Arrays.asList(colNames);

        Log.d( "ImageViewActivity.java", "Database Version # " + databaseVersion );
        Log.d( "ImageViewActivity.java", "Number Of Columns in Cursor: " + myCursor.getColumnCount() );
        Log.d( "ImageViewActivity.java", "Columns in Cursor: " + colNamesList.toString() );
        Log.d( "ImageViewActivity.java", "Number of results in cursor: " + myCursor.getCount() );

        //if statement required to avoid cursorIndexOutOfBounds Error
        if(myCursor != null && myCursor.moveToFirst() ){

            String colOne;
            String colTwo;
            String colThree;
            String colFour;
            String colFive;
            String colSix;
            String colSeven;

            do{
                colOne   = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_ID));
                colTwo   = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_DATE));
                colThree = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_EXPLANATION));
                colFour  = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_HD_URL));
                colFive  = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_TITLE));
                colSix   = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_URL));
                colSeven = myCursor.getString(myCursor.getColumnIndex(DatabaseOpener.COL_FILENAME));

                Log.d("ImageViewActivity.java", "Row of results in the cursor - ID: " + colOne + " Date: " + colTwo + " Explanation " + colThree +
                                                         " HD URL: " + colFour + " Title " + colFive + " URL " + colSix + " Filename " + colSeven   );

            }
            while (myCursor.moveToNext());        }

        Log.d("ImageViewActivity.java", "Results in cursor: " + DatabaseUtils.dumpCursorToString(myCursor));
    }

    /*  createDeleteItemAlertDialog()

        parameters: int

        Behavior: takes int value for index of current item of list view, displays an AlertDialog
        showing ID detail about current ImageDataObject and its position within the Arraylist / ListView
        and allows the user to delete if the PositiveButton is clicked or closes the AlertDialog if the
        NegativeButton is pressed.

        Method Objects / Variables:
        ImageData imageDataObjToDelete is set to the object at the index value passed into the method
        as a parameter.

        AlertDialog.Builder alertBuilder used to construct the AlertDialog window and its displayd values
        and buttons.

     */

    private void createDeleteItemAlertDialog(int indexOfElement){
        //Gets message object at index of ListView element that was pressed.
        ImageData imageDataObjToDelete = imageDataArrayList.get(indexOfElement);

        //Gets layout for alert window.
       // View alert_layout = getLayoutInflater().inflate(R.layout.alert_dialog_layout,null);

        //Create View objects to output text values.
        //TextView msgValue = alert_layout.findViewById(R.id.msgTextValue);
        //TextView listViewMsg = alert_layout.findViewById(R.id.listViewMsg);
       // TextView databaseMsg = alert_layout.findViewById(R.id.databaseMsg);

        //Set View text values
        //msgValue.setText(selectedMessage.getMsgValue());
        //listViewMsg.setText("The position of your item is: " + indexOfViewElement);
        //databaseMsg.setText("The database ID: " + selectedMessage.getId() );

        //Build alert window.
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Do you want to delete this data?");
        //alertBuilder.setView(alert_layout);
        alertBuilder.setMessage("The position of your item  is:" + indexOfElement);

        alertBuilder.setPositiveButton("Yes", (click, arg) -> {

            deleteImageData(imageDataObjToDelete);
            imageDataArrayList.remove(indexOfElement);
            imageDataArrayListAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Removed from favorites, ID = " + imageDataObjToDelete.getId(), Toast.LENGTH_LONG).show();

            /*
            //removes fragment detailsPaneFragment from view if larger device detected.
            if(isTablet){
                //creates SupportFragmentManager without the need to initialize to a variable.
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(detailsPaneFragment)
                        .commit();
            }
            */


        });

        alertBuilder.setNegativeButton("No", (click, arg) -> { });
        alertBuilder.create().show();
    }

    /*  createItemDetailAlertDialog()

        parameters: int

        Behavior: takes int value for index of current item of list view, displays an AlertDialog
        showing detail about current ImageDataObject. Title is set to Image Data object title value and
        data values displayed are listview index position, database ID value, URL, HD URL, Image Date and fileName
        if the NegativeButton is pressed the AlertDialog is closed.

        Method Objects / Variables:
        ImageData imageDataObj is set to the object at the index value passed into the method
        as a parameter.

        AlertDialog.Builder alertBuilder used to construct the AlertDialog window and its displayd values
        and buttons.
     */

    private void createItemDetailAlertDialog(int indexOfViewElement){

        ImageData imageDataObj = imageDataArrayList.get(indexOfViewElement);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(imageDataObj.getTitle());
        alertBuilder.setMessage( imageDataObj.getExplanation() + "\n\n" + "The index of this item in SavedImage_DB is: " + imageDataObj.getId()
                                                               + "\n\n" + "The index of this item in Listview is: " + indexOfViewElement
                                                               + "\n\n" + "The URL of saved image is: " + imageDataObj.getUrlString()
                                                               + "\n\n" + "The HD URL of saved image is: " + imageDataObj.getHdUrlString()
                                                               + "\n\n" + "The Date of saved image is: " + imageDataObj.getDate()
                                                               + "\n\n" + "The Filename of saved image is: " + imageDataObj.getFileName()

        );
        alertBuilder.setNegativeButton("Close", (click, arg) -> { });
        alertBuilder.create().show();
    }

    // deleteImageData() method to delete row storing ImageData object data from database by its database ID value.

    private void deleteImageData(ImageData imageDataToDelete){
        dbObject.delete(DatabaseOpener.TABLE_NAME, DatabaseOpener.COL_ID + "= ?", new String[] {Long.toString(imageDataToDelete.getId())});
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
        paragraphOne.setText(R.string.savedImageHelpMenuParaOne);
        paragraphTwo.setText(R.string.savedImageHelpMenuParaTwo);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.savedImageHelpMenuDialogTitle);
        alertBuilder.setView(alert_dialog_layout);
        alertBuilder.setNegativeButton("Close", (click, arg) -> { });
        alertBuilder.create().show();
    }






}//end of file