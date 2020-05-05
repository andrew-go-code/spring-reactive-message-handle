package demo.dashboard;

import demo.dashboard.config.DashboardProps;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableConfigurationProperties(DashboardProps.class)
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class DashboardApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DashboardApp.class)
                .bannerMode(Banner.Mode.OFF)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
