package name.valery1707.drebedengi.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Service
@Singleton
public class TelegramService {

	@Inject
	private TelegramConf conf;

	private TelegramBot bot;

	private int updateOffset = 0;

	@PostConstruct
	public void init() {
		bot = TelegramBotAdapter.build(conf.getToken());
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
		bot.execute(new SendMessage(message.chat().id(), "Echo: " + message.text()));
	}
}
