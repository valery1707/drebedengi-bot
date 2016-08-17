package name.valery1707.drebedengi.bot;

import javaslang.control.Try;
import name.valery1707.drebedengi.domain.BaseResponse;
import name.valery1707.drebedengi.domain.GetAccessStatus;
import name.valery1707.drebedengi.domain.GetAccessStatusResponse;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class DrebedengiTest {

	private DrebedengiConf conf;
	private Drebedengi drebedengi;

	@Before
	public void setUp() throws Exception {
		conf = new DrebedengiConf();
		conf.setApi(new DrebedengiConf.Api());
		conf.getApi().setUrl(new URL("http://www.drebedengi.ru/soap/"));
		conf.getApi().setKey("demo_api");
		drebedengi = new Drebedengi(conf);
		drebedengi.init();
	}

	@Test
	public void testGetAccessStatus_ok() throws Exception {
		Try<GetAccessStatusResponse> response = drebedengi.request(new GetAccessStatus(), GetAccessStatusResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().isSuccess()).isTrue();
		assertThat(response.get().getFailCode()).isNull();
		assertThat(response.get().getFailMessage()).isNullOrEmpty();
		assertThat(response.get().getValue()).isEqualTo(1);
	}

	@Test
	public void testGetAccessStatus_fail() throws Exception {
		conf.getApi().setKey("----");
		Try<GetAccessStatusResponse> response = drebedengi.request(new GetAccessStatus(), GetAccessStatusResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().isSuccess()).isFalse();
		assertThat(response.get().getFailCode()).isEqualTo(50);
		assertThat(response.get().getFailMessage()).isNotEmpty().contains(conf.getApi().getKey());
		assertThat(response.get().getValue()).isNull();
	}
}
