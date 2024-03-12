package project.app.apns_server.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import project.app.apns_server.modules.service.scheduler.TimeUnitConvertService;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private final TimeUnitConvertService timeUnitConvertService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("[SchedulerConfig configureTasks] processor count = {}", processors);
        threadPoolTaskScheduler.setPoolSize(processors);
        threadPoolTaskScheduler.setThreadNamePrefix("schedulerTask-");
        threadPoolTaskScheduler.initialize(); //Set up the ExecutorService.
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Bean
    public Map<String, ScheduledFuture<?>> scheduledTasks() {
        return new HashMap<>();
    }

    @Bean
    public Trigger trigger() {
        Duration duration = timeUnitConvertService.getPeriodToDuration();
        PeriodicTrigger trigger = new PeriodicTrigger(duration);
        trigger.setInitialDelay(duration);
        return trigger;
    }
}
