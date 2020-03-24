package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectDateDeserializer;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectDateTimeDeserializer;
import nl.accoda.platform.ingestionsecuritech.dto.ProxyScanDTO;
import nl.accoda.platform.ingestionsecuritech.util.DocStatus;
import nl.accoda.platform.ingestionsecuritech.util.DocType;
import nl.accoda.platform.ingestionsecuritech.util.ImageType;
import nl.accoda.platform.ingestionsecuritech.util.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "proxyScan")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyScan {

    @Id
    private String id;
    @JsonProperty(value = "CustomerNbr")
    private String customerNbr;
    private String token;
    @JsonProperty(value = "LocationCode")
    private String locationCode;
    @JsonProperty(value = "DateTimeScan")
    @JsonDeserialize(using = DrConnectDateTimeDeserializer.class)
    private LocalDateTime dateTimeScan;
    @JsonProperty(value = "DocStatus")
    private DocStatus docStatus;
    @JsonProperty(value = "Document")
    private ProxyDocument document;
    private DocType docType;
    private boolean isRevoked;
    private boolean isRFIDDocument;

    private String appId;
    private Status status;
    private LocalDateTime created;
    private LocalDateTime modify_date;

    public ProxyScan() {
        this.status = Status.NEW;
        this.created = LocalDateTime.now();
    }


}
