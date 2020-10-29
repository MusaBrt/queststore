package me.koply.sorustore.commands.subcommands;

import me.koply.sorustore.commands.ACommand;
import me.koply.sorustore.commands.CommandParameters;
import me.koply.sorustore.commands.annotations.CommandInfo;
import me.koply.sorustore.utilities.DataManager;

@CommandInfo("savedatas")
public class SaveDatas extends ACommand {

    @Override
    protected void onCommand(CommandParameters cmd) {
        out("Tüm veriler data dosyalarına kaydediliyor.");
        DataManager.getInstance().saveAllDatas();
        out("İşlem tamamlandı.");
    }
}