package com.example.cst_2335_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageViewActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 300;


    private TextView viewExp;
    private TextView viewURL;
    private TextView ViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ViewDate = findViewById(R.id.dateTextXML);
        viewURL = findViewById(R.id.urlTextXML);
        viewExp = findViewById(R.id.expTextXML);






        getNasaDataJSON queryJSON = new getNasaDataJSON();
        queryJSON.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=2020-07-10");



    }


        //Type1     Type2   Type3
        private class getNasaDataJSON extends AsyncTask< String, Integer, String>
        {
            String Explanation;
            String Date;
            String URL;

            //Type3                Type1
            public String doInBackground(String ... args)
            {
                try {

                    //create a URL object of what server to contact:
                    URL url = new URL(args[0]);

                    JSONObject reportJSON = createObjectJSON(url);

                    Date = reportJSON.getString("date");
                    URL = reportJSON.getString("url");
                    Explanation = reportJSON.getString("explanation");

                    Log.i("MainActivity", "The uv is now: " + Explanation) ;



                }
                catch (Exception e)
                {

                }

                return "Done";
            }

            //Type 2
            public void onProgressUpdate(Integer ... args)
            {

            }
            //Type3
            public void onPostExecute(String fromDoInBackground)
            {
                Log.i("HTTP", fromDoInBackground);

                ViewDate.setText(Date);
                viewURL.setText(URL);
                viewExp.setText(Explanation);

            }
        }


        public JSONObject createObjectJSON(URL url) throws IOException, JSONException {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream response = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
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

}







