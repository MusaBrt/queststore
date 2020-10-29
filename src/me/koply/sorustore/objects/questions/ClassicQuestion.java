package me.koply.sorustore.objects.questions;

import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.enums.QuestionType;
import org.json.JSONObject;

public class ClassicQuestion extends Question {

    public ClassicQuestion(boolean save) {
        this.type = QuestionType.CLASSIC;
        if (save) Data.getInstance().getTypedQuestions(this.getClass()).add(this);
    }

    @Override
    public void lightPrintInformation() {
        out("Soru: " + soru);
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
    }

    @Override
    public void printExam() {
        lightPrintInformation();
    }

    @Override
    public Question setSelf(JSONObject jsonObject) {
        this.soru = jsonObject.get("soru").toString();
        this.puan = Integer.parseInt(jsonObject.get("puan").toString());
        String diffi = jsonObject.get("zorluk").toString();
        for (Difficulty dif : Difficulty.values()) {
            if (diffi.equals(dif.getValue())) {
                this.zorluk = dif;
            }
        }
        this.userAnswer = jsonObject.get("useranswer").toString();

        return this;
    }

    @Override
    public JSONObject getSelf() {
        JSONObject json = new JSONObject();
        json.put("soru", soru);
        json.put("puan", puan + "");
        json.put("zorluk", zorluk.getValue());
        json.put("questiontype", type.getValue());
        json.put("useranswer", userAnswer);
        return json;
    }


}