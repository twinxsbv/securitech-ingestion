package nl.accoda.platform.ingestionsecuritech.model.base;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class SecuritechDocumentScan extends DocumentScan {

    private String token;
    private String customerNumber;
    private String locationCode;

}
