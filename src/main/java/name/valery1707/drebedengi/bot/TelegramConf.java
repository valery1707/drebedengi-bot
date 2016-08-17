package name.valery1707.drebedengi.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram")
public class TelegramConf {
	private String token;

	public String getToken() {
		return token;
	}

	@SuppressWarnings("unused")
	public void setToken(String token) {
		this.token = token;
	}
}
