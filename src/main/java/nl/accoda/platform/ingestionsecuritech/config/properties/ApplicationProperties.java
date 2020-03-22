package nl.accoda.platform.ingestionsecuritech.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;


/**
 * Properties specific to SecurityIngestionService.
 * <p>
 * Properties are configured in the application.yml file.
 */
@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Getter
@Setter
public class ApplicationProperties {

    private QueueProperties queue = new QueueProperties();
    private SecuritechProperties securitech = new SecuritechProperties();


    @Data
    public static class QueueProperties {
        private String name;
    }

    @Data
    public static class  SecuritechProperties {

        private String token;
        private List<Map<String, String>> customers;
        private SecuritechUrl url = new SecuritechUrl();
        private Retention retention = new Retention();
    }

    @Data
    public static class SecuritechUrl{
        private String callBack;
        private String soap;
        private String rest;
    }

    @Data
    public static class Retention {
        private String cron="* * * * * * *";
        private int days = 3;

    }
}
