package com.example.weather;

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
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText ;
    TextView weatherText;
    String message = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        weatherText = findViewById(R.id.weatherText);

    }
    public void findWeather(View view) {
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter A City", Toast.LENGTH_SHORT).show();


        } else {


            try {
                downloadTask task = new downloadTask();
                String encodedCity = URLEncoder.encode(editText.getText().toString(), "UTF-8");
                task.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=439d4b804bc8187953eb36d2a8c26a02");
                InputMethodManager mnr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mnr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't find the city :(", Toast.LENGTH_SHORT).show();

            }

        }
    }



      public class downloadTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;


            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't find the city :(", Toast.LENGTH_SHORT).show();
                return null;
            }


        }

            @Override
            protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather content", weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);


                for (int i=0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message += main + ":"  + description + "\r\n";
                    }

                }
                if (!message.equals("")){
                    weatherText.setText(message);
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't find the city :(", Toast.LENGTH_SHORT).show();

                }


            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Couldn't find the city :(", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }

    }
}

