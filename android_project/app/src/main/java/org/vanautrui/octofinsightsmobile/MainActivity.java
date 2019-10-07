package org.vanautrui.octofinsightsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity mainActivity = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                final Intent intent = new Intent(mainActivity,LoginActivity.class);
                startActivity(intent);
            }
        },500);

    }
}
