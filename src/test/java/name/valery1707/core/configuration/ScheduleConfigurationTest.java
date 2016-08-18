package name.valery1707.core.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import static org.assertj.core.api.Assertions.assertThat;

public class ScheduleConfigurationTest {

	private ScheduleConfiguration configuration;

	@Before
	public void setUp() throws Exception {
		configuration = new ScheduleConfiguration();
	}

	@Test
	public void testConfigureTasks() throws Exception {
		ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
		configuration.configureTasks(registrar);
		assertThat(registrar.getScheduler()).isNotNull();
	}

	@Test
	public void testTaskExecutor() throws Exception {
		assertThat(configuration.taskExecutor()).isNotNull();
	}
}