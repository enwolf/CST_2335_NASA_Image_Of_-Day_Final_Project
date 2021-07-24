package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 300;

    private final Intent openBrowser = new Intent(Intent.ACTION_VIEW);
    private final String nasaApiKey = "0ZK9fHgnuczikynPWW6NolmHs5LqB6GzjZdDOoHC";

    private SQLiteDatabase dbObject;
    private TextView viewExp;
    private TextView viewURL;
    private TextView viewDate;
    private ImageView viewImage;
    private Button openHD_URL_Btn;
    private Button saveBtn;
    private Bitmap spaceImage;
    private Intent pickedDateValues;
    private Toolbar toolbar;
    private ProgressBar progBar;
    private String Date;
    private String Explanation;
    private String URLPath;
    private String urlHdPath;
    private String FileNameWithExtension;
    private String FileNameWithoutExtension;
    private String FileName;
    private String Title;
    private String passedDateEditText;
    private String passedDatePickerDate;




    private String stringDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    private String[] stringDateArray = stringDate.split("-");

    Context context = this;


    /* ImageViewActivity.java onCreate()


      After loading activity_image_view.xml we link our views with their java objects
      progBar set to VISIBLE and progress is set to 0 .

      Checks to see if date has been passed from PickedDateActivity, if so loads URL with user
      chosen date, if the date is null then it loads the image for today's date.

      Then an object of our getNasaDataJSON class is created and pass the URl we wil use to parse data
      from which is set to currently load today's date.

      openDatabaseConnection() is called on our dbObject so that the database connection is opened and
      we can save image + data into SavedImage_DB

      setOnClickListener is set to open default browser

      saveBtn setOnClickListener set to save data values into using ContentValues with DatabaseOpener
      column names to save values into SavedImage_DB

      createDirectoryAndSaveFile() is called to store current image into application cache sub
      directory and a Snackbar is displayed with an option to view your saved images.

    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        openHD_URL_Btn = findViewById(R.id.hdUrlButton);
        viewImage = findViewById(R.id.nasaImageContainer);
        progBar   = findViewById(R.id.viewActivityProBar);
        viewDate  = findViewById(R.id.dateTextXML);
        viewURL   = findViewById(R.id.urlTextXML);
        viewExp   = findViewById(R.id.expTextXML);
        saveBtn   = findViewById(R.id.saveButton);

        progBar.setVisibility(View.VISIBLE);
        progBar.setProgress(0);

        toolbar = findViewById(R.id.viewImageToolBarXML);
        toolbar.setTitle(R.string.toolbarTitleImageViewActivity);

        setSupportActionBar(toolbar);
        openDatabaseConnection();

        //Todo Maybe extract this to a method later
        pickedDateValues = getIntent();
        passedDateEditText = pickedDateValues.getStringExtra("date");
        passedDatePickerDate = pickedDateValues.getStringExtra("datePickerDate");

        if (passedDatePickerDate != null)
            Log.i("Test DatePicker Date", passedDatePickerDate);

        getNasaDataJSON queryJSON = new getNasaDataJSON();

        if (passedDatePickerDate != null){
            Log.i("Test DatePicker Date", passedDatePickerDate);
            queryJSON.execute("https://api.nasa.gov/planetary/apod?api_key=" + nasaApiKey + "&date=" + passedDatePickerDate);
        }else if(passedDateEditText != null) {
            Log.i("Test DatePicker Date", passedDateEditText);
            queryJSON.execute("https://api.nasa.gov/planetary/apod?api_key=" + nasaApiKey + "&date=" + passedDateEditText);
        }else{
            queryJSON.execute("https://api.nasa.gov/planetary/apod?api_key=" + nasaApiKey + "&date=" + stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]);
        }


        //+ stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]


        //Todo instead of url could be used to load a fragment showing a larger version of the image, which means you could show more of a thumb nail.
        //viewImage.setOnClickListener(Click -> startActivity(openBrowser));

        Log.i("Test Date", stringDate);
        Log.i("Test Date", "Year = " + stringDateArray[0]);
        Log.i("Test Date", "Month = " + stringDateArray[1]);
        Log.i("Test Date", "Day = " + stringDateArray[2]);
        Log.i("Test Date", stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]);
        Log.i("Test Date", "Todays Date URL = https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]);


        openHD_URL_Btn.setOnClickListener(Click -> startActivity(openBrowser) );

        saveBtn.setOnClickListener(Click -> saveButtonAction() );
    }

    // Inflate the menu items for use in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    //Type1  Type2   Type3
    private class getNasaDataJSON extends AsyncTask< String, Integer, String>
    {
           //Type1
            public String doInBackground(String ... args) {
                try {

                    //create a URL object of what server to contact:
                    URL url = new URL(args[0]);

                    JSONObject reportJSON = createObjectJSON(url);

                    Date = reportJSON.getString("date");
                    publishProgress(25);
                    URLPath = reportJSON.getString("url");

                    Explanation = reportJSON.getString("explanation");
                    urlHdPath = reportJSON.getString("hdurl");
                    Title = reportJSON.getString("title");

                    Log.i("Test URL PATH", "Value of HD URL PATH = " + urlHdPath);
                    publishProgress(50);

                    FileNameWithExtension = URLPath.substring(URLPath.lastIndexOf('/') + 1, URLPath.length());
                    FileNameWithoutExtension = FileNameWithExtension.substring(0, FileNameWithExtension.lastIndexOf('.'));

                    //sets url for intent object to load webpage in browser of HD image.
                    openBrowser.setData(Uri.parse(urlHdPath));

                    URL urlObject = new URL(URLPath);
                    //String testSplit = urlObject.getPath();

                    Log.i("ImageViewActivity", "explanation text: " + Explanation);

                    Log.i("URL TEST", "URL String path = " + URLPath);
                    Log.i("URL TEST", "URL getFile() = " + urlObject.getFile());
                    Log.i("URL TEST", "URL getPath() = " + urlObject.getPath());

                    Log.i("URL TEST", "URL getPath() = " + urlObject.getPath());
                    Log.i("FileWithExtension", "Path = " + FileNameWithExtension);
                    Log.i("FileWithoutExtension", "Path = " + FileNameWithoutExtension);

                    FileName = FileNameWithoutExtension + ".png";

                    URL imageDataURL = new URL(URLPath);
                    HttpURLConnection connectionImage = (HttpURLConnection) imageDataURL.openConnection();
                    connectionImage.connect();
                    int responseCode = connectionImage.getResponseCode();

                    if (responseCode == 200) {

                        spaceImage = BitmapFactory.decodeStream(connectionImage.getInputStream());
                        FileOutputStream outputStream = openFileOutput(FileName, Context.MODE_PRIVATE);
                        spaceImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();


                        publishProgress(75);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                publishProgress(100);
              return "Complete";
            }

                //Type 2
            public void onProgressUpdate(Integer ... args)
            {
                progBar.setVisibility(args[0]);
            }
            //Type3
            public void onPostExecute(String fromDoInBackground)
            {
                Log.i("HTTP", fromDoInBackground);

                viewDate.setText(Date);
                viewURL.setText(URLPath);
                viewExp.setText(Explanation);
                viewImage.setImageBitmap(spaceImage);

                progBar.setVisibility(View.INVISIBLE);

            }
        }

    /*  createObjectJSON()

        Parameters:
        URL url, this is the URl from which we will parse the data from and store within our JSON Object.

        Behavior: Takes a URl and use HttpURLConnection to open a connection with the address stored
        in that URL and process the data provided from the URL as an input stream which is passed
        into our BufferedReader before being assemble as a String which is then used to generate
        the JSON Object to be returned by this method.

        Method variables:

        HttpURLConnection urlConnection: used to open connection with URL passed into method as parameter.
        InputStream urlResponse, stores the response from the URL as an input stream
        BufferedReader bufferedReader: takes the urlResponse as an input stream.
        StringBuilder stringBuilder: stores one  the data from the bufferedReader into

        String result: takes the values stored in our stringBuilder and reads them one line at a time
        and assemble them into a results string which we use to build our JSON Object form

        JSONObject reportJSON: creates our JSON object from result which is then returned by the method.

 */

    public JSONObject createObjectJSON(URL url) throws IOException, JSONException {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream urlResponse = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlResponse, "UTF-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();

            String currentLine = null;

            while ((currentLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(currentLine + "\n");
                Log.i("createObjectJSON()", "CurrentLine: " + currentLine) ;
            }

            String result = stringBuilder.toString();

            JSONObject reportJSON = new JSONObject(result);

            return reportJSON;

        }

    /*  openDatabaseConnection()


        Behavior: opens connection for writable Database on db object take as parameter

    */

    private void openDatabaseConnection(){

        DatabaseOpener databaseOpener = new DatabaseOpener(this);
        dbObject = databaseOpener.getWritableDatabase();
    }

    /*  createDirectoryAndSaveFile()

        Parameters:
        BitMap imageToSave the image we wish to save.
        String fileName: the filename that the image is saved as.

        Behavior: creates directory if it does not exist current, delete file if duplicate is found,
        Stores image File in application cache sub directory imageFolder.

        Method Variables:

        File myDir: represents path to directory we want to save our file to
        File file: file that we wish to save into our application director


        Exception: handles FileNotFoundException when attempting to save image file

     */


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File myDir = new File(getCacheDir(), "imageFolder");

        if (!myDir.exists()) {
            File imageFolder = new File(getCacheDir(), "imageFolder");
            imageFolder.mkdirs();
            Log.i("createDirAndSaveFile", "Created Dirctory: imageFolder" ) ;
        }

        File file = new File(new File(getCacheDir(), "imageFolder"), fileName);

        if (file.exists()) {
            file.delete();
            Log.i("createDirAndSaveFile", "Deleted Duplicate Filename " + fileName);
        }

        try {

            FileOutputStream outputStream = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Log.i("createDirAndSaveFile", fileName + " saved to imageFolder Dir" ) ;

        } catch (Exception FileNotFoundException) {
            FileNotFoundException.printStackTrace();
        }
    }

    /* saveButtonAction()

       Variables: ContentValues newRowValues stores dataValues to be inserted into database
                  Intent savedActivity used with Snackbar to send user to the specific activity.

       Method starts by passing data into newRowValues using .put we call DatabaseOpener calle's
       static values for column name, and the data to be placed in that column.

       once all values have inserted into  newRowValues we insert them into the database by calling
       the DB object's insert method, taking the table name and values to be inserted.

       We then updated the user with a snake bar confirming item has been saved and present's a button
       which will take them to the SavedImages Activity.

     */

    private void saveButtonAction(){

        //ContentValues used to hold values to be entered into database.
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(DatabaseOpener.COL_DATE, Date);
        newRowValues.put(DatabaseOpener.COL_EXPLANATION, Explanation);
        newRowValues.put(DatabaseOpener.COL_HD_URL, urlHdPath);
        newRowValues.put(DatabaseOpener.COL_TITLE, Title);
        newRowValues.put(DatabaseOpener.COL_URL, URLPath);
        newRowValues.put(DatabaseOpener.COL_FILENAME, FileName);

        long newID = dbObject.insert(DatabaseOpener.TABLE_NAME,null, newRowValues);

        createDirectoryAndSaveFile(spaceImage,FileName);

        Intent savedActivity = new Intent(this, SavedImages.class);
        Snackbar.make(saveBtn, getResources().getString(R.string.snackbarMessage) , Snackbar.LENGTH_LONG)
                .setAction(getResources().getString(R.string.snackbarCallToAction), click-> startActivity(savedActivity))
                .show();
    }

}







