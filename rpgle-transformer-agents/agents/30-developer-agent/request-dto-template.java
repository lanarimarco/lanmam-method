package {PACKAGE_NAME}.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for {PROGRAM_NAME}
 * Represents input data from display file: {DISPLAY_FILE}
 */
public class {ProgramName}RequestDTO {

    // ==================== Input Fields ====================

    @JsonProperty("customerNumber")
    @NotNull(message = "Customer number is required")
    private BigDecimal customerNumber;

    @JsonProperty("customerName")
    @Size(max = 30, message = "Customer name cannot exceed 30 characters")
    private String customerName;

    // ==================== Function Key Fields ====================

    @JsonProperty("f3Pressed")
    private boolean f3Pressed;  // Exit key

    @JsonProperty("f12Pressed")
    private boolean f12Pressed;  // Cancel key

    // ==================== Constructors ====================

    public {ProgramName}RequestDTO() {
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

    // ==================== toString ====================

    @Override
    public String toString() {
        return "{ProgramName}RequestDTO{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
