package me.koply.sorustore.commands;

import me.koply.sorustore.commands.annotations.CommandInfo;
import org.reflections.Reflections;

import java.util.*;

public class CommandHandler {

    // Tüm proje boyunca kullanılacak olan "Scanner" kuruluyor.
    private final Scanner scanner = new Scanner(System.in);

    // Çalışacak olan komutların listesi burada tutuluyor.
    // Key değeri komutun kendisi olup value olarak komutun objesini tutuyor.
    // polymorphism
    private final Map<String, ACommand> commands = new HashMap<>();

    public CommandHandler() {
        // Komutlar kaydediliyor.
        registerCommands();

        // Komut sistemi açılıyor
        commandsMethod();
    }

    // Komut sistemi
    private void commandsMethod() {
        // Uygulamanın tek seferde çalışmasını engellemek için döngü koyduk.
        // Döngüyü sona erdirmek için aşağıda "exit" girdisini kontrol ettik.
        boolean running = true;
        while (running) {
            // Daha kullanıcı dostu olması amacıyla girdi satırlarının başına ">" ekledik.
            out("\n> ");
            // Komut girdisi alınıyor.
            // Komutlar tek kelime olduğu için Scanner#next() kullandık.
            // Çok kelime olsaydı Scanner#nextLine() kullanmamız doğru olurdu.
            String rawCommand = scanner.next();

            // Eğer komut boşsa bilinmeyen komut mesajı atıp tekrar döngünün başına geliyor.
            if (rawCommand.equals("") || rawCommand.equals(" ")) {
                out("\nUnknown command.");
                continue;
            }

            // Eğer girdi exit ise programdan çıkılmasını sağlamak için döngü değerini false yapıyor.
            if (rawCommand.equals("exit")) {
                running = false;
                out("\nGoodbye");
                continue;
            }

            // Girilen komutu boşluklardan ayırdık. Böylece çoklu girdilerde hata almamamız sağlandı.
            String[] args = rawCommand.split(" ");

            // Girilen komutun komutlar listesinde olup olmadığı kontrol edildi.
            // Eğer komut listede yoksa geçersiz komut mesajı gönderilip döngü tekrar başa alındı.
            if (!commands.containsKey(args[0])) {
                out("\nUnknown command.");
                continue;
            }

            // Komutlar listesinden ilgili komut çekildi.
            ACommand aco = commands.get(args[0]);

            // Geliştirme araçları olarak opsiyonel gecikme ölçer. Gereksiz olduğu düşünüldüğünde silinebilir.
            long firstTime = System.currentTimeMillis();

            aco.onCommand(new CommandParameters(rawCommand, args, commands, scanner)); // Komut çağrılıyor

            // Gecikme değeri ekrana basılıyor.
            out((System.currentTimeMillis() - firstTime) + "ms");

        }
    }

    // Komutlar reflection ile kaydediliyor. Böylece her komut için new Object() yapmaktan kurtuluyoruz.
    private void registerCommands() {
        // Commands package'ı içinde olan subcommands package'ını alıyoruz.
        System.out.println(CommandHandler.class.getPackage().getName() + ".subcommands");
        Reflections reflections = new Reflections(CommandHandler.class.getPackage().getName() + ".subcommands");

        // Subcommands package'ı içinde ACommands'ı extends eden sınıfları alıyoruz
        Set<Class<? extends ACommand>> classes = reflections.getSubTypesOf(ACommand.class);

        // Aldığımız sınıfları foreach döngüsü ile construct edip "commands" map'ı içine alıyoruz.
        for (Class<? extends ACommand> claz : classes) {
            // Subcommands içinde olan sınıflarda CommandInfo etiketi zorunlu olduğu için olup olmadığını kontrol eden bir satır eklemedik.
            CommandInfo ci = claz.getAnnotation(CommandInfo.class);

            try {
                ACommand cmd = claz.newInstance(); // Komutu new Object() şeklinde construct ettik.

                commands.put(ci.value(), cmd); // Commands mapi içine aldık.
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Daha kolay yazım olması için System.out.print fonksiyonunu kısalttık.
    public final void out(Object o) {
        System.out.print(o + "");
    }
}