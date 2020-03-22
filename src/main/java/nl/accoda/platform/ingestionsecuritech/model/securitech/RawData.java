package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RawData {

    @JsonProperty(value = "BirthDate")
    private String birthDate;
    @JsonProperty(value = "DocumentNumber")
    private String documentNumber;
    @JsonProperty(value = "DocumentType")
    private String documentType;
    @JsonProperty(value = "ExpirationDate")
    private String expirationDate;
    @JsonProperty(value = "Gender")
    private String gender;
    @JsonProperty(value = "IssuingCountry")
    private String issuingCountry;
    @JsonProperty(value = "LastName")
    private String lastName;
    @JsonProperty(value = "FirstName")
    private String firstName;
    @JsonProperty(value = "MRZLines")
    private ArrayList<String> mrzLines;
    @JsonProperty(value = "Nationality")
    private String nationality;
    @JsonProperty(value = "Optional1")
    private String optional1;
    @JsonProperty(value = "Optional2")
    private String optional2;

}
