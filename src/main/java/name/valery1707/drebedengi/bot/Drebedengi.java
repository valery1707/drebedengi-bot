package name.valery1707.drebedengi.bot;

import javaslang.control.Either;
import javaslang.control.Try;
import name.valery1707.drebedengi.domain.BaseRequest;
import name.valery1707.drebedengi.domain.BaseResponse;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.function.Function;

@Service
@Singleton
public class Drebedengi {
	public static final MediaType SOAP = MediaType.parse("application/soap+xml; charset=utf-8");

	private final DrebedengiConf conf;

	private OkHttpClient httpClient;

	@Inject
	public Drebedengi(DrebedengiConf conf) {
		this.conf = conf;
	}

	@PostConstruct
	public void init() throws IOException {
		httpClient = new OkHttpClient.Builder()
				.build();
	}

	private static final String SOAP_REQ_PREFIX =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
			"\t\t\t\t   xmlns:ns1=\"urn:ddengi\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
			"\t\t\t\t   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
			"\t\t\t\t   xmlns:ns2=\"http://xml.apache.org/xml-soap\"\n" +
			"\t\t\t\t   SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
			"\t<SOAP-ENV:Body>";
	private static final String SOAP_REQ_SUFFIX =
			"\t</SOAP-ENV:Body>\n" +
			"</SOAP-ENV:Envelope>";

	public <R extends BaseResponse> Try<R> request(BaseRequest request, Function<String, R> response) {
		request.setApiId(conf.getApi().getKey());
		request.setLogin("demo@example.com");
		request.setPass("demo");
		String operation = StringUtils.uncapitalize(request.getClass().getSimpleName());
		StringBuilder soap = new StringBuilder(SOAP_REQ_PREFIX);
		soap.append("<ns1:").append(operation).append(">\n");
		request.write(soap);
		soap.append("</ns1:").append(operation).append(">\n");
		soap.append(SOAP_REQ_SUFFIX);
		return request(soap.toString()).map(response);
	}

	public Try<String> request(String soap) {
		try {
			Response response = httpClient
					.newCall(
							new Request.Builder()
									.url(conf.getApi().getUrl())
									.post(RequestBody.create(SOAP, soap))
									.build()
					)
					.execute();
			if (response.isSuccessful()) {
				return Try.success(response.body().string());
			} else {
				return Try.failure(new RemoteInvocationFailureException(response.message(), null));
			}
		} catch (IOException e) {
			return Try.failure(e);
		}
	}
}
