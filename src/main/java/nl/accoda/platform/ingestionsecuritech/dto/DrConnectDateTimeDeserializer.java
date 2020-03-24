package nl.accoda.platform.ingestionsecuritech.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class DrConnectDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {


        // Strip de  prefix en de  om een formaat over te houden in de trant van 1535491858840-0500
        String epochAndOffset = p.getText().substring(6, p.getText().length() - 2);

        // 1535491858840
        String epoch = epochAndOffset.substring(0, epochAndOffset.length() - 5);

        // -0500 Note, behoudt de indicator
        String offset = epochAndOffset.substring(epoch.length());
        ZoneId zoneId = ZoneId.of("UTC" + offset);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(epoch)), zoneId);
        return localDateTime;

    }

}



