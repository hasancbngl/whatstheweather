package com.cobanogluhasan.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView weatherCondition;

    public void findWeather(View view) {

        //remove keybord after its tapped
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);

        try {
            String enCodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");


            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q="+ enCodedCityName +"&appid=d5243e88e613b56d105b9c23787d59e1");


        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Couldnt find weather.",Toast.LENGTH_LONG).show();
        }








    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName=(EditText) findViewById(R.id.cityName);
        weatherCondition=(TextView) findViewById(R.id.weatherCondition);


    }

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int data=inputStreamReader.read();

                while (data != -1) {

                    char current = (char) data;

                    result +=  current;

                    data=inputStreamReader.read();

                }

                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Couldnt find weather.",Toast.LENGTH_LONG).show();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String message="";

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfoo = jsonObject.getString("weather");

                Log.i("website content:", weatherInfoo);

                JSONArray array = new JSONArray(weatherInfoo);

                for(int i=0; i<array.length(); i++) {

                    JSONObject jsonPart = array.getJSONObject(i);

                  //  weatherCondition.setText(cityName.getText().toString() + jsonPart.getString("main") + " " + jsonPart.getString("description"));

                    String main="";
                    String description="";

                    main=jsonPart.getString("main");
                    description=jsonPart.getString("description");


                    if(main != "" &&  description != "") {

                        message+= main + ":" + description + "\r\n";



                    }


                    if(message != "") {

                        weatherCondition.setText(message);

                    }
                    else {    Toast.makeText(getApplicationContext(), "Couldnt find weather.",Toast.LENGTH_LONG).show();}


                    Log.i("main", jsonPart.getString("main") );
                    Log.i("description", jsonPart.getString("description"));


                }

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Couldnt find weather.",Toast.LENGTH_LONG);
            }




        }
    }








}
