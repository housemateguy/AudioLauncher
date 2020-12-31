package com.example.audiolauncher;


        import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


    public  class CalculatriceActivity extends AppCompatActivity implements RecognitionListener, TextToSpeech.OnInitListener {
      /*  private TextView returnedText;
        private ToggleButton toggleButton;
        private ProgressBar progressBar;*/
        private SpeechRecognizer speech = null;
        private Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        private String LOG_TAG = "VoiceRecognitionActivity";
     /*   private TextView TEXTEE, TEXTEE2;*/
        public  String hmmm;
        /*public TextView ar;
        private TextView fr;
        private TextView eng;*/
        private TextToSpeech tts;

   /* private TextView AR= (TextView) findViewById(R.id.textView3);
    private TextView FR=(TextView) findViewById(R.id.textView5);
    private TextView ENG=(TextView) findViewById(R.id.textView4);*/

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calculatrice);
         /*   TEXTEE = (TextView) findViewById(R.id.textView2);
            TEXTEE2 = (TextView) findViewById(R.id.textView);
            returnedText = (TextView) findViewById(R.id.textView);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            toggleButton = (ToggleButton) findViewById(R.id.toggleButton);**/
          /*  manager = new ScriptEngineManager();*/


            initializeTextToSpeech();




           /* progressBar.setVisibility(View.INVISIBLE);*/
            speech = SpeechRecognizer.createSpeechRecognizer(this);
            speech.setRecognitionListener(this);

         recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.FRENCH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"fr" );


          /*  recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-JO");
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar-JO");
*/
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            // j'ai changer listen time from 3sec lel 10sec
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);

 /*           toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setIndeterminate(true);
                        speech.startListening(recognizerIntent);

                    } else {
                        progressBar.setIndeterminate(false);
                        progressBar.setVisibility(View.INVISIBLE);
                        speech.stopListening();
                    }
                }
            });
*/
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

            onCalculate();  }
                }, 6000);


        }

     public void onCalculate() {


                 speech.startListening(recognizerIntent);



     }



        public void Onclick (View v) {
            onCalculate();




        }




        private void initializeTextToSpeech() {
            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (tts.getEngines().size() == 0 ){
                        Toast.makeText(CalculatriceActivity.this, "tts engines",Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        tts.setLanguage(Locale.FRANCE);
                        speak("activité calculatrice: ");
                    }
                }
            });
        }

        private void speak(String message) {
            if(Build.VERSION.SDK_INT >= 21){
                tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
            } else {
                tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            if (speech != null) {
                speech.destroy();
                Log.i(LOG_TAG, "destroy");
            }

        }

        @Override
        public void onBeginningOfSpeech() {
            Log.i(LOG_TAG, "onBeginningOfSpeech");
            /*progressBar.setIndeterminate(false);
            progressBar.setMax(10);*/
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.i(LOG_TAG, "onBufferReceived: " + buffer);
        }

        @Override
        public void onEndOfSpeech() {
            Log.i(LOG_TAG, "onEndOfSpeech");
          /*  progressBar.setIndeterminate(true);
            toggleButton.setChecked(false);*/
        }

        @Override
        public void onError(int errorCode) {
            String errorMessage = getErrorText(errorCode);
            Log.d(LOG_TAG, "FAILED " + errorMessage);
           /* returnedText.setText(errorMessage);
            toggleButton.setChecked(false);*/
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
            Log.i(LOG_TAG, "onEvent");
        }

        @Override
        public void onPartialResults(Bundle arg0) {
            Log.i(LOG_TAG, "onPartialResults");
            ArrayList<String> matches = arg0
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = "";
            for (String result : matches) {
                text += result + "\n";
            }
            hmmm= text;
           if  (hmmm.toLowerCase().indexOf("main") != -1) {
               Intent intent = new Intent(this, MainActivity.class);
               startActivity(intent);
               finish();
           } else {

           /* TEXTEE.setText(text);*/
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");



        /*    hmmm= hmmm.replaceAll("[O|o]pen [b|B]rackets*", "(");
            hmmm= hmmm.replaceAll("[c|C]losed* [b|B]rackets*", ")");
            hmmm= hmmm.replaceAll("[x|X]","*");
            hmmm= hmmm.replaceAll("[d|D]evide","/");
            hmmm= hmmm.replaceAll("زائد","+");
            hmmm= hmmm.replaceAll("ناقص","-");
            hmmm= hmmm.replaceAll("في","*");
            hmmm= hmmm.replaceAll("في","*");

            hmmm= hmmm.replaceAll("عل[ى|ي]","/");
            hmmm= hmmm.replaceAll("[ا|إ]غلق قوس",")");
            hmmm= hmmm.replaceAll("[ا|إ]فتح قوس","(");
            hmmm= hmmm.replaceAll("واحد","1");
            hmmm= hmmm.replaceAll("[ا|إ|أ][ث|ت]نان","2");
            hmmm= hmmm.replaceAll("ثلاث[|ه|ة]","3");
            hmmm= hmmm.replaceAll("[ا|إ]ربع[|ة|ه]","4");
            hmmm= hmmm.replaceAll("خمس[|ة|ه]","5");
            hmmm= hmmm.replaceAll("ست[|ة|ه]","6");

            hmmm= hmmm.replaceAll("سبع[|ة|ه]","7");
            hmmm= hmmm.replaceAll("[ث|ت]ماني[|ة|ه]","8");
            hmmm= hmmm.replaceAll("تسع[|ة|ه]","9");
            hmmm= hmmm.replaceAll("عشر[|ة|ه]","10");*/


            /*TEXTEE.setText(hmmm+" =");*/
            try {
                Object resultobj = (double)engine.eval(hmmm);
                String fff = resultobj.toString();
              /*  TEXTEE2.setText(fff);*/
                speak("resultat egal "+fff);
            }
            catch(Exception e){
                Toast.makeText(this, "matemchiche lol",Toast.LENGTH_SHORT).show();
            }

        } }



        @Override
        public void onReadyForSpeech(Bundle arg0) {
            Log.i(LOG_TAG, "onReadyForSpeech");
        }

        @Override
        public void onResults(Bundle results) {
            Log.i(LOG_TAG, "onResults");

        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
            /*progressBar.setProgress((int) rmsdB);*/

        }

        public static String getErrorText(int errorCode) {
            String message;
            switch (errorCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "Audio recording error";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "Client side error";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "Insufficient permissions";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "Network error";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "Network timeout";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "No match";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RecognitionService busy";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "error from server";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "No speech input";
                    break;
                default:
                    message = "Didn't understand, please try again.";
                    break;
            }
            return message;
        }























        public void tofr(View v) {
  /*      recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.FRENCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"fr" );
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000);
*/
        }
        public void toeng(View v) {
   /*     recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.ENGLISH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en" );
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000);
*/
        }

        public void toar(View v) {
  /*      recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-JO");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar-JO");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000);
*/
        }

        @Override
        public void onInit(int status) {

        }
    }




