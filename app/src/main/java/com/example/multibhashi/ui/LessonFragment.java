package com.example.multibhashi.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multibhashi.R;
import com.example.multibhashi.data.LessonEntry;
import com.example.multibhashi.util.AudioPlayback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: PulakDebasish
 */
public class LessonFragment extends Fragment implements LessonContract.View {

    @BindView(R.id.play_stop)
    FloatingActionButton playStopAudio;

    @BindView(R.id.concept_target_text)
    TextView targetText;

    @BindView(R.id.concept_source_text)
    TextView sourceText;

    @BindView(R.id.learn_layout)
    View learnLayout;

    @BindView(R.id.question_layout)
    View questionLayout;

    @BindView(R.id.hint_button)
    ImageButton hintButton;

    @BindView(R.id.hint_text)
    TextView hintText;

    @BindView(R.id.target_script)
    TextView targetScriptText;

    @BindView(R.id.record)
    FloatingActionButton recordAudio;

    @BindView(R.id.next)
    FloatingActionButton nextButton;

    @BindView(R.id.speech_result)
    TextView speechResult;

    private LessonContract.Presenter presenter;

    private LessonFragment fragment;

    public static LessonFragment newInstance() {
        return new LessonFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lesson, container,false);
        ButterKnife.bind(this, rootView);

        fragment = this;

        presenter.start();

        playStopAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startOrStopAudio(audioPlayback);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNextLesson();
            }
        });
        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.promptSpeechInput(fragment);
            }
        });
        hintButton.setOnClickListener(hintClickListener);
        hintText.setOnClickListener(hintClickListener);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LessonPresenter.SPEECH_INPUT_REQUEST_CODE: {
                if (data != null) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    presenter.findSimilarity(result.get(0));
                }
                break;
            }
        }
    }

    AudioPlayback audioPlayback = new AudioPlayback() {
        @Override
        public void audioPlaybackStarted() {
            playStopAudio.setImageResource(R.drawable.ic_stop_white_24dp);
        }

        @Override
        public void audioStoppedOrCompleted() {
            playStopAudio.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    };

    View.OnClickListener hintClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggleTargetScript();
        }
    };

    @Override
    public void setPresenter(LessonContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLesson(LessonEntry lessonEntry) {
        if (lessonEntry == null)
            return;

        if (lessonEntry.getLessonType().equals(LessonEntry.LessonType.LEARN)) {
            learnLayout.setVisibility(View.VISIBLE);
            questionLayout.setVisibility(View.GONE);
        }else {
            speechResult.setText("");
            learnLayout.setVisibility(View.GONE);
            questionLayout.setVisibility(View.VISIBLE);
        }

        targetText.setText(lessonEntry.getConceptName());
        sourceText.setText(lessonEntry.getPronunciation());
        targetScriptText.setText(lessonEntry.getTargetScript());

        presenter.prepareAudio();
    }

    @Override
    public void toggleTargetScript() {
        if (targetScriptText.getVisibility() == View.VISIBLE)
            targetScriptText.setVisibility(View.GONE);
        else
            targetScriptText.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadNextLesson() {
        presenter.goToNextLesson();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showSpeechResult(int percentMatch) {
        speechResult.setText(percentMatch + "% matched");
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}