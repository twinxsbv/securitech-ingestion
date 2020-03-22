package nl.accoda.platform.ingestionsecuritech.dto;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;

public class DrConnectDateModule extends SimpleModule {

    public DrConnectDateModule() {
        addDeserializer(LocalDateTime.class, new DrConnectDateTimeDeserializer());
    }
}
