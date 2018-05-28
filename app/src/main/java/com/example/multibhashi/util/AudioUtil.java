package com.example.multibhashi.util;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Author: PulakDebasish
 */
public class AudioUtil {

    private MediaPlayer currentMediaPlayer;
    private MediaPlayer nextMediaPlayer;

    private static AudioUtil instance;

    public static AudioUtil getInstance() {
        if (instance == null) {
            instance = new AudioUtil();
        }
        return instance;
    }

    public void prepareCurrentAndNext(String currentUrl, String nextUrl) {
        if (currentMediaPlayer != null) {
            shiftMediaPlayers();
            nextMediaPlayer = prepareMediaPlayer(nextUrl);
            return;
        }
        currentMediaPlayer = prepareMediaPlayer(currentUrl);
        nextMediaPlayer = prepareMediaPlayer(nextUrl);
    }

    private void shiftMediaPlayers() {
        currentMediaPlayer = nextMediaPlayer;
    }

    private MediaPlayer prepareMediaPlayer(String url) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    public void playOrStop(final AudioPlayback playback) {
        if (currentMediaPlayer.isPlaying()) {
            currentMediaPlayer.pause();
            currentMediaPlayer.seekTo(0);
            playback.audioStoppedOrCompleted();
        } else {
            currentMediaPlayer.start();
            currentMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playback.audioStoppedOrCompleted();
                }
            });
            playback.audioPlaybackStarted();
        }
    }
}
