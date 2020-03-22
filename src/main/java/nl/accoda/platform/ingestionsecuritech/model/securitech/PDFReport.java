package nl.accoda.platform.ingestionsecuritech.model.securitech;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.accoda.platform.ingestionsecuritech.dto.DrConnectPdfReportDeserializer;
import nl.accoda.platform.ingestionsecuritech.util.PDFReportType;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PDFReport {

    @JsonProperty(value = "PDFReportType")
    @JsonDeserialize(using = DrConnectPdfReportDeserializer.class)
    private PDFReportType pdfReportType;

    @JsonProperty(value = "Report")
    private byte[] report;

}
