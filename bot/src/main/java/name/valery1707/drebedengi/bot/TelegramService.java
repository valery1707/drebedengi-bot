package name.valery1707.drebedengi.bot;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import name.valery1707.drebedengi.bot.command.ICommand;
import name.valery1707.drebedengi.bot.command.RegexpCommand;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Singleton
public class TelegramService {

	@Inject
	private TelegramConf conf;

	@Inject
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private List<ICommand> commandList;

	private TelegramBot bot;

	private int updateOffset = 0;

	private final LoadingCache<String, Optional<ICommand>> commandExecutor = Caffeine.newBuilder()
			.build(
					name -> commandList.stream()
							.filter(cmd -> cmd instanceof RegexpCommand)
							.filter(cmd -> ((RegexpCommand) cmd).pattern().matcher(name).find())
							.findFirst()
			);

	@PostConstruct
	public void init() {
		bot = new TelegramBot
			.Builder(conf.getToken())
			.build();
	}

	@Scheduled(fixedDelay = 10 * 1000)
	public void getUpdates() {
		GetUpdatesResponse response = bot.execute(new GetUpdates().offset(updateOffset).limit(100).timeout(0));
		if (response.isOk() && response.updates() != null) {
			updateOffset = response.updates().stream()
					.map(Update::updateId)
					.max(Integer::compare).map(i -> i + 1)//Max received + 1
					.orElse(updateOffset);//Empty list - use current value
			response.updates().stream()
					.map(Update::message)
					.filter(Objects::nonNull)
					.forEachOrdered(this::processUpdate);
		}
	}

	private void processUpdate(Message message) {
		ICommand executor = extractCommand(message.text())
				.flatMap(commandExecutor::get)
				.orElseGet(() -> commandList.stream().filter(cmd -> cmd.canHandle(message)).findFirst().orElse(null));
		if (executor == null) {
			bot.execute(new SendMessage(message.chat().id(), "Incorrect message: " + message.text()));
		} else {
			executor.execute(bot, message);
		}
	}

	@Nonnull
	static Optional<String> extractCommand(String text) {
		if (text == null || !text.trim().startsWith("/")) {
			return Optional.empty();
		}
		text = text.trim();
		int end = text.indexOf(' ');
		return Optional.of(text.substring(0, end >= 0 ? end : text.length()));
	}
}
