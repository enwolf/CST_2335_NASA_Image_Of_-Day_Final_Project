package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 300;

    private final Intent openBrowser = new Intent(Intent.ACTION_VIEW);

    private SQLiteDatabase dbObject;
    private TextView viewExp;
    private TextView viewURL;
    private TextView viewDate;
    private ImageView viewImage;
    private Button openHD_URL_Btn;
    private Button saveBtn;
    private Bitmap spaceImage;
    private ProgressBar progBar;
    private String Date;
    private String Explanation;
    private String URLPath;
    private String urlHdPath;
    private String FileNameWithExtension;
    private String FileNameWithoutExtension;
    private String FileName;
    private String Title;
    private String passedDate;
    private Intent pickedDateValues;


    String stringDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    String[] stringDateArray = stringDate.split("-");

    Context context = this;

    /*
    private Calendar myCal;
    private String curYear;
    private int curMonth;
    private int curDay;
    private Date currentTime = Calendar.getInstance().getTime();
    private SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String curTimeCorrectFormat = myDateFormat.format(currentTime);
    Date currentTimeFormatted = null;


            {
                try {
                    currentTimeFormatted = myDateFormat.parse(curTimeCorrectFormat);

                    curYear = currentTimeFormatted.toString().substring(currentTimeFormatted.toString().lastIndexOf(' ') + 1, currentTimeFormatted.toString().length() );
                    curMonth = currentTimeFormatted.getMonth() + 1;
                    curDay = currentTimeFormatted.getDay();
                    Log.i("Test Date", "currentTimeFormatted toString() = " + currentTimeFormatted.toString() );
                    Log.i("Test Date", "currentTimeFormatted get year = " + curYear);
                    Log.i("Test Date", "currentTimeFormatted get month = " + (curMonth));
                    Log.i("Test Date", "currentTimeFormatted get day = " + curDay );



                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
*/



    /* ImageViewActivity.java onCreate()


      After loading activity_image_view.xml we link our views with their java objects
      progBar set to VISIBLE and progress is set to 0 .

      Checks to see if date has been passed from PickedDateActivity, if so loads URL with user
      chosen date, if the date is null then it loads the image for today's date.

      Then an object of our getNasaDataJSON class is created and pass the URl we wil use to parse data
      from which is set to currently load todays date.

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

        viewDate = findViewById(R.id.dateTextXML);
        viewURL = findViewById(R.id.urlTextXML);
        viewExp = findViewById(R.id.expTextXML);
        viewImage = findViewById(R.id.nasaImageContainer);
        saveBtn = findViewById(R.id.saveButton);
        progBar = findViewById(R.id.viewActivityProBar);
        progBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        progBar.setVisibility(View.VISIBLE);

        progBar.setProgress(0);

        openHD_URL_Btn = findViewById(R.id.hdUrlButton);

        openDatabaseConnection();


        pickedDateValues = getIntent();
        passedDate = pickedDateValues.getStringExtra("date");

        if(passedDate != null)
        Log.i("Test Picked Date", passedDate);

        getNasaDataJSON queryJSON = new getNasaDataJSON();

        if(passedDate != null) {
            Log.i("Test Picked Date", passedDate);
            queryJSON.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + passedDate);
        } else {
            queryJSON.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]);
        }

        //+ stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]




        //instead of url could be used to load a fragment showing a larger version of the image, which means you could show more of a thumb nail.
        //viewImage.setOnClickListener(Click -> startActivity(openBrowser));

        Log.i("Test Date", stringDate);
        Log.i("Test Date", "Year = " + stringDateArray[0]);
        Log.i("Test Date", "Month = " + stringDateArray[1]);
        Log.i("Test Date", "Day = " + stringDateArray[2]);
        Log.i("Test Date", stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]);
        Log.i("Test Date", "Todays Date URL = https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + stringDateArray[0] + "-" + stringDateArray[1] + "-" + stringDateArray[2]);





        openHD_URL_Btn.setOnClickListener(Click -> startActivity(openBrowser) );

        saveBtn.setOnClickListener(Click ->{

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


        });
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

        HttpURLConnection urlConnection: used to open connection with URL passed into method as parmater.
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

}







