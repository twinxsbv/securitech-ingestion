package nl.accoda.platform.ingestionsecuritech.util;


public enum DocStatus {

    PASSED(0),
    FAILED(1),
    OVERRIDE(2),
    UNTESTED(3),
    PENDING(4),
    INVESTIGATION(5);

    private final int docStatusCode;

    private DocStatus(int code) {
        this.docStatusCode = code;
    }
}

