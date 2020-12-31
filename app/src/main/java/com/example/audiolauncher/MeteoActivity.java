package com.example.audiolauncher;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class MeteoActivity extends AppCompatActivity {

   /* TextView selectCity, cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    ProgressBar loader;
    Typeface weatherFont;*/
    String city = "Dhaka, BD";
    Geocoder gcd;

    String OPEN_WEATHER_MAP_API = "b8e6f49a5f73ef471fbac23716f8acb4";
    LocationManager locationManager;
    String _Location;
    String description;
    String temp;
    String humidity;
    String  pressure;
    private TextToSpeech tts2;
    private SpeechRecognizer speechRecog2= null;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meteo);
        initializeTextToSpeech();


        gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Verification de permission
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);

        List<String>  providerList = locationManager.getAllProviders();



        if(null!=locations && null!=providerList && providerList.size()>0){
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    _Location = listAddresses.get(0).getAddressLine(0);
                    Toast.makeText(this, ""+_Location, Toast.LENGTH_LONG).show();
                    Toast.makeText(this, ""+_Location, Toast.LENGTH_LONG).show();
                    Toast.makeText(this, ""+_Location, Toast.LENGTH_LONG).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String[] arrOfStr = _Location.split(",", 5);

            city = arrOfStr[1] + ", " + "Algeria";
            Toast.makeText(this, "" + city, Toast.LENGTH_LONG).show();

            taskLoadUp(city);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
             /*   tts2.speak("vous étes dans "+city+", Température: "+temp+", "+humidity+", "+pressure+", "+description+".",TextToSpeech.QUEUE_FLUSH,null,null);
                speak("vous étes dans "+city+", Température: "+temp+", "+humidity+", "+pressure+", "+description+".");
    */  String f="vous étes dans "+city+", Température: "+temp+", "+humidity+", "+pressure+", "+description+".";
        Toast.makeText(getApplicationContext(), ""+f, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), ""+f, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), ""+f, Toast.LENGTH_LONG).show();

       /* speak("vous étes dans ");
        speak(city);
        speak(", Température: ");
      speak(temp); speak(", "); speak(humidity); speak(", ");  speak(pressure);  speak(", "); speak(description); speak(".");
     */ speak(""+f); /*speak("lol teste");
        speak("taper sur l'ecran pour returner a l'activité Main");*/
            }
        }, 20000);

    }


    public void taskLoadUp(String query) {
        if (Meteo_function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }




    public void onClickd(View V) {
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
        if(tts2 != null) {

            tts2.stop();
            tts2.shutdown();
        }

    }






    class DownloadWeather extends AsyncTask < String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*loader.setVisibility(View.VISIBLE);*/

        }
        protected String doInBackground(String...args) {
            String xml = Meteo_function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                  // cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    description=""+details.getString("description").toUpperCase(Locale.US);
                    //detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    temp=""+String.format("%.2f", main.getDouble("temp")) + "°";
                   // currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    humidity=""+"Humidity: " + main.getString("Humidité") + "%";
                  //  humidity_field.setText("Humidité: " + main.getString("humidity") + "%");
                    pressure=""+"pression: " + main.getString("pressure") + " hPa";
                 //   pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
               //     updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                 /*  weatherIcon.setText(Html.fromHtml(Meteo_function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));
                  //  weatherIcon.setText(HtmlCompat.fromHtml(Meteo_function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000), HtmlCompat.FROM_HTML_MODE_LEGACY)); */

                 //   loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }



    }










    private void initializeTextToSpeech() {
        tts2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts2.getEngines().size() == 0 ){
                    Toast.makeText(MeteoActivity.this, "tts engines",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts2.setLanguage(Locale.FRANCE);
                    speak("Activité de météo, veuillez patienter la fonction est en cours de chargement");
                }
            }
        });
    }




    public void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts2.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts2.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts2.stop();
        tts2.shutdown();
       /* speechRecog.stopListening();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeTextToSpeech();

    }
    @Override
    protected void onDestroy() {


        //Close the Text to Speech Library
        if(tts2 != null) {

            tts2.stop();
            tts2.shutdown();
        }
        speechRecog2.destroy();
        super.onDestroy();
    }
}




