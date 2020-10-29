package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;
import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.questions.Question;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@CommandInfo("sorusil")
public class SoruSil extends ACommand {
    /*
    Soru bankasından soru çıkarma: Soru bankasından soru çıkartılırken öncelikle silinecek sorunun
    soru bankasından bulunması gereklidir. Bunun için silinmek istenen sorunun bulunmasında “soru
    metni” üzerinden arama yapılacaktır. Arama yapılacak kelime soru metinleri içerisinden aranarak
    soru metni içerisinde aranan kelimenin geçtiği sorular listelenecektir. Daha sonra kullanıcı
    filtrelenmiş listeden istediği soruyu seçerek silebilecektir.
     */
    @Override
    protected void onCommand(CommandParameters cmd) {
        cmd.getScanner().nextLine();
        out("---- Soru Sil ----");
        out("Lütfen bir anahtar kelime girin.");

        String keyWord = cmd.getScanner().nextLine();
        Map<Integer, Question> foundQuestions = searchQuestions(keyWord);
        if (foundQuestions.isEmpty()) {
            out("Girilen anahtar kelimeyi içeren soru bulunamadı.");
            return;
        }

        int i = 0;
        for (Map.Entry<Integer, Question> entry : foundQuestions.entrySet()) {
            out("-- " + entry.getKey() + " numaralı bulunan soru detayları --");
            entry.getValue().lightPrintInformation();
        }

        out("Lütfen silinecek olan sorunun numarasını giriniz. (Vazgeçmek için -1)");
        int index = Integer.parseInt(cmd.getScanner().next());

        if (index == -1) {
            out("İşlemden vazgeçildi.");
            return;
        }

        if (!foundQuestions.containsKey(index)) {
            out("Bu numaraya sahip bulunmuş soru bulunamadı. İşlemden vazgeçiliyor.");
            return;
        }

        out("Soru başarıyla silindi.");
    }

    private Map<Integer, Question> searchQuestions(String keyword) {
        Map<Integer, Question> questionMap = new HashMap<>();
        int i = 0;
        for (Map.Entry<Class<? extends Question>, PriorityQueue<Question>> mapq : Data.getInstance().getTumSorular().entrySet()) {
            for (Question q : mapq.getValue()) {
                questionMap.put(i, q);
                i++;
            }
        }

        return questionMap;
    }
}