package com.example.multibhashi.ui;

import android.support.v4.app.Fragment;

import com.example.multibhashi.data.LessonEntry;
import com.example.multibhashi.util.AudioPlayback;

/**
 * Author: PulakDebasish
 */
public interface LessonContract {

    interface View {
        void setPresenter(Presenter presenter);

        void showLesson(LessonEntry lessonEntry);

        void toggleTargetScript();

        void loadNextLesson();

        void showSpeechResult(int percentMatch);

        void showToastMessage(String message);
    }

    interface Presenter {
        void start();

        void loadLessons();

        void goToNextLesson();

        void prepareAudio();

        void startOrStopAudio(AudioPlayback playback);

        void promptSpeechInput(Fragment fragment);

        void findSimilarity(String result);
    }

}
