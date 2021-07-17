package com.example.cst_2335_final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedImages extends AppCompatActivity {

    public static final int REQUEST_CODE = 400;

    private SQLiteDatabase dbObject;
    private String fileName;
    private ArrayList<ImageData> imageDataArrayList = new ArrayList<>(Arrays.asList());
    private ArrayListAdapter imageDataArrayListAdapter;
    private File dirPath;

    //Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_images);

        imageDataArrayListAdapter  = new ArrayListAdapter();
        dirPath = new File(getCacheDir(), "imageFolder");

        ListView listViewImageData = findViewById(R.id.savedImageListViewXML);
        listViewImageData.setAdapter(imageDataArrayListAdapter);

        loadDataFromDatabase();

        listViewImageData.setOnItemLongClickListener( (AdapterView, View, indexOfViewElement, databaseID) -> {
            createAlertDialog(indexOfViewElement);

            return true;
        });


    }


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

            View newView;

            //Inflate view with layout
            newView = getLayoutInflater().inflate(R.layout.listview_layout, parent, false);
            //Pulls ImageData Object from current index to retrieve its file name.
            ImageData currentImageDataObject = (ImageData) getItem(indexOfElement);
            fileName = currentImageDataObject.getFileName();

            //Link View with XML
            TextView titleText = (TextView) newView.findViewById(R.id.listViewLayoutTitleXML);
            TextView dateText  = (TextView) newView.findViewById(R.id.listViewLayoutDateXML);
            TextView urlText =  (TextView) newView.findViewById(R.id.listViewLayoutUrlXML);

            //Set Text values
            titleText.setText(currentImageDataObject.getTitle());
            dateText.setText(currentImageDataObject.getDate());
            urlText.setText(currentImageDataObject.getUrlString());

            //load saved image
            loadImageFromDirectoryIntoView(newView, fileName, dirPath);

            return newView;
        }
    }

    private void loadImageFromDirectoryIntoView(View newView, String fileName, File path)
    {

      File imageFile = new File(path, fileName);

      try{
            Bitmap imageToLoad = BitmapFactory.decodeStream(new FileInputStream(imageFile));

            ImageView imageFrame = (ImageView) newView.findViewById(R.id.listViewLayoutImageXML);
            imageFrame.setImageBitmap(imageToLoad);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

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

    // Function to be used for debugging prints to logcat

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

    private void createAlertDialog(int indexOfViewElement){
        //Gets message object at index of ListView element that was pressed.
        ImageData imageDataObjToDelete = imageDataArrayList.get(indexOfViewElement);

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
        alertBuilder.setMessage("The position of your item  is:" + indexOfViewElement);

        alertBuilder.setPositiveButton("Yes", (click, arg) -> {

            deleteImageData(imageDataObjToDelete);
            imageDataArrayList.remove(indexOfViewElement);
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

    private void deleteImageData(ImageData imageDataToDelete){
        dbObject.delete(DatabaseOpener.TABLE_NAME, DatabaseOpener.COL_ID + "= ?", new String[] {Long.toString(imageDataToDelete.getId())});
    }


}//end of file