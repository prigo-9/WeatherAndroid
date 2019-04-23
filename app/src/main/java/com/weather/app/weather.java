package com.weather.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class weather extends AppCompatActivity {
    String TAG = "weatherLOG";
    String latitude;
    String longitude;
    String api_key = "031de059999883c6d2fbbdb6a45ddbfe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent callingIntent = getIntent();
        latitude = callingIntent.getStringExtra("latitude");
        longitude = callingIntent.getStringExtra("longitude");
        TextView latitude_value_tv = findViewById(R.id.latitude_value);
        TextView longitude_value_tv = findViewById(R.id.longitude_value);
        latitude_value_tv.setText(latitude);
        longitude_value_tv.setText(longitude);

        make_weather_api_call(api_key,latitude,longitude);
    }

    private void make_weather_api_call(String api_key,String lat, String lon){
        String url = "https://api.darksky.net/forecast/" +
                api_key+"/" +
                lat + "," + lon;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String responseFromAPI = response.body().string().toString();

                if (!response.isSuccessful()) {
                    Log.d(TAG,"Something goes wrong : " + responseFromAPI);
                } else {
                    Log.d(TAG,responseFromAPI);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hide_all_progressbars();
                            processJsonResponse(responseFromAPI);
                        }
                    });

                }
            }
        });
    }

    private void processJsonResponse(String api_response_string){
        JsonParser parser = new JsonParser();
        JsonObject json_complete_response = parser.parse(api_response_string).getAsJsonObject();
        String location = json_complete_response.get("timezone").getAsString().toString();
        JsonObject json_current_weather = json_complete_response.get("currently").getAsJsonObject();

        String time = json_current_weather.get("time").getAsString().toString();
        String temperature = json_current_weather.get("temperature").getAsString().toString();
        String humidity = json_current_weather.get("humidity").getAsString().toString();
        String pressure = json_current_weather.get("pressure").getAsString().toString();
        String windSpeed = json_current_weather.get("windSpeed").getAsString().toString();
        String ozone = json_current_weather.get("ozone").getAsString().toString();
        String visibility = json_current_weather.get("visibility").getAsString().toString();

        TextView temperature_tv = findViewById(R.id.temperature_textView);
        TextView humidity_tv = findViewById(R.id.humidity_textView);
        TextView pressure_tv = findViewById(R.id.pressure_textView);
        TextView windspeed_tv = findViewById(R.id.windspeed_textView);
        TextView ozone_tv = findViewById(R.id.ozone_textView);
        TextView visibility_tv = findViewById(R.id.visibility_textView);
        TextView location_tv = findViewById(R.id.location_value);

        temperature_tv.setText(temperature+" Celsius ");
        humidity_tv.setText(humidity);
        pressure_tv.setText(pressure+" Hectopascals");
        windspeed_tv.setText(windSpeed+" Km/Hour");
        ozone_tv.setText(ozone);
        visibility_tv.setText(visibility+ " Kilometers");
        location_tv.setText(location);
        //JsonObject json_hourly_weather = json_complete_response.get("hourly").getAsJsonObject();

    }

    private void hide_all_progressbars(){
        findViewById(R.id.progressBar1).setVisibility(View.GONE);
        findViewById(R.id.progressBar2).setVisibility(View.GONE);
        findViewById(R.id.progressBar3).setVisibility(View.GONE);
        findViewById(R.id.progressBar4).setVisibility(View.GONE);
        findViewById(R.id.progressBar5).setVisibility(View.GONE);
        findViewById(R.id.progressBar6).setVisibility(View.GONE);
    }
}
