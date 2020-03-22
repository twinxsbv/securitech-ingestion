package nl.accoda.platform.ingestionsecuritech.util;

public enum DocType {

    PASSPORT(0),
    IDCARD(1),
    LICENSE(2),
    VISA(3), UNKNOWN(99);

    private final int type;

    private DocType(int type) {
        this.type = type;
    }
}
