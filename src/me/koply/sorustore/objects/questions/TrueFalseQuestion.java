package me.koply.sorustore.objects.questions;


import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.enums.QuestionType;
import me.koply.sorustore.utilities.Util;
import org.json.JSONObject;

public class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(boolean save) {
        this.type = QuestionType.TRUEFALSE;
        if (save) Data.getInstance().getTypedQuestions(this.getClass()).add(this);
    }

    boolean cevap;

    public void setCevap(boolean cevap) {
        this.cevap = cevap;
    }

    public boolean getCevap() {
        return cevap;
    }


    @Override
    public void lightPrintInformation() {
        out("Soru: " + soru);
        out("Cevap: " + Util.getStrBoolean(cevap));
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
    }

    @Override
    public void printExam() {
        out(soru);
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
    }

    @Override
    public Question setSelf(JSONObject jsonObject) {
        this.soru = jsonObject.get("soru").toString();
        this.puan = Integer.parseInt(jsonObject.get("puan").toString());
        String jsCevap = jsonObject.get("cevap").toString();
        switch (jsCevap) {
            case "true":
                cevap = true;
                break;
            case "false":
                cevap = false;
                break;
            default:
                System.out.println("Data dosyasında hata tespit edildi.");
        }
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
        json.put("cevap", (cevap + "").toLowerCase());
        json.put("useranswer", userAnswer);
        return json;
    }
}