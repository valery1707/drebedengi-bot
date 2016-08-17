package name.valery1707.drebedengi.bot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;

@Service
@Singleton
@Order(10)
public class StartCmd extends RegexpCommand {
	public StartCmd() {
		super("start");
	}

	@Override
	public void execute(TelegramBot bot, Message message) {
		bot.execute(
				new SendMessage(message.chat().id(), "Welcome to Drebedengi Bot.")
						.parseMode(ParseMode.Markdown)
						.replyMarkup(new ReplyKeyboardMarkup(
								new String[]{"/login", "/logout"}
						))
		);
	}
}
