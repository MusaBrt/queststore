package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.objects.exams.ClassicExam;
import me.koply.sorustore.objects.exams.Exam;
import me.koply.sorustore.objects.exams.MultipleChoiceExam;
import me.koply.sorustore.objects.exams.RandomExam;
import me.koply.sorustore.utilities.Util;
import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;
import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.questions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.koply.sorustore.objects.enums.ExamType.*;

@CommandInfo("sinavbaslat")
public class SinavBaslat extends ACommand {

    @Override
    protected void onCommand(CommandParameters cmd) {
        cmd.getScanner().nextLine(); // girdi bugu fix
        // Oluşturulan sınav soruları veri sakladığımız sınıfta bulunuyor. Onları çektik.
        Map<Class<? extends Exam>, List<Exam>> exams = Data.getInstance().getExams();
        if (exams.isEmpty()) {
            out("Hazırlanmış sınav bulunamadı.");
            return;
        }

        List<Exam> sinavlar = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Class<? extends Exam>, List<Exam>> entry : exams.entrySet()) {
            try {
                String examtype = "";
                if (entry.getKey() == ClassicExam.class) {
                    examtype = CLASSIC.getValue();
                } else if (entry.getKey() == MultipleChoiceExam.class) {
                    examtype = TEST.getValue();
                } else if (entry.getKey() == RandomExam.class) {
                    examtype = RANDOM.getValue();
                } else {
                    out("Ciddi bir hata bulundu. İşlem yapılamadı.");
                    return;
                }

                for (Exam ex : entry.getValue()) {
                    out(i++ + " -> " + examtype + " tipinde " + ex.getQuestionCount() + " adet soruya sahip bir sınav bulunudu.");
                    sinavlar.add(ex);
                }
            } catch (Throwable t) {
                t.printStackTrace();
                // Geliştirme aşamasında hataları görmek için bu şekilde, normalde hata vermeyecek bir sistem.
                // Daha sonra ignored olması normaldir.
            }
        }
        Exam currentExam;
        while (true) {
            out("Olmak istediğiniz sınavın başında yer alan numarayı girin.");
            int examNumber = -1;
            try {
                examNumber = Integer.parseInt(cmd.getScanner().nextLine());
                currentExam = sinavlar.get(examNumber);
                if (currentExam != null) {
                    break;
                } else out("Geçersiz girdi.");

            } catch (Throwable ignored) {}
        }



        out("---------------");
        out("Sınav Başladı.");
        out("Sınavda toplam " + currentExam.getQuestionCount() + " kadar soru var.");

        out("Lütfen adınızı giriniz.");
        String name = cmd.getScanner().nextLine();
        currentExam.setUsername(name);

        int puan=0; // Toplam kazanılan puan

        switch (currentExam.getExamType()) {
            case TEST:
                multipleChoiceExam((MultipleChoiceExam) currentExam, cmd, puan);
                break;
            case CLASSIC:
                classicExam((ClassicExam) currentExam, cmd);
                break;
            case RANDOM:
                randomExam((RandomExam) currentExam, cmd, puan);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentExam.getExamType());
        }

        // Sınav sonuçlarının ekrana bastırılması.
        out("Tebrikler sınavınız bitti. Aldığınız puan " + puan + ".");
    }

    public void multipleChoiceExam(MultipleChoiceExam exam, CommandParameters cmd, int puan) {
        int soru = 1;
        List<MultipleChoiceQuestion> answeredQuestions = new ArrayList<>();
        for (MultipleChoiceQuestion q : exam.getQuestions()) {
            out("---- " + soru + " numaralı soru ----");
            executeMultipleChoiceQuestion(q, cmd, puan);
            answeredQuestions.add(q);
            soru++;
        }
        // TODO, Add to answered questions json to sinavlar.dat içindeki çözülenler kısmı
    }

    public void classicExam(ClassicExam exam, CommandParameters cmd) {
        int soru = 1;
        List<ClassicQuestion> answeredQuestions = new ArrayList<>();
        for (ClassicQuestion q : exam.getQuestions()) {
            out("---- " + soru + " numaralı soru ----");
            executeClassicQuestion(q, cmd);
            answeredQuestions.add(q);
            soru++;
        }
        // TODO, Add to answered questions json to sinavlar.dat içindeki çözülenler kısmı
    }

    public void randomExam(RandomExam exam, CommandParameters cmd, int puan) {
        int soru = 0;
        List<Question> answeredQuestion = new ArrayList<>();
        for (Question q : exam.getQuestions()) {
            out("---- " + soru + " numaralı soru ----");
            switch (q.getQuestionEnumType()) {
                case CLASSIC:
                    executeClassicQuestion((ClassicQuestion) q, cmd);
                    break;
                case MULTIPLECHOICEQUESTION:
                    executeMultipleChoiceQuestion((MultipleChoiceQuestion) q, cmd, puan);
                    break;
                case GAPFILLING:
                    executeGapFillingQuestion((GapFillingQuestion)q, cmd);
                    break;
                case TRUEFALSE:
                    executeTrueFalseQuestion((TrueFalseQuestion)q, cmd);
                    break;
            }
            soru++;
        }
    }



    public void executeMultipleChoiceQuestion(MultipleChoiceQuestion q, CommandParameters cmd, int puan) {
        // Sorunun ekrana basılması.
        q.printExam();
        // Geçerli cevap vermeden ilerlemeyi engellemek amacı ile döngü.
        boolean running = true;
        while (running) {
            out("Lütfen cevabınızı giriniz. (Boş bırakmak istiyorsanız ::bos yazabilirsiniz)");
            String cevap = cmd.getScanner().nextLine();

            // Soruyu boş bırakmak isteyenler için devam ettirici blok.
            if (cevap.equalsIgnoreCase("::bos")) {
                q.setUserAnswer(cevap);
                return;
            }

            // Girilen şıkkın geçerli olup olmadığına bakılması.
            if (Util.selectionsCheck(cevap)) {
                // Cevabın doğru olması durumunda puanın eklenmesi.
                if (cevap.equalsIgnoreCase(q.getDogruCevap() + "")) {
                    puan+=q.getPuan();
                }
                q.setUserAnswer(cevap);
                // Soru için doğru cevap döngüsünün kapatılması.
                running = false;
            }
        }
    }

    public void executeClassicQuestion(ClassicQuestion q, CommandParameters cmd) {
        q.printExam();

        out("Lütfen cevabınızı giriniz. (Boş bırakmak istiyorsanız ::bos yazabilirsiniz.)");
        String cevap = cmd.getScanner().nextLine();

        q.setUserAnswer(cevap);
    }

    public void executeGapFillingQuestion(GapFillingQuestion q, CommandParameters cmd) {
        q.printExam();

        out("Lütfen cevabınızı giriniz. (Boşluğa gelecek kelimeyi girin. Boş bırakmak için ::bos)");
        String cevap = cmd.getScanner().nextLine();

        q.setUserAnswer(cevap);
    }

    public void executeTrueFalseQuestion(TrueFalseQuestion q, CommandParameters cmd) {
        q.printExam();
        out("Lütfen cevabınızı giriniz. (True/False şeklinde. Boş bırakmak için ::bos)");
        String cevap = cmd.getScanner().nextLine();
        q.setUserAnswer(cevap);
    }
}