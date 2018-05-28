package com.example.multibhashi.data;

import java.util.List;

/**
 * Author: PulakDebasish
 */
public interface LoadLessonsCallback {
    void onLessonsLoaded(List<LessonEntry> lessons);

    void onLoadFailed();
}
