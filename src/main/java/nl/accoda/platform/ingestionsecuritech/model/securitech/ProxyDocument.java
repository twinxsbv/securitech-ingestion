package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProxyDocument {

    @JsonProperty(value = "PersonalData")
    private PersonalInfo personalData;
    @JsonProperty(value = "DocumentData")
    private DocumentInfo documentData;
    @JsonProperty(value = "DocumentSides")
    private List<DocumentSide> documentSides = new ArrayList<>();
    @JsonProperty(value = "RawData")
    private RawData rawData;

}
