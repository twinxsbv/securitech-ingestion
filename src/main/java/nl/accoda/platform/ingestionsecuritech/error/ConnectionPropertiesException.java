package nl.accoda.platform.ingestionsecuritech.error;

public class ConnectionPropertiesException extends Exception {

    private String constraint;

    public ConnectionPropertiesException(String constraint, String message) {
        super(message);
        this.constraint = constraint;
    }

    public ConnectionPropertiesException(String constraint, String message, Throwable cause) {
        super(message, cause);
        this.constraint = constraint;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

}
