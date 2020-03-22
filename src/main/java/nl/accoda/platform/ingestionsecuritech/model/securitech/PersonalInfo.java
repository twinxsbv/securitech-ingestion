package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectDateTimeDeserializer;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectSexDeserializer;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonalInfo {


    @JsonDeserialize(using = DrConnectDateTimeDeserializer.class)
    @JsonProperty(value = "BirthDate")
    private LocalDateTime birthDate;

    @JsonProperty(value = "BSN")
    private String bsn;

    @JsonProperty(value = "FirstName")
    private String firstName;

    @JsonProperty(value = "Gender")
    @JsonDeserialize(using = DrConnectSexDeserializer.class)
    private String gender;

    @JsonProperty(value = "Initials")
    private String initials;

    @JsonProperty(value = "LastName")
    private String lastName;

    @JsonProperty(value = "LastNamePrefix")
    private String lastNamePrefix;

    @JsonProperty(value = "Nationality")
    private String nationality;

    @JsonProperty(value = "Optional1")
    private String optional1;

    @JsonProperty(value = "Optional2")
    private String optional2;

}
