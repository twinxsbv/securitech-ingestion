package nl.accoda.platform.ingestionsecuritech.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PDFReportDTO {

    private String pdfReportType;
    private byte[] report;
}
