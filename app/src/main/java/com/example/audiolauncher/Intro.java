package com.example.audiolauncher;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

    public class Intro extends AppCompatActivity {

        private static int SPLASH_TIME = 7000; //en seconde

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_intro);

            //ne touch pas tout ca vvvvvv
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent mySuperIntent = new Intent(Intro.this, MainActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }
            }, SPLASH_TIME);

        }

    }
