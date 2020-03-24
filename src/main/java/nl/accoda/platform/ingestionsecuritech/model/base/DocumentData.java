package nl.accoda.platform.ingestionsecuritech.model.base;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DocumentData {

    public enum ReportType {
        PDF_WITHOUT_IMAGES, PDF_WITH_IMAGES
    }
    private ReportType reportType;
    private byte[] report;

    public DocumentData(ReportType reportType, byte[] report) {
        this.reportType = reportType;
        this.report = report;
    }
}
