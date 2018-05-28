package com.example.multibhashi.data;

/**
 * Author: PulakDebasish
 */
public class LessonEntry {

    private LessonType lessonType;

    private String conceptName;

    private String pronunciation;

    private String targetScript;

    private String audioUrl;

    public LessonEntry(LessonType lessonType, String conceptName, String pronunciation, String targetScript, String audioUrl) {
        this.lessonType = lessonType;
        this.conceptName = conceptName;
        this.pronunciation = pronunciation;
        this.targetScript = targetScript;
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        return "LessonEntry{" +
                "lessonType=" + lessonType +
                ", conceptName='" + conceptName + '\'' +
                ", pronunciation='" + pronunciation + '\'' +
                ", targetScript='" + targetScript + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public String getConceptName() {
        return conceptName;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getTargetScript() {
        return targetScript;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public enum LessonType {

        LEARN, QUESTION

    }

}
