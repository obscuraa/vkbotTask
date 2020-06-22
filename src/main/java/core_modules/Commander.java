package core_modules;

import com.vk.api.sdk.objects.messages.Message;

// Обработка сообщений

public class Commander {
    public static void execute(Message message){
        CheckCommand.getCommand(CommandManager.getCommands(), message).exec(message);
    }
}
