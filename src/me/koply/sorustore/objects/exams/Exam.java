package me.koply.sorustore.objects.exams;

import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.enums.ExamType;
import org.json.JSONObject;

public abstract class Exam {
    public Exam(boolean save) {
        if (save) Data.getInstance().getTypedExams(this.getClass()).add(this);
    }

    int fullPoint;
    int questionCount;
    String username;
    ExamType examType;

    public int getFullPoint() {
        return fullPoint;
    }

    public void setFullPoint(int fullPoint) {
        this.fullPoint = fullPoint;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Exam getInstance() {
        return this;
    }

    public abstract Exam setSelf(JSONObject json);
    public abstract JSONObject getSelf();
}