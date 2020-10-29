package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.objects.enums.Difficulty;
import me.koply.sorustore.objects.questions.MultipleChoiceQuestion;
import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;

@CommandInfo("examples")
public class Examples extends ACommand {

    @Override
    protected void onCommand(CommandParameters cmd) {

        // Bu komut tamamen uygulamanın test edilmesi sırasında soruları eklemeyi kolaylaştırmak amacıyla yazılmıştır.
        // Reel amaca hizmet edecek bir komut değildir.

        // Sorular şıkları array olarak alıyor. Sorular için şıkları ayarladık.
        String[] lele = {"a şıkkı","b şıkkı","c şıkkı","d şıkkı","e şıkkı"};
        String[] lala = {"2. soru a şıkkı","2. soru b şıkkı","2. soru c şıkkı","2. soru d şıkkı","2. soru e şıkkı"};


        MultipleChoiceQuestion a = new MultipleChoiceQuestion(true)
                .setCevap('a')
                .setCevaplar(lele);
        a.setSoru("merhaba arkadaşlar bu birinci sorudur. teşekkür ederiz.");
        a.setPuan(30);
        a.setZorluk(Difficulty.EASY);



        MultipleChoiceQuestion b = new MultipleChoiceQuestion(true)
                .setCevap('c')
                .setCevaplar(lala);
        b.setSoru("merhaba arkadaşlar bu ikinci sorudur. teşekkür ederiz.");
        b.setPuan(35);
        b.setZorluk(Difficulty.NORMAL);

        // Soru objeleri oluşturulup verileri depoladığımız sınıf içerisinden işaretlediğimiz sorular
        // ArrayList'i içine ekledik.
        out("ok");
    }
}