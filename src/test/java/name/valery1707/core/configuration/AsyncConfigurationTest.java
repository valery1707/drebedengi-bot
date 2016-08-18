package name.valery1707.core.configuration;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AsyncConfigurationTest {

	private AsyncConfiguration configuration;

	@Before
	public void setUp() throws Exception {
		configuration = new AsyncConfiguration();
	}

	@Test
	public void testGetAsyncExecutor() throws Exception {
		assertThat(configuration.getAsyncExecutor()).isNotNull();
	}

	@Test
	public void testGetAsyncUncaughtExceptionHandler() throws Exception {
		assertThat(configuration.getAsyncUncaughtExceptionHandler()).isNull();
	}
}