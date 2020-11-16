package me.koply.sorustore.objects.questions;

import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.enums.QuestionType;
import org.json.JSONObject;

public abstract class Question implements Comparable<Question> {
    String soru;
    int puan;
    Difficulty zorluk;
    QuestionType type;
    String userAnswer = " ";

    public void setSoru(String soru) {
        this.soru = soru;
    }

    public String getSoru() {
        return soru;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    public int getPuan() {
        return puan;
    }

    public void setZorluk(Difficulty zorluk) {
        this.zorluk = zorluk;
    }

    public Difficulty getZorluk() {
        return zorluk;
    }

    public QuestionType getQuestionEnumType() { return type; }

    public String getQuestionType() {
        return type.getValue();
    }

    //
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
    public String getUserAnswer() {
        return userAnswer;
    }
    //

    public abstract void lightPrintInformation();
    public abstract void printExam();

    public void out(Object o) {
        System.out.println(o + "");
    }

    @Override
    public int compareTo(Question q) {
        return this.puan - q.puan;
    }

    public abstract Question setSelf(JSONObject jsonObject);
    public abstract JSONObject getSelf();
}