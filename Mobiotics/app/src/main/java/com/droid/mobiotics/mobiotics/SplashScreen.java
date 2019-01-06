package com.droid.mobiotics.mobiotics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = (ImageView) findViewById(R.id.imgView);

        Thread thread = new Thread(){
            public void run()
            {
                try
                {
                    sleep(5000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent=new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        finish();

    }
}
