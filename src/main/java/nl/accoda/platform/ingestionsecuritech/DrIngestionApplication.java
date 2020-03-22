package nl.accoda.platform.ingestionsecuritech;

import nl.accoda.platform.ingestionsecuritech.config.properties.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class DrIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrIngestionApplication.class, args);
    }

}
