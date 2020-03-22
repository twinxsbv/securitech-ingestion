package nl.accoda.platform.ingestionsecuritech.util;

public enum DocReviewResultFailReason {


    NONE(0),
    DOCUMENT_IS_EXPIRED(1),
    DOCUMENT_NOT_VALID_FOR_PURPOSE(2),
    NO_REVIEW_POSSIBLE_WEAR_AND_TEAR(3),
    NO_REVIEW_POSSIBLE_DATA_INCOMPLETE(4),
    DOCUMENT_IS_FAKE_FALSIFIED(5),
    OTHER(99);

    private final int docReviewResultFailREason;

    private DocReviewResultFailReason(int reason) {
        this.docReviewResultFailREason = reason;
    }
}
