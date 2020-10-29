package me.koply.sorustore.objects.enums;

public enum Difficulty {

    // Zorluk düzeyleri ve düzeylerin Türkçe anlamı.
    // Soruların bilgilerini bastırırken katlarca kolaylık oluyor.

    HARD("Zor"),
    NORMAL("Orta"),
    EASY("Kolay");

    Difficulty(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}