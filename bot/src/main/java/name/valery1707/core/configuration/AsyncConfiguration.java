package name.valery1707.core.configuration;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {
	@Override
	@Bean(destroyMethod = "shutdown")
	public ScheduledExecutorService getAsyncExecutor() {
		ThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.daemon(true)
				.namingPattern("spring-async-%d")
				.build();
		return Executors.newScheduledThreadPool(10, threadFactory);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
}
