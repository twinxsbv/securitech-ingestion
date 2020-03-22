package nl.accoda.platform.ingestionsecuritech.model.base;


import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class SecuritechDocumentScan extends DocumentScan {

    private String token;
    private String customerNumber;
    private String locationCode;

    public SecuritechDocumentScan() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SecuritechDocumentScan that = (SecuritechDocumentScan) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(customerNumber, that.customerNumber) &&
                Objects.equals(locationCode, that.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, customerNumber, locationCode);
    }

    @Override
    public String toString() {
        return "SecuritechDocumentScan{" +
                "token='" + token + '\'' +
                ", customerNumber='" + customerNumber + '\'' +
                ", locationCode='" + locationCode + '\'' +
                "} " + super.toString();
    }
}
