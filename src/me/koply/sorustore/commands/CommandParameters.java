package me.koply.sorustore.commands;

import java.util.Map;
import java.util.Scanner;

public class CommandParameters {

    // Komutlar içerisinde kullanılacak olan verilerin tutulacağı obje

    CommandParameters(String rawCommand, String[] args, Map<String, ACommand> commandsMap, Scanner scanner) {
        this.rawCommand = rawCommand;
        this.args = args;
        this.commandsMap = commandsMap;
        this.scanner = scanner;
    }

    private String rawCommand;
    private String[] args;
    private Map<String, ACommand> commandsMap;
    private Scanner scanner;

    public Scanner getScanner() {
        return scanner;
    }

    public String getRawCommand() {
        return rawCommand;
    }

    public String[] getArgs() {
        return args;
    }

    public Map<String, ACommand> getCommandsMap() {
        return commandsMap;
    }
}