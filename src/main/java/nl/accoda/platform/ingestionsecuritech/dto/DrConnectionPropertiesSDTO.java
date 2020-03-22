package nl.accoda.platform.ingestionsecuritech.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DrConnectionPropertiesSDTO {

    private String proxyScanId;
    private String proxyScanStatus;
    String token;
    String customerNbr;
    String locationCode;
    LocalDateTime scanDateTime;
    String date;
    boolean onlyNonRevoked;
    boolean images = true;
    boolean reports = true;


}
