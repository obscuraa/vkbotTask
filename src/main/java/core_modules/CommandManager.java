package core_modules;

import command.QuoteCommand;

import java.util.HashSet;

// Хранилище для объектов комманд

public class CommandManager {
    private static HashSet<Command> commands = new HashSet<>();

    static {
        commands.add(new QuoteCommand("quote"));
    }

    public static HashSet<Command> getCommands(){
        return commands;
    }
    public static void addCommand(Command command) {
        commands.add(command);
    }
}
