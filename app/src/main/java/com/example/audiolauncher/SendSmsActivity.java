package com.example.audiolauncher;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class SendSmsActivity extends Activity {

    Button sendSMSBtn;
    EditText toPhoneNumberET;
    EditText smsMessageET;
    private SpeechRecognizer speechRecog;
    private String is_there_a_wrong_input;
    private TextToSpeech tts;
    private int count;
    private String toSMS;
    private String smscontenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);


        Handler wait = new Handler();
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        wait.postDelayed(new Runnable() {
                             public void run() {  speak("nom de Récepteur");
                             }
                         },
                5000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecog.startListening(intent);
            }
        }, 8000);

        if (is_there_a_wrong_input=="oui") {
            speak("il ya une erreur d'entré de donneés, s'il vous plé tapper sur l'ecran pour esseyer une nouvelle foit");
        }


        wait.postDelayed(new Runnable() {
            public void run() {  speak("contenu d'sms")
            ;
            }
        }, 13000);


        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

                speechRecog.startListening(intent);

            }
        }, 16000);

        sendSMS();


    }

    protected void sendSMS() {
       String toPhoneNumber = toSMS;
        String smsMessage = smscontenu;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Sending SMS failed.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


   public void onclicktomain(View v) {

       Intent intent = new Intent(this, MainActivity.class);
       startActivity(intent);        finish();


   }














    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(SendSmsActivity.this, "tts engines",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.FRANCE);
                    speak("Activité D'envoi du sms:");
                }
            }
        });
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
         if(result_message.indexOf("main") != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);        finish();
        } else
        switch(count) {
            case 0:

                toSMS = "0"+getNameNumDetails(result_message);
                count=1;
                Toast.makeText(this, "reciever de l'sms: "+result_message+" "+toSMS, Toast.LENGTH_SHORT).show();

                break;
            case 1:
                smscontenu=result_message+" - envoyé par AudioLauncher.";
                count=2;
                Toast.makeText(this, "Contenu de l'sms: "+result_message, Toast.LENGTH_SHORT).show();
                break;

        }
        Toast.makeText(this, ""+result_message, Toast.LENGTH_SHORT).show();

    }

    public int getNameNumDetails(String recepteurnom){     SQLiteDatabase.CursorFactory factory = null;
        MyDBhandler dbhandler= new  MyDBhandler(this,"random",factory,2);
        Login login=new Login(1,"f","e",0264225);
        login= dbhandler.findHandler(recepteurnom);
        int recepteurnum=login.getNumber();
        if(recepteurnum==0) { Toast.makeText(getApplicationContext(),"le contact n'exist pas",Toast.LENGTH_LONG); }
        return recepteurnum;
    }


    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

}
