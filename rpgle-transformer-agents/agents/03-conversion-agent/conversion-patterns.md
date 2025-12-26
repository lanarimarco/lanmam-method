# RPGLE to Java Conversion Patterns

## Program Structure Mapping

### RPGLE Program Components → Java Spring Components

| RPGLE Component | Java Spring Equivalent |
|-----------------|------------------------|
| Main C-specs | @Service class methods |
| Subroutines | Private methods in @Service |
| PLIST parameters | Method parameters |
| Global variables | Instance variables (minimize) |
| Display file I/O | REST API + DTO |
| File operations | Repository calls |

## Operation Code Conversions

### File Operations

#### CHAIN (Keyed Read)
```rpgle
CHAIN key CUSTMAST;
IF %FOUND(CUSTMAST);
  // Process record
ENDIF;
```
↓
```java
Optional<Customer> customer = customerRepository.findById(key);
if (customer.isPresent()) {
    // Process record
    Customer cust = customer.get();
}
```

#### READ (Sequential Read)
```rpgle
READ CUSTMAST;
DOW NOT %EOF(CUSTMAST);
  // Process record
  READ CUSTMAST;
ENDDO;
```
↓
```java
List<Customer> customers = customerRepository.findAll();
for (Customer customer : customers) {
    // Process record
}
```

#### SETLL + READ (Positioned Read)
```rpgle
SETLL key CUSTMAST;
READ CUSTMAST;
DOW NOT %EOF(CUSTMAST) AND condition;
  // Process record
  READ CUSTMAST;
ENDDO;
```
↓
```java
List<Customer> customers = customerRepository
    .findByCustomerNumberGreaterThanEqualOrderByCustomerNumber(key);
for (Customer customer : customers) {
    if (!condition) break;
    // Process record
}
```

#### WRITE (Insert)
```rpgle
WRITE CUSTREC;
```
↓
```java
customerRepository.save(customer);
```

#### UPDATE
```rpgle
UPDATE CUSTREC;
```
↓
```java
// Fetch, modify, save
Customer customer = customerRepository.findById(id)
    .orElseThrow(() -> new CustomerNotFoundException(id));
customer.setName(newName);
customerRepository.save(customer);
```

#### DELETE
```rpgle
DELETE CUSTREC;
```
↓
```java
customerRepository.deleteById(id);
```

### Control Flow

#### IF/ELSE
```rpgle
IF condition;
  // statements
ELSE;
  // statements
ENDIF;
```
↓
```java
if (condition) {
    // statements
} else {
    // statements
}
```

#### DOW (Do While)
```rpgle
DOW condition;
  // statements
ENDDO;
```
↓
```java
while (condition) {
    // statements
}
```

#### FOR
```rpgle
FOR i = 1 TO 10;
  // statements
ENDFOR;
```
↓
```java
for (int i = 1; i <= 10; i++) {
    // statements
}
```

#### SELECT/WHEN
```rpgle
SELECT;
  WHEN condition1;
    // statements
  WHEN condition2;
    // statements
  OTHER;
    // statements
ENDSL;
```
↓
```java
if (condition1) {
    // statements
} else if (condition2) {
    // statements
} else {
    // statements
}
// Or use switch if appropriate
```

### Display File Operations

#### EXFMT (Execute Format)
```rpgle
EXFMT SCREEN1;
IF *IN03;  // F3 pressed
  // Exit logic
ENDIF;
```
↓
```java
// In Controller - expose REST endpoint
@PostMapping("/customer-inquiry")
public ResponseEntity<CustomerInquiryResponse> inquire(
    @RequestBody CustomerInquiryRequest request) {

    if (request.isF3Pressed()) {
        // Exit logic
        return ResponseEntity.ok(new CustomerInquiryResponse(true));
    }

    CustomerInquiryResponse response = service.processInquiry(request);
    return ResponseEntity.ok(response);
}
```

### Indicator Handling

```rpgle
IF *IN03;  // F3 = Exit
IF *IN12;  // F12 = Cancel
*IN50 = *ON;  // Error indicator
```
↓
```java
// In DTO or Request object
private boolean f3Pressed;
private boolean f12Pressed;

// In Service - handle as business logic
if (request.isF3Pressed()) {
    // Handle exit
}

// Error indicators become exceptions or response flags
throw new ValidationException("Error condition");
// Or
response.setErrorFlag(true);
response.setErrorMessage("Error condition");
```

### Subroutine Calls

```rpgle
EXSR ValidateCustomer;

BEGSR ValidateCustomer;
  // Validation logic
ENDSR;
```
↓
```java
private void validateCustomer(Customer customer) {
    // Validation logic
    if (!isValid(customer)) {
        throw new ValidationException("Invalid customer");
    }
}
```

### Error Handling

```rpgle
MONITOR;
  // Code that might error
ON-ERROR;
  // Error handling
ENDMON;
```
↓
```java
try {
    // Code that might error
} catch (Exception e) {
    // Error handling
    log.error("Error occurred", e);
    throw new ServiceException("Operation failed", e);
}
```

## Data Type Conversions

### Numeric Operations

```rpgle
Result = Num1 + Num2;
Result = %DEC(Value:11:2);
```
↓
```java
BigDecimal result = num1.add(num2);
BigDecimal result = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
```

### String Operations

```rpgle
String = %TRIM(Field);
String = %SUBST(Field:1:10);
Len = %LEN(Field);
```
↓
```java
String string = field.trim();
String string = field.substring(0, 10);
int len = field.length();
```

### Date Operations

```rpgle
CurrentDate = %DATE();
DateString = %CHAR(DateField);
```
↓
```java
LocalDate currentDate = LocalDate.now();
String dateString = dateField.toString();
```

## Common Utility Methods to Create

### Date Utilities
```java
public class DateUtils {
    public static LocalDate parseRpgDate(String rpgDate) {
        // Handle YYMMDD, MMDDYY, YYYYMMDD formats
    }

    public static String formatToRpg(LocalDate date) {
        // Convert to RPG format
    }
}
```

### Numeric Utilities
```java
public class NumericUtils {
    public static BigDecimal fromPackedDecimal(String packed, int scale) {
        // Convert packed decimal representation
    }
}
```
