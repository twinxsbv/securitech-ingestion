package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectDateTimeDeserializer;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DocumentInfo {

    @JsonProperty(value = "DocumentNumber")
    private String documentNumber;

    @JsonProperty(value = "DocumentType")
    private String documentType;

    @JsonDeserialize(using = DrConnectDateTimeDeserializer.class)
    @JsonProperty(value = "ExpirationDate")
    private LocalDateTime expirationDate;
    @JsonProperty(value = "IssuingCountry")
    private String issuingCountry;

}
