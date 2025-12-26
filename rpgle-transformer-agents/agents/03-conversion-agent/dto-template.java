package {PACKAGE_NAME}.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for {PROGRAM_NAME}
 * Represents data from RPGLE display file: {DISPLAY_FILE}
 */
public class {ProgramName}DTO {

    // Input fields (from display file input fields)
    @JsonProperty("customerNumber")
    private BigDecimal customerNumber;

    // Output fields (from display file output fields)
    @JsonProperty("customerName")
    private String customerName;

    // Control fields
    @JsonProperty("f3Pressed")
    private boolean f3Pressed;  // Exit key

    @JsonProperty("f12Pressed")
    private boolean f12Pressed;  // Cancel key

    // Response metadata
    @JsonProperty("success")
    private boolean success = true;

    @JsonProperty("errorMessage")
    private String errorMessage;

    // Constructors
    public {ProgramName}DTO() {
    }

    // Getters and Setters
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

    public boolean isF3Pressed() {
        return f3Pressed;
    }

    public void setF3Pressed(boolean f3Pressed) {
        this.f3Pressed = f3Pressed;
    }

    public boolean isF12Pressed() {
        return f12Pressed;
    }

    public void setF12Pressed(boolean f12Pressed) {
        this.f12Pressed = f12Pressed;
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

    @Override
    public String toString() {
        return "{ProgramName}DTO{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                ", success=" + success +
                '}';
    }
}
