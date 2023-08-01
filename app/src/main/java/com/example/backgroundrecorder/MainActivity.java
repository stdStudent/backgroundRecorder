package com.example.backgroundrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, RecordingService.class);
        startService(intent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }


}