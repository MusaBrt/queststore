package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;
import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.questions.ClassicQuestion;
import me.koply.sorustore.objects.questions.GapFillingQuestion;
import me.koply.sorustore.objects.questions.MultipleChoiceQuestion;
import me.koply.sorustore.objects.questions.TrueFalseQuestion;
import me.koply.sorustore.utilities.Util;
import me.koply.sorustore.commands.ACommand;

@CommandInfo("soruekle")
public class SoruEkle extends ACommand {

    /*
    Soru bankasına soru ekleme: Soru bankasına yeni soru eklenebilmelidir. Her bir soru çoktan
    seçmeli soru türünde olup aşağıdaki özellikleri içerir:
    a) Soru metni
    b) a,b,c, d gibi cevap şıkları
    c) Cevabın hangi şık olduğu
    d) Puan
    e) Zorluk derecesi (kolay, normal ve zor gibi)
     */

    @Override
    protected void onCommand(CommandParameters cmd) {
        cmd.getScanner().nextLine();
        // Next ile alınan veride bulunan next-line karakteri ardından gelen nextLine methodunu
        // atlatıyor. Bunu engellemek için başa bir tane nextLine koyduk.

        boolean loop = true;
        while (loop) {
            out("Hangi tür soru eklemek istersiniz?");
            out("A - Klasik Soru");
            out("B - Çoktan Seçmeli Soru");
            out("C - Doğru/Yanlış Sorusu");
            out("D - Boşluk Doldurma");
            String entry = cmd.getScanner().nextLine();

            switch (entry.toLowerCase()) {
                case "a":
                    addClassicQuestion(cmd);
                    loop= false;
                    break;
                case "b":
                    addMultipleChoiceQuestion(cmd);
                    loop= false;
                    break;
                case "c":
                    addTrueFalseQuestion(cmd);
                    loop= false;
                    break;
                case "d":
                    addGapFillingQuestion(cmd);
                    loop= false;
                    break;
                default:
                    out("Geçersiz seçim yaptınız.");
                    break;
            }
        }

        out("Soru başarıyla eklendi!");
    }

    private void addClassicQuestion(CommandParameters cmd) {
        out("---- Klasik Soru Ekle ----");
        out("Lütfen soru içeriğini giriniz.");
        String soru = cmd.getScanner().nextLine();

        ClassicQuestion classicQuestion = new ClassicQuestion(true);

        classicQuestion.setSoru(soru);
        classicQuestion.setPuan(enterPoint(cmd));
        classicQuestion.setZorluk(enterDifficulty(cmd));
        classicQuestion.lightPrintInformation();
    }

    private void addMultipleChoiceQuestion(CommandParameters cmd) {
        out("---- Çoktan Seçmeli Soru Ekle ----");
        out("Lütfen soru içeriğini giriniz.");
        String soru = cmd.getScanner().nextLine();

        // Eklenecek olan sorunun objesi oluşturuldu.
        MultipleChoiceQuestion multipleChoiceQuestionObject = new MultipleChoiceQuestion(true);

        // Girilen soru içeriği soru objesinde ayarlandı.
        multipleChoiceQuestionObject.setSoru(soru);

        // Sorunun cevaplarını alan döngü.
        int i = 0;
        String[] cevaplar = new String[5];
        for (char c : Util.selections) {
            if (i <= 3) {
                out("Lütfen " + c + " şıkkını giriniz.");
            } else out("Lütfen " + c + " şıkkını giriniz. (E şıkkı eklemek istemiyorsanız \"::bitti\" yazabilirsiniz)");

            String entry = cmd.getScanner().nextLine();

            if (entry.equals("::bitti")) break;
            cevaplar[i] = entry;
            i++;
        }
        multipleChoiceQuestionObject.setCevaplar(cevaplar);

        // Geçerli bir girdi alana kadar döngü içinde kalacak.
        boolean answerLoop = true;
        while (answerLoop) {
            out("Lütfen doğru olan şıkkı giriniz");
            String entry = cmd.getScanner().nextLine();
            if (entry.length() == 1 && Util.selectionsCheck(entry.toUpperCase())) {
                // Şık alacağımız için girdinin uzunluğunu önemsiz olarak kabul ettim ve girilen metnin ilk karakterini cevap şıkkı olarak kabul ettim.
                multipleChoiceQuestionObject.setCevap(entry.toCharArray()[0]);
                answerLoop=false;
            }
        }

        // Geçerli bir puan girilene kadar döngü içinde kalacak.
        multipleChoiceQuestionObject.setPuan(enterPoint(cmd));

        // Geçerli bir zorluk girilene kadar döngü içinde kalacak.
        multipleChoiceQuestionObject.setZorluk(enterDifficulty(cmd));


        // Oluşturulan sorunun detayları ekrana bastırıldı.
        multipleChoiceQuestionObject.printInformation();
    }

    private void addTrueFalseQuestion(CommandParameters cmd) {
        out("---- Doğru-Yanlış Sorusu Ekle ----");
        out("Lütfen soru içeriğini giriniz.");
        String soru = cmd.getScanner().nextLine();

        TrueFalseQuestion trueFalseQuestion = new TrueFalseQuestion(true);

        trueFalseQuestion.setSoru(soru);
        trueFalseQuestion.setPuan(enterPoint(cmd));
        trueFalseQuestion.setZorluk(enterDifficulty(cmd));

        while(true) {
            out("Cevap doğru ise true, yanlış ise false giriniz.");
            String entry = cmd.getScanner().nextLine();
            if (entry.equalsIgnoreCase("true")) {
                trueFalseQuestion.setCevap(true);
                break;
            } else if (entry.equalsIgnoreCase("false")) {
                trueFalseQuestion.setCevap(false);
                break;
            }
        }

        trueFalseQuestion.lightPrintInformation();


    }

    private void addGapFillingQuestion(CommandParameters cmd) {
        out("---- Boşluk Doldurma Sorusu Ekle ----");
        out("Lütfen soru içeriğini giriniz. (Boşluk olacak yere boşluk bırakmayın!)");
        String soru = cmd.getScanner().nextLine();

        GapFillingQuestion gapFillingQuestion = new GapFillingQuestion(true);
        String[] soruArgs = soru.split(" ");
        if (soruArgs.length == 1) {
            out("Çok kısa bir soru metni girdiğiniz soru eklenemedi. İşlem iptal ediliyor.");
            return;
        }
        gapFillingQuestion.setSoru(soru);

        gapFillingQuestion.setPuan(enterPoint(cmd));
        gapFillingQuestion.setZorluk(enterDifficulty(cmd));

        out("Soru içinde boşluk olacak olan kelimeyi girin. (Büyük küçük harf duyarlıdır)");
        while (true) {
            try {
                String[] kelime = cmd.getScanner().nextLine().split(" ");
                boolean tru = false;
                int i = 0;
                for (String s : soruArgs) {
                    if (s.equals(kelime[0])) {
                        out("Boşluk olacak olan kelime '" + kelime[0] + "' olarak ayarlandı");
                        gapFillingQuestion.setGapIndex(i);
                        tru = true;
                    }
                    i++;
                }
                if (!tru) {
                    out("Geçersiz bir kelime girdiniz. Lütfen doğru kelimeyi girin.");
                } else break;

            } catch (Throwable ignored) {}
        }
        gapFillingQuestion.lightPrintInformation();

    }

    private Difficulty enterDifficulty(CommandParameters cmd) {
        while (true) {
            out("Lütfen sorunun zorluk derecesini girin.");
            out("kolay - 0");
            out("orta - normal - 1");
            out("zor - 2");
            String difficulty = cmd.getScanner().nextLine();
            switch (difficulty) {
                case "kolay":
                case "0":
                    // Sorunun zorluk derecesi kolaya ayarlandı.
                    return Difficulty.EASY;
                case "orta":
                case "normal":
                case "1":
                    // Sorunun zorluk derecesi normale ayarlandı.
                    return Difficulty.NORMAL;
                case "zor":
                case "2":
                    // Sorunun zorluk derecesi zora ayarlandı.
                    return Difficulty.HARD;
                default:
                    out("Geçersiz zorluk düzeyi girdiniz. Soru otomatikmen kolay olarak ayarlanıyor.");
                    return Difficulty.EASY;
            }
        }
    }

    private int enterPoint(CommandParameters cmd) {
        int point = 0;
        while (true) {
            out("Soru kaç puan olacak?");
            try {
                // Eğer girdi integer'a dönüşmeye uygun ise alt satırlara geçilecektir. Uygun değilse döngü baştan başlayacaktır.
                point = Integer.parseInt(cmd.getScanner().nextLine());
                break;
            } catch (Throwable ignored) {}
        }
        return point;
    }

}