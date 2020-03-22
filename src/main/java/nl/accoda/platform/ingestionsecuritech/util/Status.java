package nl.accoda.platform.ingestionsecuritech.util;

public enum Status {

    NEW(0),
    PROCESSED(1),
    REVOKED(2),
    DELETED_FROM_DRCONNECT(3);

    private final int statusCode;

    private Status(int statusCode) {
        this.statusCode = statusCode;
    }

}
