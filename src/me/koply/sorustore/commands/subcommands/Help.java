package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;

import java.util.Map;

@CommandInfo("help")
public class Help extends ACommand {

    @Override
    protected void onCommand(CommandParameters cmd) {
        // Döngülerde String üleştirmesinde performans için StringBuilder kullandık.
        StringBuilder sb = new StringBuilder();

        // Komutlar Map'ini döngüye aldık.
        for (Map.Entry<String, ACommand> entry : cmd.getCommandsMap().entrySet()) {
            // Komutun adını ekleyip alt satıra geçirdik.
            sb.append(entry.getKey()).append("\n");
        }
        // Tüm komutlar eklendikten sonra bir ayraç ekledik.
        sb.append("------");

        // StringBuilder ile hazırladığımız metni gönderdik.
        out("Halihazırda bulunan komutlar şunlardır: \n" + sb.toString());
    }

}