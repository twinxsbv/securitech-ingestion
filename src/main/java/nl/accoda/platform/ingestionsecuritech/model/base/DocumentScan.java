package nl.accoda.platform.ingestionsecuritech.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.accoda.platform.ingestionsecuritech.util.DocType;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public abstract class DocumentScan {

    private DocType documentType;
    private String documentNumber;
    private LocalDate dateOfIssue;
    private LocalDate dateOfExpiry;
    private String issuingCountry;
    private PersonalData personalData;
    private List<DocumentData> documentData = new ArrayList<>();
    private Instant scanDate;

}
