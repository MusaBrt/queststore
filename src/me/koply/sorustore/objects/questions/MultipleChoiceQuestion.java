package me.koply.sorustore.objects.questions;

import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.enums.QuestionType;
import org.json.JSONObject;

public class MultipleChoiceQuestion extends Question {

    public MultipleChoiceQuestion(boolean save) {
        this.type = QuestionType.MULTIPLECHOICEQUESTION;
        if (save) Data.getInstance().getTypedQuestions(this.getClass()).add(this);
    }

    // Çoktan seçmeli soru objesi
    private String[] cevaplar;
    private char dogruCevap;
    char[] selections = {'A', 'B', 'C', 'D', 'E'};

    public String[] cevaplar() {
        return cevaplar;
    }

    public MultipleChoiceQuestion setCevaplar(String[] cevaplar) {
        this.cevaplar = cevaplar;
        return this;
    }

    public char getDogruCevap() {
        return dogruCevap;
    }

    public MultipleChoiceQuestion setCevap(char cevap) {
        this.dogruCevap = cevap;
        return this;
    }

    // Header ile birlikte soru bastıran method
    public void printInformation() {
        out("------ Soru Bilgileri ------");
        lightPrintInformation();
    }

    // Header bulunmayan soru bilgileri bastıran method
    @Override
    public void lightPrintInformation() {
        out("Soru: " + soru);
        int i = 0;
        for (char c : selections) {
            out(Character.toUpperCase(c) + " - " + cevaplar[i]);
            i++;

        }
        out("Doğru olan cevap: " + Character.toUpperCase(dogruCevap));
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
    }

    // Sınav durumunda cevap bulunmadan soru bastıran method
    @Override
    public void printExam() {
        out(soru);
        int i = 0;
        for (char c : selections) {
            out(Character.toUpperCase(c) + " - " + cevaplar[i]);
            i++;

        }
        out("Sorunun puansal değeri: " + puan);
        out("Zorluk düzeyi: " + zorluk.getValue());
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
        this.dogruCevap = jsonObject.get("dogrucevap").toString().charAt(0);
        String[] innercevaplar = new String[5];
        for (int i = 0; i<5; i++) {
            if (jsonObject.get(i + "").toString().equals("$::null::$")) {
                continue;
            }
            innercevaplar[i] = jsonObject.get(i + "").toString();
        }
        this.cevaplar = innercevaplar;
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
        json.put("dogrucevap", dogruCevap + "");
        for (int i = 0; i<5; i++) {
            if (cevaplar[i] == null) {
                json.put(i + "", "$::null::$");
            } else {
                json.put(i + "", cevaplar[i]);
            }
        }
        return json;
    }

}