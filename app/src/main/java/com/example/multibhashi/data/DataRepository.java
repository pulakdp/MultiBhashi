package com.example.multibhashi.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: PulakDebasish
 */
public class DataRepository {

    public static final String LOG_TAG = DataRepository.class.getName();

    private static DataRepository instance;

    private List<LessonEntry> cachedLessons;

    private WeakReference<Context> appContext;

    public static DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(context);
        }
        return instance;
    }

    private DataRepository(Context context) {
        appContext = new WeakReference<>(context);
    }

    private String loadJsonFromAsset() {
        String json;
        try {
            if (appContext.get() == null)
                return null;

            InputStream inputStream = appContext.get().getAssets().open("lessonflow.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        }catch (IOException e) {
            Log.e(LOG_TAG, "Failed to load JSON");
            return null;
        }
        return json;
    }

    private List<LessonEntry> getLessonsFromJson() {
        String jsonString = loadJsonFromAsset();
        if (jsonString == null)
            return null;

        List<LessonEntry> entries = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONArray lessonData = obj.getJSONArray("lesson_data");

            for (int i = 0; i < lessonData.length(); i++) {
                JSONObject lesson = lessonData.getJSONObject(i);
                String type = lesson.getString("type");
                String conceptName = lesson.getString("conceptName");
                String pronunciation = lesson.getString("pronunciation");
                String targetScript = lesson.getString("targetScript");
                String audioUrl = lesson.getString("audio_url");
                LessonEntry entry = new LessonEntry(type.equals("learn") ?
                                LessonEntry.LessonType.LEARN : LessonEntry.LessonType.QUESTION
                        ,conceptName, pronunciation, targetScript, audioUrl);

                entries.add(entry);
                Log.d(LOG_TAG, entry.toString() + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return entries;
    }

    public void getLessons(LoadLessonsCallback callback) {
        List<LessonEntry> lessons = getLessonsFromJson();
        if (lessons != null)
            callback.onLessonsLoaded(lessons);
        else
            callback.onLoadFailed();
    }

}
