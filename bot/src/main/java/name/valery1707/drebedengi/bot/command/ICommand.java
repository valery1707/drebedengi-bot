package name.valery1707.drebedengi.bot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;

public interface ICommand {
	default boolean canHandle(Message message) {
		return canHandle(message.text());
	}

	boolean canHandle(String message);

	void execute(TelegramBot bot, Message message);
}
