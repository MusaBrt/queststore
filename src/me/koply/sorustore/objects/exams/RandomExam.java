package me.koply.sorustore.objects.exams;

import me.koply.sorustore.objects.enums.ExamType;
import me.koply.sorustore.objects.questions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RandomExam extends Exam {
    public RandomExam(boolean save) {
        super(save);
        this.examType = ExamType.RANDOM;
    }

    @Override
    public Exam setSelf(JSONObject json) {
        JSONArray jsonArray = new JSONArray(json.get("questions").toString());
        for (Object o : jsonArray) {
            JSONObject jo = (JSONObject) o;
            switch (jo.get("questiontype").toString()) {
                case "Çoktan Seçmeli":
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) new MultipleChoiceQuestion(false).setSelf(jo);
                    this.fullPoint+=mcq.getPuan();
                    this.questionCount++;
                    questions.add(mcq);
                    break;
                case "Klasik":
                    ClassicQuestion cq = (ClassicQuestion) new ClassicQuestion(false).setSelf(jo);
                    this.fullPoint+=cq.getPuan();
                    this.questionCount++;
                    questions.add(cq);
                    break;
                case "Doğru-Yanlış":
                    TrueFalseQuestion tfq = (TrueFalseQuestion) new TrueFalseQuestion(false).setSelf(jo);
                    this.fullPoint+=tfq.getPuan();
                    this.questionCount++;
                    questions.add(tfq);
                    break;
                case "Boşluk Doldurma":
                    GapFillingQuestion gq = (GapFillingQuestion) new GapFillingQuestion(false).setSelf(jo);
                    this.fullPoint+=gq.getPuan();
                    this.questionCount++;
                    questions.add(gq);
                    break;
                default:
                    System.out.println("Sınav data dosyasında hata tespit edildi.");
                    break;

            }
        }
        return this;
    }

    @Override
    public JSONObject getSelf() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Question q : questions) {
            jsonArray.put(q.getSelf());
        }
        json.put("examtype", examType.getValue());
        json.put("questions", jsonArray);
        json.put("username", username);
        return json;
    }


    private List<Question> questions = new ArrayList<>();

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions1) {
        questions = questions1;
    }

}