package project.app.apns_server.modules.service.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimeUnitConvertService {

    private static final long MILLISECONDS = 1_000;

    @Value("${weather.search.scheduler.period}")
    private Long period;

    @Value("${weather.search.scheduler.time-unit}")
    private String timeUnit;

    public String getTimeUnit() {
        return switch (this.timeUnit) {
            case "HOURS" -> "시";
            case "MINUTES" -> "분";
            case "SECONDS" -> "초";
            default -> this.timeUnit;
        };
    }

    public Long getPeriodToMilliseconds() {
        return switch (this.timeUnit) {
            case "HOURS" -> this.period*MILLISECONDS*3600;
            case "MINUTES" -> this.period*MILLISECONDS*60;
            case "SECONDS" -> this.period*MILLISECONDS;
            default -> this.period;
        };
    }

    public Long getPeriod() {
        return this.period;
    }
}
