package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectDateTimeDeserializer;
import nl.accoda.platform.ingestionsecuritech.util.DocStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProxyItem {

    @JsonProperty(value = "CustomerNbr")
    private String customerNbr;
    @JsonProperty(value = "DateOfBirth")
    @JsonDeserialize(using = DrConnectDateTimeDeserializer.class)
    private LocalDateTime dateOfBirth;
    @JsonProperty(value = "DocStatus")
    private DocStatus docStatus;
    @JsonProperty(value = "DocType")
    private nl.accoda.platform.ingestionsecuritech.util.DocType DocType;
    @JsonProperty(value = "DocumentNumber")
    private String documentNumber;
    private boolean isRevoked;
    @JsonProperty(value = "LastName")
    private String lastName;
    @JsonProperty(value = "LocationCode")
    private String locationCode;
    @JsonProperty(value = "Photo")
    private byte[] photo;
    @JsonProperty(value = "ScanDateTime")
    @JsonDeserialize(using = DrConnectDateTimeDeserializer.class)
    private LocalDateTime scanDateTime;


}
