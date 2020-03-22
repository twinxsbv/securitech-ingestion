package nl.accoda.platform.ingestionsecuritech.dto;

import java.time.LocalDateTime;

public class DrConnectionPropertiesSDTOBuilder {

    String token;
    String customerNbr;
    String locationCode;
    String scanDateTime;
    String date;
    boolean onlyNonRevoked;
    boolean images = true;
    boolean reports = true;


    private static DrConnectionPropertiesSDTO builder = new DrConnectionPropertiesSDTO();

    public static DrConnectionPropertiesSDTOBuilder anConnection() {
        return new DrConnectionPropertiesSDTOBuilder();
    }

    public DrConnectionPropertiesSDTOBuilder withToken(String token) {
        builder.setToken(token);
        return this;
    }

    public DrConnectionPropertiesSDTOBuilder withCustomerNbr(String customerNbr) {
        builder.setCustomerNbr(customerNbr);
        return this;
    }

    public DrConnectionPropertiesSDTOBuilder withLocationCode(String locationCode) {
        builder.setLocationCode(locationCode);
        return this;

    }

    public DrConnectionPropertiesSDTOBuilder withScanDateTime(LocalDateTime scanDateTime) {
        builder.setScanDateTime(scanDateTime);
        return this;
    }

    public DrConnectionPropertiesSDTOBuilder withDate(String date) {
        builder.setDate(date);
        return this;
    }

    public DrConnectionPropertiesSDTOBuilder withOnlyNonRevoked(boolean onlyNonRevoked) {
        builder.setOnlyNonRevoked(onlyNonRevoked);
        return this;
    }

    public DrConnectionPropertiesSDTOBuilder withImages(boolean images) {
        builder.setImages(images);
        return this;
    }

    public DrConnectionPropertiesSDTOBuilder withReports(boolean reports) {
        builder.setReports(reports);
        return this;
    }
}
