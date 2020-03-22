package nl.accoda.platform.ingestionsecuritech;

import lombok.extern.slf4j.Slf4j;
import nl.accoda.platform.ingestionsecuritech.model.base.SecuritechDocumentScan;
import org.springframework.stereotype.Component;


import java.util.function.Consumer;

@Slf4j
@Component
public class ScanConsumer implements Consumer<SecuritechDocumentScan> {

    @Override
    public void accept(SecuritechDocumentScan scan) {
         log.info("Processing log function for list {}", scan);

    }

}
