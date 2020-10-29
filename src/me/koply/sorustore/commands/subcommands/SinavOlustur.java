package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.objects.enums.ExamType;
import me.koply.sorustore.objects.exams.ClassicExam;
import me.koply.sorustore.objects.exams.Exam;
import me.koply.sorustore.objects.exams.MultipleChoiceExam;
import me.koply.sorustore.objects.exams.RandomExam;
import me.koply.sorustore.objects.questions.ClassicQuestion;
import me.koply.sorustore.objects.questions.MultipleChoiceQuestion;
import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;
import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.questions.Question;

import java.util.*;

@CommandInfo("sinavolustur")
public class SinavOlustur extends ACommand {

    /*
    Sınav oluşturma: Puan toplamı 100-110 arasında olacak şekilde soru bankasından rasgele sorular
    seçilerek bir sınav oluşturulacaktır. Sınav soruları kullanıcıya sıra ile sorulmalı ve cevapları
    alınarak sınav sonunda sınavdan alınan not ekranda yazılmalıdır.

    Not: Sınavı başlatma olayını ayrı bir komuta koydum.
     */

    @Override
    protected void onCommand(CommandParameters cmd) {
        out("Oluşturulabilecek sınav çeşitleri şunlardır:");
        out("0 - Çoktan Seçmeli\n" +
                "1 - Klasik\n" +
                "2 - Karışık");
        boolean running = true;
        while (running) {
            out("Oluşturulmasını istediğiniz sınav türünün numarasını girin. (Çıkmak için exit)");
            try {
                int index = Integer.parseInt(cmd.getScanner().next());
                switch (index) {
                    case 0:
                        typedExam(ExamType.TEST);
                        running = false;
                        break;
                    case 1:
                        typedExam(ExamType.CLASSIC);
                        running = false;
                        break;
                    case 2:
                        typedExam(ExamType.RANDOM);
                        running = false;
                        break;
                    default:
                        out("Geçersiz girdi.");
                }
            } catch (Exception ignored) {}
        }
    }

    private void typedExam(ExamType type) {
        List<Question> soruBankasi = new ArrayList<>();
        Map<Class<? extends Question>, PriorityQueue<Question>> tumSorular = Data.getInstance().getTumSorular();
        if (tumSorular.isEmpty()) {
            out("Hiç soru bulunamadı :(");
            return;
        }
        switch (type) {
            case TEST:
                for (Question qa : tumSorular.get(MultipleChoiceQuestion.class)) {
                    MultipleChoiceQuestion q = (MultipleChoiceQuestion) qa;
                    soruBankasi.add(q);
                }
                break;
            case CLASSIC:
                for (Question qa : tumSorular.get(ClassicQuestion.class)) {
                    ClassicQuestion q = (ClassicQuestion) qa;
                    soruBankasi.add(q);
                }
                break;
            case RANDOM:
                for(Map.Entry<Class<? extends Question>, PriorityQueue<Question>> entry : tumSorular.entrySet()) {
                    soruBankasi.addAll(entry.getValue());
                }
                break;
            default:
                out("Geçersiz tip girdisi. (Internal error)");
                return;
        }

        out("Toplamda " + soruBankasi.size() + " kadar soru bulundu.");

        // Sınav için uygun soruların ekleneceği listenin oluşturulması.
        List<Question> selected = new ArrayList<>();

        // RECURSIVE şekilde çalışan soru oluşturma sisteminin başlaması.
        if (func(0, soruBankasi, selected)) {
            // Recursive çalışan fonksiyonumuz 100-110 puanları arasında olan bir sınav oluşturulması durumunda tryableQuestionsue
            // değer dönderecek ve işlemler continueExam methodundan devam edecek.
            continueExam(selected, type);
        } else {
            // 100-110 puanları arasında sınav oluşturulması mümkün olmadığı için kullanıcıya uyarı mesajı attık.
            out("Bu sorulardan 100-110 puan aralığında bir sınav oluşturulamıyor.");
        }
    }
    
    // Oluşturulan sınav soruları veri sınıfımız içine kaydedilmiştir.
    private void continueExam(List<Question> questions, ExamType type) {

        if (!Data.getInstance().getExams().containsKey(type.getTypeClass())) {
            Data.getInstance().getExams().put(type.getTypeClass(), new ArrayList<>());
        }
        List<Exam> exams = Data.getInstance().getExams().get(type.getTypeClass());

        switch (type) {
            case TEST:
                MultipleChoiceExam multipleChoiceExam = new MultipleChoiceExam(true);
                for (Question q : questions) {
                    multipleChoiceExam.getQuestions().add((MultipleChoiceQuestion) q);
                }
                break;
            case CLASSIC:
                ClassicExam classicExam = new ClassicExam(true);
                for (Question q : questions) {
                    classicExam.getQuestions().add((ClassicQuestion) q);
                }
                break;
            case RANDOM:
                RandomExam randomExam = new RandomExam(true);
                for (Question q : questions) {
                    randomExam.getQuestions().add(q);
                }
                break;
            default:
                out("Geçersiz tip girdisi. (Internal error)");
                return;
        }

        out("Sınav oluşturulmuştur. Sınav başlatmak için \"sinavbaslat\" komutunu girebilirsiniz");
    }

    private boolean func(int totalPoint, List<Question> allQuestions, List<Question> selected) {
        // Eğer puan durumu halihazırda 100-110 arasında ise tryableQuestionsue dönderdik.
        // Fakat puan durumu 110 üstüne çıkmış ise false dönderdik.
        if (totalPoint >= 100 && totalPoint <= 110) return true;
        else if (totalPoint >110) return false;

        // Denenebilecek olan sorular bu list içinde tutuluyor.
        List<Question> tryableQuestions = new ArrayList<>();
        tryableQuestions.addAll(allQuestions);
        // Rastgele bir soru seçmek için random sınıfı oluşturuldu.
        Random rnd = new Random();
        
        while (!tryableQuestions.isEmpty()) {
            
            // Rastgele bir soru seçildi.
            Question randomQuestion = tryableQuestions.get(rnd.nextInt(tryableQuestions.size()));
            
            // Rastgele seçilen soru seçilebilir sorular listesinden kaldırıldı.
            tryableQuestions.remove(randomQuestion);

            // Rastgele seçilen soru toplam puanın tutulduğu değişkene eklendi.
            totalPoint+= randomQuestion.getPuan();

            // Seçilmiş sorular listesine rastgele seçilmiş olan soru eklendi.
            selected.add(randomQuestion);

            if (func(totalPoint, tryableQuestions, selected)) { // RECURSIVE OLARAK KENDINI CAGIRDI METHOD
                return true;
            } else {
                // Recursive false verdiyse bu soru ile 100-110 aralığında soru oluşturamamışız demektir.
                // Seçilmiş olan soru silindi ve döngü kullanılabilir olan sorulardan devam etti.
                // Kullanılabilir sorulardan bu rastgele seçilen soru silindiği için döngü kullanılabilir soruların
                // boşalıp boşalmadığına bakarak devam etti.
                // Eğer kullanılabilir soru kalmasaydı döngü çalışamazdı.

                selected.remove(randomQuestion);
                totalPoint-= randomQuestion.getPuan();
            }
        }
        return false;
    }
}