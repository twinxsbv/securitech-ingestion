package nl.accoda.platform.ingestionsecuritech.config;

import nl.accoda.platform.ingestionsecuritech.ScanConsumer;
import nl.accoda.platform.ingestionsecuritech.model.base.SecuritechDocumentScan;
import nl.accoda.platform.ingestionsecuritech.services.ProxyScanService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class MessagingConfig {

    private final ProxyScanService proxyScanService;

    public MessagingConfig(ProxyScanService proxyScanService) {
        this.proxyScanService = proxyScanService;
    }

    // Todo: move to indentity microservice and digest generic scan-model
    @Bean
    public ScanConsumer log() {
        return new ScanConsumer();
    }

    @Bean
    public Supplier<SecuritechDocumentScan> supply() {
       return proxyScanService::getLatestSecuritechScans;
    }


}
