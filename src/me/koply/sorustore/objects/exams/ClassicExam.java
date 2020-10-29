package me.koply.sorustore.objects.exams;

import me.koply.sorustore.objects.enums.ExamType;
import me.koply.sorustore.objects.questions.ClassicQuestion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassicExam extends Exam {
    public ClassicExam(boolean save) {
        super(save);
        this.examType = ExamType.CLASSIC;
    }

    private List<ClassicQuestion> questions = new ArrayList<>();

    public List<ClassicQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ClassicQuestion> questions1) {
        questions = questions1;
    }

    @Override
    public Exam setSelf(JSONObject json) {
        JSONArray jsonArray = json.getJSONArray("questions");
        for (Object o : jsonArray) {
            ClassicQuestion cq = (ClassicQuestion) new ClassicQuestion(false).setSelf((JSONObject) o);
            this.fullPoint+=cq.getPuan();
            this.questionCount++;
            questions.add(cq);
        }

        return this;
    }

    @Override
    public JSONObject getSelf() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (ClassicQuestion q : questions) {
            jsonArray.put(q.getSelf());
        }
        json.put("examtype", examType.getValue());
        json.put("questions", jsonArray);
        json.put("username", username);
        return json;
    }
}