package me.koply.sorustore.utilities;

import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.exams.ClassicExam;
import me.koply.sorustore.objects.exams.Exam;
import me.koply.sorustore.objects.exams.MultipleChoiceExam;
import me.koply.sorustore.objects.exams.RandomExam;
import me.koply.sorustore.objects.questions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class DataManager {
    public DataManager() {
        try {
            for (File file : files) {
                if (!file.exists()) file.createNewFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }


    public final File[] files = {new File("sorubankasi.dat"), new File("sinavlar.dat")};

    public void loadAllDatas() {
        getSinavlarFromFile();
        getSorularFromFile();
    }

    public void saveAllDatas() {
        setSinavlarToFile();
        setSorularToFile();
    }

    // Sınavlar data dosyasından soruları çekip Data sınıfı içindeki ilgili mape ekler
    private void getSinavlarFromFile() {
        String str = readAll(files[1]);
        if (str.equals("nunull")) {
            System.out.print("\nSınavlar data dosyası boş.");
            return;
        }
        int i = 0;
        JSONArray jsonArray = new JSONArray(str);
        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            switch (json.get("examtype").toString()) {
                case "Klasik":
                    JSONArray examsArray = new JSONArray(json.get("exams").toString());
                    for (Object examObject : examsArray) {
                        JSONObject examJson = (JSONObject) examObject;
                        new ClassicExam(true).setSelf(examJson);
                    }
                    break;
                case "Çoktan Seçmeli":
                    JSONArray examsArray1 = new JSONArray(json.get("exams").toString());
                    for (Object examObject : examsArray1) {
                        JSONObject examJson = (JSONObject) examObject;
                        new MultipleChoiceExam(true).setSelf(examJson);
                    }
                    break;
                case "Karışık":
                    JSONArray examsArray2 = new JSONArray(json.get("exams").toString());
                    for (Object examObject : examsArray2) {
                        JSONObject examJson = (JSONObject) examObject;
                        new RandomExam(true).setSelf(examJson);
                    }
                    break;
                default:
                    System.out.println("Sınavlar data dosyasında hatalı veri bulundu.");
                    break;
            }
        }
    }

    // Sınavları kendi data dosyasına kaydeder
    private void setSinavlarToFile() {
        JSONArray jsonArray = new JSONArray();
        Map<Class<? extends Exam>, List<Exam>> sinavlar = Data.getInstance().getExams();
        for (Map.Entry<Class<? extends Exam>, List<Exam>> entry : sinavlar.entrySet()) {
            JSONObject json = new JSONObject();
            String examtype = "";
            JSONArray examsArray = new JSONArray();
            for (Exam exam : entry.getValue()) {
                examtype = exam.getExamType().getValue();
                examsArray.put(exam.getSelf());
            }
            json.put("examtype", examtype);
            json.put("exams", examsArray);
            jsonArray.put(json);
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(files[1]), "utf-8"))) {
                writer.write(jsonArray.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Sorular data dosyasından soruları çekip Data sınıfı içindeki ilgili mape ekler
    private void getSorularFromFile() {
        String str = readAll(files[0]);
        if (str.equals("nunull")) {
            System.out.print("\nSorular data dosyası boş.");
            return;
        }
        JSONArray jsonArray = new JSONArray(str);
        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            switch (json.get("questiontype").toString()) {
                case "Çoktan Seçmeli":
                    new MultipleChoiceQuestion(true).setSelf(new JSONObject(json.get("question").toString()));
                    break;
                case "Klasik":
                    new ClassicQuestion(true).setSelf(new JSONObject(json.get("question").toString()));
                    break;
                case "Doğru-Yanlış":
                    new TrueFalseQuestion(true).setSelf(new JSONObject(json.get("question").toString()));
                    break;
                case "Boşluk Doldurma":
                    new GapFillingQuestion(true).setSelf(new JSONObject(json.get("question").toString()));
                    break;
                default:
                    System.out.println("Sorular data dosyasında hatalı veri bulundu.");
                    break;
            }
        }
    }

    // Soruları kendi data dosyasına kaydeder
    private void setSorularToFile() {
        JSONArray jsonArray = new JSONArray();
        Map<Class<? extends Question>, PriorityQueue<Question>> sorular = Data.getInstance().getTumSorular();
        for (Map.Entry<Class<? extends Question>, PriorityQueue<Question>> entry : sorular.entrySet()) {
            for (Question q : entry.getValue()) {
                JSONObject json = new JSONObject();
                json.put("questiontype", q.getQuestionType());
                json.put("question", q.getSelf());
                jsonArray.put(json);
            }
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(files[0]), "utf-8"))) {
            writer.write(jsonArray.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Verilen dosyadaki tüm satırları okuyup string olarak dönderir
    // Eğer hata oluşursa dönen string "nunull" olur
    private String readAll(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                //process the line
                sb.append(line);
            }
            line = sb.toString();
            br.close();
            if (line.equals("")) {
                return "nunull";
            } else {
                return line;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "nunull";
        }
    }
}