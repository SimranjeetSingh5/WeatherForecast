package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

    Button button;
    TextView weather;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        weather = findViewById(R.id.weatherInfo);
        editText = findViewById(R.id.cityPlainText);
    }

    public void getWeather(View view){

        try {
            DownloadTask task = new DownloadTask();

            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8";
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=439d4b804bc8187953eb36d2a8c26a02");

            weather.setVisibility(View.VISIBLE);

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT).show();

        }
    }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char curr = (char)data;
                    result += curr;
                    data =reader.read();
                }

                return result;

            }catch (Exception e){
                e.printStackTrace();

                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather is",weatherInfo);

                JSONArray weatherArray = new JSONArray(weatherInfo);

                String message = "";

                for(int i = 0; i < weatherArray.length(); i ++){
                    JSONObject jsonPart = weatherArray.getJSONObject(i);

                    String main  = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message += main + " : " + description;
//                        Toast.makeText("NO Information regarding" + ).show();
                    }
                }

                if(!message.equals("")){
                    weather.setText("But Weather there seems..\n"+ message);
                }else{
                    Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather",Toast.LENGTH_SHORT).show();
            }
        }
    }
}