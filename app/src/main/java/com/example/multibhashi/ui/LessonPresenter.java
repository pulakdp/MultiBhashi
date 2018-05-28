package com.example.multibhashi.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.multibhashi.data.DataRepository;
import com.example.multibhashi.data.LessonEntry;
import com.example.multibhashi.data.LoadLessonsCallback;
import com.example.multibhashi.util.AudioPlayback;
import com.example.multibhashi.util.AudioUtil;
import com.example.multibhashi.util.StringUtil;

import java.util.List;

/**
 * Author: PulakDebasish
 */
public class LessonPresenter implements LessonContract.Presenter {

    private static final String LOG_TAG = LessonPresenter.class.getSimpleName();

    public static final int SPEECH_INPUT_REQUEST_CODE = 0;

    private static final String LOCALE = "kn-IN";

    private LessonContract.View lessonView;

    private DataRepository lessonRepo;

    private int lessonIndex = 0;
    private List<LessonEntry> lessonEntries;

    private AudioUtil audioUtil;

    public LessonPresenter(DataRepository lessonRepo, LessonContract.View lessonView) {
        this.lessonRepo = lessonRepo;
        this.lessonView = lessonView;

        this.lessonView.setPresenter(this);
        audioUtil = AudioUtil.getInstance();
    }

    @Override
    public void start() {
        loadLessons();
    }

    @Override
    public void loadLessons() {
        lessonRepo.getLessons(new LoadLessonsCallback() {
            @Override
            public void onLessonsLoaded(List<LessonEntry> lessons) {
                lessonEntries = lessons;
                lessonView.showLesson(lessons.get(lessonIndex));
            }

            @Override
            public void onLoadFailed() {
                lessonEntries = null;
                Log.e(LOG_TAG, "Failed to get lessons");
            }
        });
    }

    @Override
    public void goToNextLesson() {
        lessonIndex++;
        if (lessonIndex == lessonEntries.size()) {
            lessonView.showToastMessage("Reached the end. Showing first lesson again.");
            lessonIndex = 0;
        }
        lessonView.showLesson(lessonEntries.get(lessonIndex));
    }

    @Override
    public void prepareAudio() {
        audioUtil.prepareCurrentAndNext(
                lessonEntries.get(lessonIndex).getAudioUrl(),
                lessonEntries.get(lessonIndex == lessonEntries.size() - 1 ? 0 : lessonIndex + 1).getAudioUrl());
    }

    @Override
    public void startOrStopAudio(AudioPlayback playback) {
        audioUtil.playOrStop(playback);
    }

    @Override
    public void promptSpeechInput(Fragment fragment) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, LOCALE);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LOCALE);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        try {
            fragment.startActivityForResult(intent, SPEECH_INPUT_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(fragment.getActivity(), "Speech Not Supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void findSimilarity(String result) {
        String original = lessonEntries.get(lessonIndex).getTargetScript();
        double percentageDouble = StringUtil.similarity(original, result) * 100;
        lessonView.showSpeechResult((int) percentageDouble);
    }
}
