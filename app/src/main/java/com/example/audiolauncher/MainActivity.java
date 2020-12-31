package com.example.audiolauncher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts;

    private SpeechRecognizer speechRecog= null;
    public int cc;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        Object fab = findViewById(R.id.fab);
        ((View) fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission
                    // don't touch adi
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {

                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                    speechRecog.startListening(intent);
                }
            }
        });


        Handler handler = new Handler();

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);


        handler.postDelayed(new Runnable() {
            public void run() {    speechRecog.startListening(intent);
            }
        }, 5000);
    }



    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result_arr.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();
        Toast.makeText(this, ""+result_message, Toast.LENGTH_SHORT).show();


        if(result_message.indexOf("calculatrice") != -1) {
                speak("activité calculatrice");
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy();
        Intent intent = new Intent(this, CalculatriceActivity.class);
        startActivity(intent);

        finish();
         }
          else  if (result_message.indexOf("temp") != -1) {
                String time_now = DateUtils.formatDateTime(this, new Date().getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("Le temp maintenant est: " + time_now);
            }
         else if (result_message.indexOf("mail") != -1 || result_message.indexOf("email") != -1 && result_message.indexOf("envoi") != -1) {
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy();   Intent intent = new Intent(this, SendMailActivity.class);
            startActivity(intent);

            finish();

        }
        else if (result_message.indexOf("mail") != -1 || result_message.indexOf("email") != -1 && result_message.indexOf("recevoir") != -1) {
            speak("activité de recevoi des emails");
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy(); Intent intent = new Intent(this, ReadMailActivity.class);
            startActivity(intent);

            finish();

        }
         else if (result_message.indexOf("sms") != -1 && result_message.indexOf("envoi") != -1) {
            speak("activité de recevoi Sms");
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy(); Intent intent = new Intent(this, SendSmsActivity.class);
            startActivity(intent);

            finish();

        }
         else if (result_message.indexOf("sms") != -1  && result_message.indexOf("voir") != -1) {
            speak("activité d'envoi Sms");
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy(); Intent intent = new Intent(this, SmsActivity.class);
            startActivity(intent);

            finish();

        }
        else if (result_message.indexOf("meteo") != -1  || result_message.indexOf("météo") != -1) {
            speak("activité de météo");

            speechRecog.destroy(); Intent intent = new Intent(this, MeteoActivity.class);
            startActivity(intent);

            finish();

        }
        else if (result_message.indexOf("appel") != -1) {
            speak("activité de appel");
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy(); Intent intent = new Intent(this, PhoneCallActivity.class);
            startActivity(intent);
            finish();
        }
        else if (result_message.indexOf("para") != -1) {
            speak("activité des Paramétres");
            if(tts != null) {

                tts.stop();
                tts.shutdown();
            }
            speechRecog.destroy(); Intent intent = new Intent(this, Parametre.class);
            startActivity(intent);
            finish();
        }

    }

    private void initializeTextToSpeech() {
     /*   tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(MainActivity.this, "tts engines",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.FRANCE);
                    speak("Bonjour utilisateur, dit moi quelque chose.");
                }
            }
        });
*/
     tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(MainActivity.this, "tts engines",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.FRANCE);
                    speak("Bonjour utilisateur, dit moi quelque chose.");
                }
            }
        });



    }

    public void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
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
        tts.stop();
        tts.shutdown();
        speechRecog.stopListening();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeSpeechRecognizer();
        initializeTextToSpeech();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {    speechRecog.startListening(intent);
            }
        }, 5000);
    }
    @Override
    protected void onDestroy() {


        //Close the Text to Speech Library
        if(tts != null) {

            tts.stop();
            tts.shutdown();
        }
        speechRecog.destroy();
        super.onDestroy();
    }
}

