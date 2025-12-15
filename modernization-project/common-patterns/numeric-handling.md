# Numeric Type Handling

## Packed Decimal Conversion

RPGLE packed decimals must map to Java BigDecimal for precision.

### Declaration Pattern
```java
// RPGLE: D Amount 11P 2
@Column(name = "AMOUNT", precision = 11, scale = 2)
private BigDecimal amount;
```

### Arithmetic Operations
```java
// Addition
BigDecimal result = amount1.add(amount2);

// Subtraction
BigDecimal result = amount1.subtract(amount2);

// Multiplication
BigDecimal result = amount1.multiply(amount2);

// Division
BigDecimal result = amount1.divide(amount2, 2, RoundingMode.HALF_UP);
```

### Comparisons
```java
// RPGLE: IF Amount > 100.00
if (amount.compareTo(new BigDecimal("100.00")) > 0) {
    // ...
}

// Equal
if (amount.compareTo(threshold) == 0) {
    // ...
}
```

## Rounding
Always specify rounding mode:
```java
BigDecimal rounded = value.setScale(2, RoundingMode.HALF_UP);
```

## Zero Handling
```java
// RPGLE: IF Amount = 0
if (amount.compareTo(BigDecimal.ZERO) == 0) {
    // ...
}
```
