package project.app.apns_server.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.app.apns_server.modules.common.aspect.LogAop;

@Configuration
public class AppConfig {

    @Bean
    public LogAop logAop() {
        return new LogAop();
    }
}
