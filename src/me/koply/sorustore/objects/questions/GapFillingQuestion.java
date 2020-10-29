package me.koply.sorustore.objects.questions;

import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.enums.QuestionType;
import org.json.JSONObject;

public class GapFillingQuestion extends Question {

    public GapFillingQuestion(boolean save) {
        this.type = QuestionType.GAPFILLING;
        if (save) Data.getInstance().getTypedQuestions(this.getClass()).add(this);
    }

    private int gapIndex;

    public void setGapIndex(int index) {
        gapIndex = index;
    }

    public int getGapIndex() {
        return gapIndex;
    }

    @Override
    public void lightPrintInformation() {
        out("Soru: " + soru);
        out("Cevap olan kelime: " + soru.split(" ")[gapIndex]);
        out("Boşluk olan kelime sırası " + (gapIndex + 1));
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
    }

    @Override
    public void printExam() {
        String gapText = soru.split(" ")[gapIndex];

        String gappedText = soru.replaceAll(gapText, boslukGetir(gapText));
        out(gappedText);
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
    }

    @Override
    public Question setSelf(JSONObject jsonObject) {
        this.soru = jsonObject.get("soru").toString();
        this.puan = Integer.parseInt(jsonObject.get("puan").toString());
        this.gapIndex = Integer.parseInt(jsonObject.get("gapindex").toString());
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
        json.put("gapindex", gapIndex + "");
        json.put("useranswer", userAnswer);
        return json;
    }

    private String boslukGetir(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append("_");
        }
        return sb.toString();
    }
}