package project.app.apns_server.modules.service.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class TimeUnitConvertService {

    @Value("${weather.search.scheduler.period}")
    private Long period;

    @Value("${weather.search.scheduler.time-unit}")
    private String timeUnit;

    public String getTimeUnitToKorean() {
        return switch (this.timeUnit) {
            case "HOURS" -> "시";
            case "MINUTES" -> "분";
            case "SECONDS" -> "초";
            default -> this.timeUnit;
        };
    }

    public TimeUnit getTimeUnit() {
        return TimeUnit.valueOf(this.timeUnit);
    }

    public Duration getPeriodToDuration() {
        return switch (this.timeUnit) {
            case "HOURS" -> Duration.ofHours(period);
            case "MINUTES" -> Duration.ofMinutes(period);
            case "SECONDS" -> Duration.ofSeconds(period);
            default -> Duration.ZERO;
        };
    }

    public Long getPeriod() {
        return this.period;
    }
}
