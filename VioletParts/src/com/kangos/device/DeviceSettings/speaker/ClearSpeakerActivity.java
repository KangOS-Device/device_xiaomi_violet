/*
 * Copyright (C) 2020 Paranoid Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kangos.device.DeviceSettings.speaker;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Button;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;

import com.kangos.device.DeviceSettings.R;

public class ClearSpeakerActivity extends Activity {

private static final String TAG = ClearSpeakerFragment.class.getSimpleName();

    private AudioManager mAudioManager;
    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private ProgressBar mProgressBar;
    private Button mProgressButton;
    private boolean playingStopped;
    private int progressLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_speaker);

        mProgressBar = findViewById(R.id.speaker_progress_bar);
        mProgressButton = findViewById(R.id.speaker_progress_button);  
        mHandler = new Handler();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        
        progressLevel = mProgressBar.getMax();
        mProgressBar.setProgress(progressLevel);
        
        mProgressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonClicked();
            }
        });
        
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_clear_speaker);
        ClearSpeakerFragment clearSpeakerFragment;
        if (fragment == null) {
            clearSpeakerFragment = new ClearSpeakerFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_clear_speaker, clearSpeakerFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onStop() {
        stopPlaying();
        super.onStop();
    }

    public boolean startPlaying() {
        playingStopped=false;
        mAudioManager.setParameters("status_earpiece_clean=on");
        mMediaPlayer = new MediaPlayer();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.clear_speaker_sound);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            } finally {
                file.close();
            }
            mMediaPlayer.setVolume(1.0f, 1.0f);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to play speaker clean sound!", ioe);
            return false;
        }
        return true;
    }

    public void stopPlaying() {
        if ((mMediaPlayer != null) && (playingStopped==false)){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        mAudioManager.setParameters("status_earpiece_clean=off");
        playingStopped=true;
    }
    
    public void showProgress() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressLevel >= 0) {
                    mProgressBar.setProgress(progressLevel);
                    progressLevel--;
                    mHandler.postDelayed(this, 100);
                } else {
                    mHandler.removeCallbacks(this);
                }
            }
        }, 1);
    }
    
    public void resetProgressBar() {
            mHandler.removeCallbacksAndMessages(null);
            progressLevel=mProgressBar.getMax();
            mProgressButton.setText("Start");
            mProgressBar.setProgress(progressLevel);
    }
    
    public void onButtonClicked() {
        if ( mProgressButton.getText().equals("Start")){
        mProgressButton.setText("Stop");
        if (startPlaying()) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(() -> {
            stopPlaying();
            resetProgressBar();
            }, 30000);
        }
        showProgress();
        } else {
            resetProgressBar();
            stopPlaying();
        }
    }
}
