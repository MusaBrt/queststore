package me.koply.sorustore.objects.enums;

public enum QuestionType {
    MULTIPLECHOICEQUESTION("Çoktan Seçmeli"),
    CLASSIC("Klasik"),
    TRUEFALSE("Doğru-Yanlış"),
    GAPFILLING("Boşluk Doldurma");

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private final String value;
}