package com.example.audiolauncher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SendMailActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;
    public Button send;



    public String frommail/* ="@gmail.com"*/;
    public String frompassword /*=""*/;
    public String toEmail;
    public String mailsubject;
    public String mailbody;
    public int count;
    public String is_there_a_wrong_input;
    String fromEmail;
    String fromPassword;
    String toEmails;
    List toEmailList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
     /*   send = (Button) this.findViewById(R.id.button1);*/
        Handler wait = new Handler();
         initializeTextToSpeech();
         initializeSpeechRecognizer();

        /*getting les mot de pass w email from parametres*/
        SQLiteDatabase mydatabase2 = openOrCreateDatabase("passwordemail.db", MODE_PRIVATE, null);
        Cursor resultSet = mydatabase2.rawQuery("Select * from list",null);
        resultSet.moveToFirst();
        frommail = resultSet.getString(0);
        frompassword = resultSet.getString(1);
        Toast.makeText(getApplicationContext(),""+fromEmail,Toast.LENGTH_LONG);
        /*end*/



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
            public void run() {  speak("sujet d'email")
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

        if (is_there_a_wrong_input=="oui") {
            speak("il ya une erreur d'entré de donneés, s'il vous plé tapper sur l'ecran pour esseyer une nouvelle foit");
        }




        wait.postDelayed(new Runnable() {
            public void run() {  speak("contenu d'email")
            ;
            }
        }, 21000);

        Handler handler3 = new Handler();
        handler3.postDelayed(new Runnable() {
            public void run() {  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                speechRecog.startListening(intent);
            }
        }, 24000);

       /* send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i("SendMailActivity", "Send Button Clicked.");

                String fromEmail = frommail;
                String fromPassword = frompassword;
                String toEmails = toEmail;
                List toEmailList = Arrays.asList(toEmails
                        .split("\\s*,\\s*"));
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = mailsubject;
                String emailBody = mailbody;
                new SendMailTask(SendMailActivity.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
            }
        });   */

    }



    private void initializeTextToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(getApplicationContext(), "tts engines",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.FRANCE);
                    speak("Activité D'envoi du mail:");
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
        switch(count) {
            case 0:
                toEmail = ""+getNameEmailDetails(result_message);
                if (toEmail=="le nom n'exist pas dans la repertoire") { speak("le nom n'exist pas dans la repertoire"); is_there_a_wrong_input="oui";}
                count=1;
                Toast.makeText(this, "reciever de l'email: "+result_message+" "+toEmail, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "l'email: "+toEmail, Toast.LENGTH_SHORT).show();

                break;
            case 1:
                mailsubject=result_message;
                count=2;
                Toast.makeText(this, "Sujet de l'email: "+result_message, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                mailbody=result_message+" - envoyé par AudioLauncher.";
                count=0;
                Toast.makeText(this, "Body de l'email: "+result_message, Toast.LENGTH_SHORT).show();
                break;
        }
        Toast.makeText(this, ""+result_message, Toast.LENGTH_SHORT).show();

    }



    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }


    public String getNameEmailDetails(String recepteurnom){     SQLiteDatabase.CursorFactory factory = null;
        MyDBhandler dbhandler= new  MyDBhandler(this,"random",factory,2);
        Login login=new Login(1,"f","e",0264225);
        login= dbhandler.findHandler(recepteurnom);
        String recepteurmail=login.getMail();
        if(!recepteurmail.contains("@") || recepteurmail==null) { recepteurmail="le nom n'exist pas dans la repertoire"; }
      return recepteurmail;
    }




    public void startlisteningtosendmail (View v) {



        Log.i("SendMailActivity", "Send Button Clicked.");

        String fromEmail = frommail;
        String fromPassword = frompassword;
        String toEmails = toEmail;
        List toEmailList = Arrays.asList(toEmail
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);
        String emailSubject = mailsubject;
        String emailBody = mailbody;
        new SendMailTask(SendMailActivity.this).execute(fromEmail,
                fromPassword, toEmailList, emailSubject, emailBody);


    }





}
