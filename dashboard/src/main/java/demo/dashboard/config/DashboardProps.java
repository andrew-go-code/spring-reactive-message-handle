package demo.dashboard.config;

import demo.dashboard.model.MessageService;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dashboard.services")
public class DashboardProps {
    private MessageService messageCenter;
    private MessageService mrAnalyst;
    private MessageService police;
}
