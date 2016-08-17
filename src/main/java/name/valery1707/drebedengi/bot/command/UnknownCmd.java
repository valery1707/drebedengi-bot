package name.valery1707.drebedengi.bot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import java.util.regex.Pattern;

@Service
@Singleton
@Order(20)
public class UnknownCmd extends RegexpCommand {
	public UnknownCmd() {
		super(Pattern.compile("^\\s*/\\w+\\b"));
	}

	@Override
	public void execute(TelegramBot bot, Message message) {
		bot.execute(new SendMessage(message.chat().id(), "Unknown command: " + message.text()));
	}
}
