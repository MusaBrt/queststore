package me.koply.sorustore;

import me.koply.sorustore.commands.CommandHandler;
import me.koply.sorustore.utilities.DataManager;

public class Main {


    // Main içerisinde programın asıl çalışma alanı hazırlanıyor
    private Main() {
        out("Soru Mağazası");
        out("\n -       -       -  ");
        out("\nÇıkmak için exit yazabilirsiniz.");


        // Komut sisteminden önce DataManager çağrılıp dosyalar okunacak.
        DataManager.getInstance().loadAllDatas();

        // Uygulamanın kapanma eventinde dataların kaydedilmesini sağladık.
        // Bu blok CommandHandler'dan sonra olursa hiçbir zaman okunamaz, taa ki program kapanmadan hemen öncesine kadar.
        // Program uygulama içindeki exit komutu ile kapanmazsa bu blokta okunmayacağı için işe yaramayacak. O yüzden burada.
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                DataManager.getInstance().saveAllDatas();
            }
        }, "Shutdown-thread"));

        // Komut sistemi açılıyor.
        new CommandHandler();
    }

    // Program buradan başlıyor
    public static void main(String[] args) {
        new Main();
    }

    // Daha kullanışlı olması için out methodu
    private void out(Object o) {
        System.out.print(o + "");
    }

}