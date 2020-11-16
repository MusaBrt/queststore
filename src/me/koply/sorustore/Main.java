package me.koply.sorustore;

import me.koply.sorustore.commands.CommandHandler;
import me.koply.sorustore.utilities.DataManager;

public final class Main {

    // Main içerisinde programın asıl çalışma alanı hazırlanıyor
    private Main() {
        System.out.println("Soru Mağazası");
        System.out.println("\n -       -       -  ");
        System.out.println("\nÇıkmak için exit yazabilirsiniz.");

        // Komut sisteminden önce DataManager çağrılıp dosyalar okunacak.
        DataManager.getInstance().loadAllDatas();

        // Uygulamanın kapanma eventinde dataların kaydedilmesini sağladık.
        // Bu blok CommandHandler'dan sonra olursa hiçbir zaman okunamaz, taa ki program kapanmadan hemen öncesine kadar.
        // Program uygulama içindeki exit komutu ile kapanmazsa bu blokta okunmayacağı için işe yaramayacak. O yüzden burada.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DataManager.getInstance().saveAllDatas(), "Shutdown-thread"));

        // Komut sistemi açılıyor.
        new CommandHandler();
    }

    // Program buradan başlıyor
    public static void main(String[] args) {
        new Main();
    }
}