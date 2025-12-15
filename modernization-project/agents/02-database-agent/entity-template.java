package com.company.modernization.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing {BUSINESS_CONCEPT}
 * Mapped to DB2 table: {TABLE_NAME}
 * Original RPGLE file: {FILE_NAME}
 */
@Entity
@Table(name = "{TABLE_NAME}")
public class {EntityName} {

    @Id
    @Column(name = "{KEY_FIELD}", precision = {P}, scale = {S})
    private BigDecimal {keyField};

    @Column(name = "{FIELD_NAME}", length = {LENGTH})
    private String {fieldName};

    @Column(name = "{FIELD_NAME}", precision = {P}, scale = {S})
    private BigDecimal {fieldName};

    @Column(name = "{DATE_FIELD}")
    private LocalDate {dateField};

    // Constructors
    public {EntityName}() {
    }

    public {EntityName}(BigDecimal {keyField}) {
        this.{keyField} = {keyField};
    }

    // Getters and Setters
    public BigDecimal get{KeyField}() {
        return {keyField};
    }

    public void set{KeyField}(BigDecimal {keyField}) {
        this.{keyField} = {keyField};
    }

    // ... other getters and setters

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof {EntityName})) return false;
        {EntityName} that = ({EntityName}) o;
        return {keyField}.equals(that.{keyField});
    }

    @Override
    public int hashCode() {
        return {keyField}.hashCode();
    }

    @Override
    public String toString() {
        return "{EntityName}{" +
                "{keyField}=" + {keyField} +
                ", {fieldName}='" + {fieldName} + '\'' +
                '}';
    }
}
