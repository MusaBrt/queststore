package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;
import me.koply.sorustore.objects.Data;
import me.koply.sorustore.objects.DataComparator;
import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.questions.ClassicQuestion;
import me.koply.sorustore.objects.questions.MultipleChoiceQuestion;
import me.koply.sorustore.objects.questions.Question;
import me.koply.sorustore.objects.questions.TrueFalseQuestion;
import me.koply.sorustore.utilities.Util;

import java.util.*;

@CommandInfo("listele")
public class Listele extends ACommand {

    /*
    Soru bankasındaki soruları listeleme: Soru bankasındaki soruların tümü listelenebileceği gibi belli
    bir kritere uyan sorular da listelenebilmelidir. Bu kriterler aşağıdaki gibidir:
    a. Soru metni içinde arama
    b. Soru şıklarının metinleri içinde
    c. Doğru şıkları üzerinden arama (örneğin cevabı A şıkkı olanları listele gibi)
    d. Puan üzerinden arama (örneğin puanı 10 olan soruları listele gibi)
    e. Zorluk derecesi üzerinden listeleme (örneğin zor soruları listele gibi)
     */

    // Şıklar
    char[] selections = {'A', 'B', 'C', 'D', 'E'};

    @Override
    protected void onCommand(CommandParameters cmd) {
        // Listeleme komutunda yanlış kullanım durumunda direkt çıkış olmaması için döngü ile kapattık.
        boolean isLoop = true;
        while (isLoop) {
            out("Filtreleme kullanmak istiyor musunuz? Kullanmak istemezseniz tüm sorular listelenecektir. (e/h)");
            String entry = cmd.getScanner().next();

            if (entry.equalsIgnoreCase("h")) {
                // Filtreleme kullanılmayacağı durumda tüm soruları ekrana bastırdık.
                printAllQuestions();
                isLoop = false;
            } else if (entry.equalsIgnoreCase("e")) {
                // Filtreleme kullanılması istendiği için devam methoduna geçiş yaptık.
                filterContinue(cmd);
                isLoop = false;
            }
        }


    }

    private void filterContinue(CommandParameters cmd) {
        out("Hangi soru tipindeki sorulardan arama yapmak istersiniz?");
        out("A - Çoktan Seçmeli Sorular");
        out("B - Klasik Sorular");
        out("C - Doğru-Yanlış Sorular");
        out("D - Boşluk Doldurmalı Sorular");
        out("E - Hepsi");

        boolean loop = true;
        while (loop) {
            String entry = cmd.getScanner().next();

            switch (entry.toLowerCase()) {
                case "a":
                    loop = !foundMultipleChoiceQuestions(cmd);
                    break;
                case "b":
                    loop = !foundClassicQuestions(cmd);
                    break;
                case "c":
                    loop = !foundTrueFalseQuestions(cmd);
                    break;
                case "d":
                    loop = !foundGapFillingQuestions(cmd);
                    break;
                default:
                    out("Lütfen geçerli bir seçenek seçin.");

            }
        }
    }

    private boolean foundMultipleChoiceQuestions(CommandParameters cmd) {
        // Bilgilendirme mesajları
        out("Lütfen filtreleme türünüzü seçiniz.");
        out("A - Soru metni içinde arama");
        out("B - Soru şıkları içinde arama");
        out("C - Doğru şıkkı şu olanları listele");
        out("D - Puanı şu olanları listele");
        out("E - Zorluğu şu olanları listele");

        // Harf girdisi alınması için Scanner#next()
        String entry = cmd.getScanner().next();

        // Boş girdide komutun iptali
        if (entry.equalsIgnoreCase("") || entry.equalsIgnoreCase(" ")) {
            out("Geçersiz girdi. İşlem iptal ediliyor.");
            return false;
        }

        // Filtrelere uygun olan soruların ekleneceği liste oluşturuluyor.
        PriorityQueue<Question> found = new PriorityQueue<>(new DataComparator());

        // Tüm sorular pointer içine alınıyor, böylece her soruları kullanmamız gerektiğinde Data'yı çağırmamış olacağız.
        PriorityQueue<Question> sorular = Data.getInstance().getTypedQuestions(MultipleChoiceQuestion.class);
        if (entry.equalsIgnoreCase("a")) {

            // Anahtar kelimeden soru arama
            searchByQuestionText(found, cmd, sorular);

        } else if (entry.equalsIgnoreCase("b")) {
            // Sadece çoktan seçmeli sorular için geçerli şıklarda arama yapma
            cmd.getScanner().nextLine();
            out("Lütfen şıklarda aranacak olan metni girin. (Büyük/küçük harf önemsizdir.)");
            String text = cmd.getScanner().nextLine();

            // Bu sefer metni şıklarda arıyoruz.
            for (Question qa : sorular) {
                MultipleChoiceQuestion q = (MultipleChoiceQuestion) qa;
                // Her soruda birden fazla şık olduğu için şıklar içinde döngü kullanıyoruz.
                for (String s : q.cevaplar()) {
                    // Eğer şık metni içerisinde anahtar kelime(ler) var ise blok içine giriyor.
                    if (s.toLowerCase().contains(text.toLowerCase())) {
                        // Eğer soru önceden eklenmişse döngü ilerletiliyor. Eklenmemiş ise ekleniyor.
                        // Böylece aynı sorunun birden fazla kez listelenmesi engelleniyor.
                        if (found.contains(q)) continue;
                        found.add(q);
                    }
                }
            }

        } else if (entry.equalsIgnoreCase("c")) {
            // Sadece çoktan seçmeli sorularda geçerli doğru şıktan arama
            out("Lütfen doğru şıkkı girin.");
            String letter = cmd.getScanner().next();

            // Util.selectionsCheck methodu ile girdinin geçerli bir şık olup olmadığı kontrol edildi.
            if (Util.selectionsCheck(letter))  {
                for (Question qa : sorular) {
                    MultipleChoiceQuestion q = (MultipleChoiceQuestion) qa;
                    if ((q.getDogruCevap() + "").equalsIgnoreCase(letter)) {
                        found.add(q);
                    }
                }
            } else {
                out("Geçersiz bir şık seçtiniz. İşlem iptal ediliyor.");
                return false;
            }
        } else if (entry.equalsIgnoreCase("d")) {
            searchByPoint(found, cmd, sorular);
        } else if (entry.equalsIgnoreCase("e")) {
            searchByDifficulty(found, cmd, sorular);
        } else {
            out("Geçersiz girdi. İşlem iptal ediliyor.");
            return false;
        }

        // Bulunanlar listesinin boş olup olmadığı durum kontrol ediliyor.
        if (found.isEmpty()) {
            out("Girilen filtreye göre soru bulunamadı.");
            return false;
        }

        printFoundList(found);
        return true;
    }

    private boolean foundClassicQuestions(CommandParameters cmd) {
        // Bilgilendirme mesajları
        out("Lütfen filtreleme türünüzü seçiniz.");
        out("A - Soru metni içinde arama");
        out("B - Puanı şu olanları listele");
        out("C - Zorluğu şu olanları listele");

        // Harf girdisi alınması için Scanner#next()
        String entry = cmd.getScanner().next();

        // Boş girdide komutun iptali
        if (entry.equalsIgnoreCase("") || entry.equalsIgnoreCase(" ")) {
            out("Geçersiz girdi. İşlem iptal ediliyor.");
            return false;
        }

        PriorityQueue<Question> sorular = Data.getInstance().getTypedQuestions(ClassicQuestion.class);
        PriorityQueue<Question> found = new PriorityQueue<>(new DataComparator());

        switch (entry.toLowerCase()) {
            case "a":
                searchByQuestionText(found, cmd, sorular);
                break;
            case "b":
                searchByPoint(found, cmd, sorular);
                break;
            case "c":
                searchByDifficulty(found, cmd, sorular);
                break;
            default:
                out("Geçersiz girdi. İşlem iptal ediliyor.");
                return false;
        }

        // Bulunanlar listesinin boş olup olmadığı durum kontrol ediliyor.
        if (found.isEmpty()) {
            out("Girilen filtreye göre soru bulunamadı.");
            return false;
        }

        printFoundList(found);
        return true;
    }

    private boolean foundTrueFalseQuestions(CommandParameters cmd) {
        // Bilgilendirme mesajları
        out("Lütfen filtreleme türünüzü seçiniz.");
        out("A - Soru metni içinde arama");
        out("B - Puanı şu olanları listele");
        out("C - Zorluğu şu olanları listele");
        out("D - Cevabı şu olanları listele (true/false)");

        // Harf girdisi alınması için Scanner#next()
        String entry = cmd.getScanner().next();

        // Boş girdide komutun iptali
        if (entry.equalsIgnoreCase("") || entry.equalsIgnoreCase(" ")) {
            out("Geçersiz girdi. İşlem iptal ediliyor.");
            return false;
        }

        PriorityQueue<Question> sorular = Data.getInstance().getTypedQuestions(ClassicQuestion.class);
        PriorityQueue<Question> found = new PriorityQueue<>(new DataComparator());

        switch (entry.toLowerCase()) {
            case "a":
                searchByQuestionText(found, cmd, sorular);
                break;
            case "b":
                searchByPoint(found, cmd, sorular);
                break;
            case "c":
                searchByDifficulty(found, cmd, sorular);
                break;
            case "d":
                out("Cevabı doğru olan soruları bulmak için 'true', yanlış olan soruları bulmak için 'false'");
                String innerEntry = cmd.getScanner().next();
                boolean searchQuery;
                if (innerEntry.equalsIgnoreCase("true")) {
                    searchQuery = true;
                } else if (innerEntry.equalsIgnoreCase("false")) {
                    searchQuery = false;
                } else {
                    out("Geçersiz girdi. İşlem iptal ediliyor.");
                    return false;
                }

                for (Question qa : sorular) {
                    TrueFalseQuestion q = (TrueFalseQuestion) qa;
                    if (q.getCevap() == searchQuery) {
                        found.add(qa);
                    }
                }
                break;
            default:
                out("Geçersiz girdi. İşlem iptal ediliyor.");
                return false;
        }

        // Bulunanlar listesinin boş olup olmadığı durum kontrol ediliyor.
        if (found.isEmpty()) {
            out("Girilen filtreye göre soru bulunamadı.");
            return false;
        }

        printFoundList(found);
        return true;
    }

    private boolean foundGapFillingQuestions(CommandParameters cmd) {
        // Bilgilendirme mesajları
        out("Lütfen filtreleme türünüzü seçiniz.");
        out("A - Soru metni içinde arama");
        out("B - Puanı şu olanları listele");
        out("C - Zorluğu şu olanları listele");

        // Harf girdisi alınması için Scanner#next()
        String entry = cmd.getScanner().next();

        // Boş girdide komutun iptali
        if (entry.equalsIgnoreCase("") || entry.equalsIgnoreCase(" ")) {
            out("Geçersiz girdi. İşlem iptal ediliyor.");
            return false;
        }

        PriorityQueue<Question> sorular = Data.getInstance().getTypedQuestions(ClassicQuestion.class);
        PriorityQueue<Question> found = new PriorityQueue<>(new DataComparator());

        switch (entry.toLowerCase()) {
            case "a":
                searchByQuestionText(found, cmd, sorular);
                break;
            case "b":
                searchByPoint(found, cmd, sorular);
                break;
            case "c":
                searchByDifficulty(found, cmd, sorular);
                break;
            default:
                out("Geçersiz girdi. İşlem iptal ediliyor.");
                return false;
        }

        // Bulunanlar listesinin boş olup olmadığı durum kontrol ediliyor.
        if (found.isEmpty()) {
            out("Girilen filtreye göre soru bulunamadı.");
            return false;
        }

        printFoundList(found);
        return true;
    }

    private void searchByQuestionText(PriorityQueue<Question> found, CommandParameters cmd, PriorityQueue<Question> sorular) {
        cmd.getScanner().nextLine();
        // En son next() ile girdi aldık. nextLine ondan sonra bir defa boş girdi alıyor. Bu boş girdiyi alıp asıl istediğimiz girdiyi alabilmek için boş bir scanner kullandık.
        // Bundan sonra bu boş girdilerde açıklanma satırı bulunmayacaktır.
        out("Lütfen aranacak olan metni girin. (Büyük/küçük harf önemsizdir.)");
        String text = cmd.getScanner().nextLine();

        // Tüm sorular içerisinde soru metninde ilgili anahtar kelimelerin olup olmadığı kontrol ediliyor.
        // Olanlar bulunanların konulduğu found listesine ekleniyor.
        for (Question q : sorular) {
            if (q.getSoru().toLowerCase().contains(text.toLowerCase())) {
                found.add(q);
            }
        }
    }

    private void searchByPoint(PriorityQueue<Question> found, CommandParameters cmd, PriorityQueue<Question> sorular) {
        out("Lütfen aranacak olan soruların sahip olması gereken puanı girin.");
        String enteredPoint = cmd.getScanner().next();

            /*
            Puan girdisini try-catch bloğu ile integer'a parseladık. Eğer girdi integer'a parselanamayacak durumda ise
            -sayı harici karakter içeriyorsa- catch içerisinde girecek. Bu durumda da önceden ayarladığımız point değişkeni
            -1 olarak kalacak. -1 olarak kaldığı içinde girdinin geçersiz olduğu belli olacak.
             */
        int point = -1;
        try {
            point = Integer.parseInt(enteredPoint);
        } catch (Throwable ignore){}
        if (point == -1) {
            out("Geçersiz puan girdisi. İşlem iptal ediliyor.");
            return;
        }

        // Geçerli olan sayı buraya geldi ve uygun olanlar listeye eklendi.
        for (Question q : sorular) {
            if (q.getPuan() == point) {
                found.add(q);
            }
        }
    }

    private void searchByDifficulty(PriorityQueue<Question> found, CommandParameters cmd, PriorityQueue<Question> sorular) {
        out("Lütfen bulunacak olan soruların zorluk derecesini girin.");
        out("kolay - 0");
        out("orta - normal - 1");
        out("zor - 2");

        // Kullanıcı için kolay girdi alınması için yönergeler bastırıldı.

        String difficulty = cmd.getScanner().next();
        switch (difficulty) {
            case "kolay":
            case "0":
                // Döngü ile kolay olan sorular listeye eklendi.
                for (Question q : sorular) {
                    if (q.getZorluk() == Difficulty.EASY) found.add(q);
                }
                break;
            case "orta":
            case "normal":
            case "1":
                // Döngü ile normal olan sorular listeye eklendi.
                for (Question q : sorular) {
                    if (q.getZorluk() == Difficulty.NORMAL) found.add(q);
                }
                break;
            case "zor":
            case "2":
                // Döngü ile zor olan sorular listeye eklendi.
                for (Question q : sorular) {
                    if (q.getZorluk() == Difficulty.HARD) found.add(q);
                }
                break;
            default:
                out("Geçersiz zorluk düzeyi girdiniz. İşlem iptal ediliyor.");
                return;
        }
    }

    // Merkezi bastırma sistemi için:
    private void printFoundList(PriorityQueue<Question> questions) {
        // Bulunanlar listesi boş olmadığı durumda işlem buraya geldi. Burada da bulunanlar listesi döngüye alındı ve
        // bulunan soruların bilgileri bastırıldı.

        List<Question> found = new ArrayList<>();
        for (Question q : questions) {
            found.add(q);
        }
        found.sort(new DataComparator());

        // Comparable sınıfı ile kesin sıralama olması için ek döngü. Zararsız.

        int i = 1;
        for (Question q : found) {
            out("---- " + i++ + " numaralı bulunan soru bilgileri ----");
            q.lightPrintInformation();
        }
        out("Girilen filtreye göre " + --i + " adet soru bulundu ve listelendi.");
    }


    // Üstteki methodun daha temiz görünebilmesi için tüm soruları bastıran methodu buraya aldım.
    private void printAllQuestions() {
        Map<Class<? extends Question>, PriorityQueue<Question>> tumSorular = Data.getInstance().getTumSorular();
        if (tumSorular.isEmpty()) {
            out("Hiç soru bulunamadı :(");
            return;
        }
        List<Question> found = new ArrayList<>();
        for (Map.Entry<Class<? extends Question>, PriorityQueue<Question>> entry : tumSorular.entrySet()) {
            for (Question q : entry.getValue()) {
                found.add(q);
            }
        }
        found.sort(new DataComparator());

        int i = 1;
        for (Question q : found) {
            out("-----------===-----------");
            out("---- " + i++ + " numaralı soru " + q.getQuestionType() + " kategorisinden geliyor. ----");
            q.lightPrintInformation();
        }


        out("\n" + --i + " adet soru bulundu ve listelendi.");

    }

}