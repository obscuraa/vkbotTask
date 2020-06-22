package command;

import core_modules.Command;
import vk.VKMessageSender;
import com.vk.api.sdk.objects.messages.Message;

public class QuoteCommand extends Command {
    public QuoteCommand(String name) {
        super(name);
    }

    @Override
    public void exec(Message message) {
        new VKMessageSender().sendMessage("Вы сказали: " + message.getBody(), message.getUserId());
    }
}