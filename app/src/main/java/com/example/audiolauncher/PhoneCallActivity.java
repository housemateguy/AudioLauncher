package com.example.audiolauncher;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class PhoneCallActivity extends MainActivity {
    Object fab;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;

    Intent intent;
    String url;
    String num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
        fab = findViewById(R.id.fab);
        url = "tel:3334444";

        initializeTextToSpeech();
        initializeSpeechRecognizer();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                             public void run() {  speak("nom de Récepteur d'appel");
                             }
                         },
                5000);
        handler.postDelayed(new Runnable() {
            public void run() {  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecog.startListening(intent);
                url="tel:"+num;
                Toast.makeText(getApplicationContext(),"tel:"+num ,Toast.LENGTH_LONG);
                Toast.makeText(getApplicationContext(),"tel:"+num ,Toast.LENGTH_LONG);
                Toast.makeText(getApplicationContext(),"tel:"+num ,Toast.LENGTH_LONG);

                 }
        }, 8000);
    }

  public void calling() {
      speak("donne moi un numero a appeler: ");
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
          public void run() {
              speechRecog.startListening(intent);

              url="tel:"+num;
              Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse(url));
              startActivity(intent2);  }
      }, 5000);


  }

   public void onClick (View v) {
    calling();
   }



    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
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
                    assert result_arr != null;
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
        num= "0"+getNameNumDetails(result_message);
        url="tel:"+num;
        Toast.makeText(getApplicationContext(),"tel:"+num ,Toast.LENGTH_LONG);
        Toast.makeText(getApplicationContext(),"tel:"+num ,Toast.LENGTH_LONG);
        Toast.makeText(getApplicationContext(),"tel:"+num ,Toast.LENGTH_LONG);
      /*  Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse(url));  */
        Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse(url));


        startActivity(intent2);

    }
    public int getNameNumDetails(String recepteurnom){     SQLiteDatabase.CursorFactory factory = null;
        MyDBhandler dbhandler= new  MyDBhandler(this,"random",factory,2);
        Login login=new Login(1,"f","e",0264225);
        login= dbhandler.findHandler(recepteurnom);
        int recepteurnum=login.getNumber();
        if(recepteurnum==0) { Toast.makeText(getApplicationContext(),"le contact n'exist pas",Toast.LENGTH_LONG); }
        return recepteurnum;
    }
    private void initializeTextToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(PhoneCallActivity.this, "",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.FRANCE);
                    speak("activité d'appelle télephonique");
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
    protected void onPause() {
        super.onPause();
        tts.shutdown();
        speechRecog.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeSpeechRecognizer();
        initializeTextToSpeech();

    }





}
