package project.app.apns_server.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableAsync
@EnableScheduling
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private final int POOL_SIZE = 10;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("myThread");
        threadPoolTaskScheduler.initialize(); //Set up the ExecutorService.

        //생성한 Thread pool을 작업용으로 등록
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
