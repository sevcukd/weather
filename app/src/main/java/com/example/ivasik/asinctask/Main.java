package com.example.ivasik.asinctask;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends AppCompatActivity {
    private String parsingData = "";
    private TextView tvCity, tvWeathere, tvTemp, tvHumidity, tvWind, tvTempMain;
    private ImageView imageView, warm, imageView2, warm2;
    private Button button;
    private EditText city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button1);
        tvCity = findViewById(R.id.textView1);
        tvWeathere = findViewById(R.id.textView2);
        tvTemp = findViewById(R.id.textView3);
        tvHumidity = findViewById(R.id.textView4);
        tvWind = findViewById(R.id.textView5);
        imageView = findViewById(R.id.imageView5);
        tvTempMain = findViewById(R.id.textViewTemp);
        warm = findViewById(R.id.warm);
        warm2 = findViewById(R.id.warm2);
        imageView2 = findViewById(R.id.imageView90);

        Background background = new Background();
        background.execute();

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Background background = new Background();
                background.execute();
            }
        });


    }



    public String parsingCity(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        return (String) weatherJsonObject.get("name");
    }

    public String parsingPogoda(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONArray weatherArray = (JSONArray) weatherJsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        return (String) weatherData.get("main");
    }

    public Long parsingTemp(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONObject temp = (JSONObject) weatherJsonObject.get("main");
        JSONArray arrayTemp = new JSONArray();
        arrayTemp.add(temp);
        JSONObject tempData = (JSONObject) arrayTemp.get(0);
        return (Long) tempData.get("temp");
    }

    public Long parsingHumidity(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONObject humidity = (JSONObject) weatherJsonObject.get("main");
        JSONArray arrayHumidity = new JSONArray();
        arrayHumidity.add(humidity);
        JSONObject HumidityData = (JSONObject) arrayHumidity.get(0);
        return (Long) HumidityData.get("humidity");
    }

    public Long parsingWind(String parsingData) throws ParseException {
        JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(parsingData);
        JSONObject wind = (JSONObject) weatherJsonObject.get("wind");
        JSONArray arrayWind = new JSONArray();
        arrayWind.add(wind);
        JSONObject windData = (JSONObject) arrayWind.get(0);
        return (Long) windData.get("speed");
    }

    private class Background extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Main.this, "Іщу, іщу нахой", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String part1 = "http://api.openweathermap.org/data/2.5/weather?q=";
            city = findViewById(R.id.editText);
            String cityName =city.getText().toString().trim();
            String part2 = "&units=metric&APPID=55ac04bafc3dad1bf1baa40b0b9c3849";
            String query = part1+cityName+part2;
            HttpURLConnection connection = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                connection = (HttpURLConnection) new URL(query).openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.setConnectTimeout(25000);
                connection.setReadTimeout(25000);
                connection.connect();

                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    parsingData = stringBuilder.toString();
                }


            } catch (Throwable cause) {
                cause.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            Parsing parsing = new Parsing();

            String str1 = "" + parsing.parseLong("main", "temp", parsingData);
            String[] arr = str1.split("\\.");
            tvTempMain.setText(arr[0] + "°С");

            tvCity.setText(parsing.parseString("name", parsingData));
            tvWeathere.setText(parsing.parseString("weather", "main", parsingData));
            String str = parsing.parseString("weather", "main", parsingData);
            tvTemp.setText(arr[0] + "°С");
            tvHumidity.setText(parsing.parseLong("main", "humidity", parsingData) + "%");
            tvWind.setText(parsing.parseLong("wind", "speed", parsingData) + " km/h");

            if (parsing.parseLong("main", "temp", parsingData) <= 0){
                warm.setImageResource(R.drawable.warm_cold);
                warm2.setImageResource(R.drawable.warm_cold);
            }
            else if (parsing.parseLong("main", "temp", parsingData) <= 20){
                warm.setImageResource(R.drawable.warm);
                warm2.setImageResource(R.drawable.warm);
            }
            else if (parsing.parseLong("main", "temp", parsingData) > 20){
                warm.setImageResource(R.drawable.warm_hot);
                warm2.setImageResource(R.drawable.warm_hot);
            }



            switch (str){
                case("Clear"):
                    imageView.setImageResource(R.drawable.sun);
                    imageView2.setImageResource(R.drawable.sun);
                    break;
                case("Clouds"):
                    imageView.setImageResource(R.drawable.cloud);
                    imageView2.setImageResource(R.drawable.cloud);
                    break;
                case("Rain"):
                    imageView.setImageResource(R.drawable.raining);
                    imageView2.setImageResource(R.drawable.raining);
                    break;
                case("Thunderstorm"):
                    imageView.setImageResource(R.drawable.storm);
                    imageView2.setImageResource(R.drawable.storm);
                    break;
                case("Snow"):
                    imageView.setImageResource(R.drawable.snowflake);
                    imageView2.setImageResource(R.drawable.snowflake);
                    break;
                case("Mist"):
                    imageView.setImageResource(R.drawable.snowflake);
                    imageView2.setImageResource(R.drawable.snowflake);
                    break;
            }
        }
    }
}



