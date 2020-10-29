package me.koply.sorustore.objects.enums;

import me.koply.sorustore.objects.exams.ClassicExam;
import me.koply.sorustore.objects.exams.Exam;
import me.koply.sorustore.objects.exams.MultipleChoiceExam;
import me.koply.sorustore.objects.exams.RandomExam;

public enum ExamType {
    TEST("Çoktan Seçmeli", MultipleChoiceExam.class),
    CLASSIC("Klasik", ClassicExam.class),
    RANDOM("Karışık", RandomExam.class);

    ExamType(String value, Class<? extends Exam> typeClass) {
        this.value = value;
        this.typeClass = typeClass;
    }

    private String value;
    private Class<? extends Exam> typeClass;

    public String getValue() {
        return value;
    }

    public Class<? extends Exam> getTypeClass() {
        return typeClass;
    }


}