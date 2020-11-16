package me.koply.sorustore.commands;

public abstract class ACommand {

    protected abstract void onCommand(CommandParameters cmd);

    public final void out(Object...os) {
        for (Object o: os) { System.out.println(o+"");}
    }

}