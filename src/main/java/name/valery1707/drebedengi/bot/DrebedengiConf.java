package name.valery1707.drebedengi.bot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@ConfigurationProperties(prefix = "drebedengi")
public class DrebedengiConf {
	private Api api;

	public Api getApi() {
		return api;
	}

	@SuppressWarnings("unused")
	public void setApi(Api api) {
		this.api = api;
	}

	public static class Api {
		private URL url;
		private String key;

		public URL getUrl() {
			return url;
		}

		@SuppressWarnings("unused")
		public void setUrl(URL url) {
			this.url = url;
		}

		public String getKey() {
			return key;
		}

		@SuppressWarnings("unused")
		public void setKey(String key) {
			this.key = key;
		}
	}
}
