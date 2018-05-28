package com.example.multibhashi.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.multibhashi.R;
import com.example.multibhashi.data.DataRepository;

public class LessonActivity extends AppCompatActivity {

    private LessonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        LessonFragment lessonFragment =
                (LessonFragment) getSupportFragmentManager().findFragmentById(R.id.lesson_container);

        if (lessonFragment == null) {
            lessonFragment = LessonFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.lesson_container, lessonFragment)
                    .commit();
        }

        presenter = new LessonPresenter(DataRepository.getInstance(this), lessonFragment);
    }
}
