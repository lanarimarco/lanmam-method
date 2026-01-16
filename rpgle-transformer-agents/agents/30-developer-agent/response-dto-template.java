package {PACKAGE_NAME}.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for {PROGRAM_NAME}
 * Represents output data for display file: {DISPLAY_FILE}
 */
public class {ProgramName}ResponseDTO {

    // ==================== Data Fields ====================

    @JsonProperty("customerNumber")
    private BigDecimal customerNumber;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("balance")
    private BigDecimal balance;

    // ==================== Response Metadata ====================

    @JsonProperty("success")
    private boolean success = true;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("exited")
    private boolean exited = false;

    @JsonProperty("cancelled")
    private boolean cancelled = false;

    // ==================== Constructors ====================

    public {ProgramName}ResponseDTO() {
    }

    // ==================== Getters and Setters ====================

    public BigDecimal getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(BigDecimal customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return "{ProgramName}ResponseDTO{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                ", success=" + success +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
