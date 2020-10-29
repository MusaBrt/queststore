package me.koply.sorustore.objects;

import me.koply.sorustore.objects.exams.Exam;
import me.koply.sorustore.objects.questions.Question;

import java.util.*;

public class Data {

    // --------------------- START Tum Soru Things ---------------------
    private Map<Class<? extends Question>, PriorityQueue<Question>> tumSorular = new HashMap<>();

    public Map<Class<? extends Question>, PriorityQueue<Question>> getTumSorular() {
        return tumSorular;
    }

    public void setTumSorular(Map<Class<? extends Question>, PriorityQueue<Question>> sorular) {
        tumSorular = sorular;
    }

    public PriorityQueue<Question> getTypedQuestions(Class<? extends Question>  clazz) {
        tumSorular.computeIfAbsent(clazz, k -> new PriorityQueue<>(new DataComparator()));
        return tumSorular.get(clazz);
    }
    // --------------------- END Tum Soru Things ---------------------
    // -
    // --------------------- START Sınav Soru Things ---------------------
    private Map<Class<? extends Exam>, List<Exam>> exams = new HashMap<>();

    public Map<Class<? extends Exam>, List<Exam>> getExams() {
        return exams;
    }

    public void setExams(Map<Class<? extends Exam>, List<Exam>> exams1) {
        exams = exams1;
    }

    public List<Exam> getTypedExams(Class<? extends Exam> clazz) {
        exams.computeIfAbsent(clazz, k -> new ArrayList<>());
        return exams.get(clazz);
    }
    // --------------------- END Sınav Soru Things ---------------------


    // ------------------------------------------
    // Instance
    private static Data instance;

    // Obje instance'ı alma
    public static Data getInstance() {
        // Eğer obje oluşturulmamışsa oluşturup instance değişkenine setleme
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }
}