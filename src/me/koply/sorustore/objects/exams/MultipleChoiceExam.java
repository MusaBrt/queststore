package me.koply.sorustore.objects.exams;

import me.koply.sorustore.objects.enums.ExamType;
import me.koply.sorustore.objects.questions.MultipleChoiceQuestion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceExam extends Exam {
    public MultipleChoiceExam(boolean save) {
        super(save);
        this.examType = ExamType.TEST;
    }

    private List<MultipleChoiceQuestion> questions = new ArrayList<>();

    public List<MultipleChoiceQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MultipleChoiceQuestion> questions1) {
        questions = questions1;
    }

    @Override
    public Exam setSelf(JSONObject json) {
        JSONArray jsonArray = json.getJSONArray("questions");
        for (Object o : jsonArray) {
            MultipleChoiceQuestion q = (MultipleChoiceQuestion) new MultipleChoiceQuestion(false).setSelf((JSONObject) o);
            this.fullPoint+=q.getPuan();
            this.questionCount++;
            questions.add(q);
        }
        return this;
    }

    @Override
    public JSONObject getSelf() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (MultipleChoiceQuestion q : questions) {
            jsonArray.put(q.getSelf());
        }
        json.put("examtype", examType.getValue());
        json.put("questions", jsonArray);
        json.put("username", username);
        return json;
    }
}