package nl.accoda.platform.ingestionsecuritech.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;


public abstract class DocumentScan {


    public enum DocumentType {
        DRIVERS_LICENSE, ID_CARD, PASSPORT
    }

    @Id
    private String id;
    private DocumentType documentType;
    private String documentNumber;
    private LocalDate dateOfIssue;
    private LocalDate dateOfExpiry;
    private String issuingCountry;
    private PersonalData personalData;
    private boolean processed;
    private Instant scanDate;


    public DocumentScan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(LocalDate dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Instant getScanDate() {
        return scanDate;
    }

    public void setScanDate(Instant scanDate) {
        this.scanDate = scanDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentScan that = (DocumentScan) o;
        return processed == that.processed &&
                Objects.equals(id, that.id) &&
                documentType == that.documentType &&
                Objects.equals(documentNumber, that.documentNumber) &&
                Objects.equals(dateOfIssue, that.dateOfIssue) &&
                Objects.equals(dateOfExpiry, that.dateOfExpiry) &&
                Objects.equals(issuingCountry, that.issuingCountry) &&
                Objects.equals(personalData, that.personalData) &&
                Objects.equals(scanDate, that.scanDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentType, documentNumber, dateOfIssue, dateOfExpiry, issuingCountry, personalData, processed, scanDate);
    }

    @Override
    public String toString() {
        return "DocumentScan{" +
                "id='" + id + '\'' +
                ", documentType=" + documentType +
                ", documentNumber='" + documentNumber + '\'' +
                ", dateOfIssue=" + dateOfIssue +
                ", dateOfExpiry=" + dateOfExpiry +
                ", issuingCountry='" + issuingCountry + '\'' +
                ", personalData=" + personalData +
                ", processed=" + processed +
                ", scanDate=" + scanDate +
                '}';
    }
}
