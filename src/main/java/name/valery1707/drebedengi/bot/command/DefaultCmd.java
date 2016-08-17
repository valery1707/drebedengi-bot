package name.valery1707.drebedengi.bot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;

@Service
@Singleton
@Order(30)
public class DefaultCmd implements ICommand {
	@Override
	public boolean canHandle(String message) {
		return true;
	}

	@Override
	public void execute(TelegramBot bot, Message message) {
		bot.execute(new SendMessage(message.chat().id(), "Echo: " + message.text()));
	}
}
