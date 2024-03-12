package project.app.apns_server.modules.service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DelayedStartTrigger implements Trigger {

    private final TimeUnitConvertService timeUnitConvertService;

    private Boolean isFirstExecution = true;

    @Override
    public Instant nextExecution(TriggerContext triggerContext) {
        long delay = timeUnitConvertService.getPeriodToMilliseconds();
        if (isFirstExecution) {
            isFirstExecution= false;
            return Instant.now().plusMillis(delay);
        } else {

            Instant instant = triggerContext.lastScheduledExecution() == null ?
                    Instant.now() : triggerContext.lastScheduledExecution();

            assert instant != null;
            return instant.plusMillis(delay);
        }
    }
}
