package me.koply.sorustore.objects;

import me.koply.sorustore.objects.questions.Question;

import java.util.Comparator;

public class DataComparator implements Comparator<Question> {

    @Override
    public int compare(Question firstQuestion, Question secondQuestion) {
        return firstQuestion.compareTo(secondQuestion);
        //return secondQuestion.compareTo(firstQuestion);
    }
}