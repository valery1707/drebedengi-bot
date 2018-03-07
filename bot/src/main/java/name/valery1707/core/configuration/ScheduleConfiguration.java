package name.valery1707.core.configuration;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration
@EnableScheduling
public class ScheduleConfiguration implements SchedulingConfigurer {
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean(destroyMethod = "shutdown")
	public ScheduledExecutorService taskExecutor() {
		ThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.daemon(true)
				.namingPattern("spring-schedule-%d")
				.build();
		return Executors.newScheduledThreadPool(10, threadFactory);
	}
}
