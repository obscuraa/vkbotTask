package core_modules;

import java.util.Collection;
import com.vk.api.sdk.objects.messages.Message;
import command.QuoteCommand;

public class CheckCommand {
    public static Command getCommand(Collection<Command> commands, Message message) {
        String body = message.getBody();
        for (Command command : commands
        ) {
            if (command.name.equals(body.split(" ")[0])) {
                return command;
            }
        }
        return new QuoteCommand("quote");
    }
}
