package com.example.backgroundrecorder;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

public class RecordingService extends Service {
    private MediaRecorder recorder;
    private String fileName;
    private int recordingDuration;

    @Override
    public void onCreate() {
        super.onCreate();

        recordingDuration = getResources().getInteger(R.integer.N);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();

        // If we get killed, after returning from here, don't restart
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startRecording() {
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/recording" + getNextRecordingNumber() + ".ogg";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.OGG);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.OPUS);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("RecordingService::startRecording()", "prepare() failed");
        }

        recorder.start();

        // Schedule the stopRecording() method to be called after the specified duration
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRecording();
                startRecording();
            }
        }, recordingDuration * 1000); // Convert duration to milliseconds
    }

    private String padNumberWithZeros(int number, int paddingLength) {
        String numberString = String.valueOf(number);

        if (numberString.length() >= paddingLength) {
            return numberString;
        }

        StringBuilder paddedNumber = new StringBuilder();
        int numZeros = paddingLength - numberString.length();

        for (int i = 0; i < numZeros; i++) {
            paddedNumber.append("0");
        }

        paddedNumber.append(numberString);
        return paddedNumber.toString();
    }

    private String getNextRecordingNumber() {
        SharedPreferences preferences = getSharedPreferences("RecordingPrefs", MODE_PRIVATE);
        int currentNumber = preferences.getInt("currentNumber", 0);
        int nextNumber = currentNumber + 1;

        String paddedNumber = padNumberWithZeros(nextNumber, 3);

        // Update the current number for the next recording
        preferences.edit().putInt("currentNumber", nextNumber).apply();

        return paddedNumber;
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
