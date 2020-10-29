package me.koply.sorustore.commands;

public abstract class ACommand {

    protected abstract void onCommand(CommandParameters cmd);

    public void out(Object o) {
        System.out.println(o + "");
    }

}