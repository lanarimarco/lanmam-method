package {PACKAGE_NAME}.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing {BUSINESS_CONCEPT}
 * Mapped to DB2 table: {TABLE_NAME}
 * Original RPGLE file: {FILE_NAME}
 */
@Entity
@Table(name = "{TABLE_NAME}")
public class {EntityName} {

    // ==================== Primary Key ====================
    
    @Id
    @Column(name = "{KEY_COLUMN}", precision = {P}, scale = {S})
    private BigDecimal {keyField};

    // ==================== Fields ====================
    
    @Column(name = "{COLUMN_NAME}", length = {LENGTH})
    @Size(max = {LENGTH})
    private String {stringField};

    @Column(name = "{COLUMN_NAME}", precision = {P}, scale = {S})
    private BigDecimal {numericField};

    @Column(name = "{COLUMN_NAME}")
    private LocalDate {dateField};

    @Column(name = "{COLUMN_NAME}")
    @NotNull
    private String {requiredField};

    // ==================== Relationships ====================
    
    // Example ManyToOne relationship
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "{FK_COLUMN}")
    // private {RelatedEntity} {relatedEntity};

    // Example OneToMany relationship
    // @OneToMany(mappedBy = "{fieldName}", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<{RelatedEntity}> {relatedEntities} = new ArrayList<>();

    // ==================== Constructors ====================
    
    /**
     * Default constructor required by JPA
     */
    public {EntityName}() {
    }

    /**
     * Constructor with primary key
     */
    public {EntityName}(BigDecimal {keyField}) {
        this.{keyField} = {keyField};
    }

    // ==================== Getters and Setters ====================
    
    public BigDecimal get{KeyField}() {
        return {keyField};
    }

    public void set{KeyField}(BigDecimal {keyField}) {
        this.{keyField} = {keyField};
    }

    public String get{StringField}() {
        return {stringField};
    }

    public void set{StringField}(String {stringField}) {
        this.{stringField} = {stringField};
    }

    // ... additional getters and setters for all fields

    // ==================== equals, hashCode, toString ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        {EntityName} that = ({EntityName}) o;
        return Objects.equals({keyField}, that.{keyField});
    }

    @Override
    public int hashCode() {
        return Objects.hash({keyField});
    }

    @Override
    public String toString() {
        return "{EntityName}{" +
                "{keyField}=" + {keyField} +
                ", {field}='" + {field} + '\'' +
                '}';
    }
}
