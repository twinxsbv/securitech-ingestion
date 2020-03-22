package nl.accoda.platform.ingestionsecuritech.model.base;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PersonalData {

    private String firstNames;
    private String initials;
    private String lastNamePrefix;
    private String lastName;
    private LocalDateTime birthDate;
    private String bsn;
    private boolean rtw;
    private String gender;
    private String nationality;


    public PersonalData(String firstNames,
                        String initials,
                        String lastNamePrefix,
                        String lastName,
                        LocalDateTime birthDate,
                        String bsn,
                        boolean rtw,
                        String gender,
                        String nationality) {
        this.firstNames = firstNames;
        this.initials = initials;
        this.lastNamePrefix = lastNamePrefix;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.bsn = bsn;
        this.rtw = rtw;
        this.gender = gender;
        this.nationality = nationality;
    }

}
