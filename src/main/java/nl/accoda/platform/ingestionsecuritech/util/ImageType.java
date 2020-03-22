package nl.accoda.platform.ingestionsecuritech.util;

public enum ImageType {


    VC(0),
    IC(1), UC(2), SC(3), TC(4), PC(5), SCP(6), UNKNOWN(99);

    private final int type;

    private ImageType(int type) {
        this.type = type;
    }

}
