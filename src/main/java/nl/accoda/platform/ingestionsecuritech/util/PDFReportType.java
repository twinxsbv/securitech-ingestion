package nl.accoda.platform.ingestionsecuritech.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PDFReportType {

    PDF_WITH_IMAGES(0), PDF_WITHOUT_IMAGES(1), UNKNOWN(99);

    @JsonProperty(value = "PDFReportType")
    private final int pdfReportType;

    private PDFReportType(int type) {
        this.pdfReportType = type;
    }
}
