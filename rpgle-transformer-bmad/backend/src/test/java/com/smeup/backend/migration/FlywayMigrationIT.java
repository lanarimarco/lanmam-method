package com.smeup.backend.migration;

import com.smeup.backend.AbstractIntegrationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Flyway migrations.
 *
 * <p>These tests verify that:
 * <ol>
 *   <li>Flyway migrations execute successfully with real PostgreSQL</li>
 *   <li>CUSTMAST table is created with correct structure</li>
 *   <li>Column types match DDS specifications</li>
 * </ol>
 *
 * <p>Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FlywayMigrationIT extends AbstractIntegrationTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void flywayMigrationShouldExecuteSuccessfully() {
    // Verify Flyway ran by checking flyway_schema_history table
    String sql = """
        SELECT version, description, success
        FROM flyway_schema_history
        WHERE version = '1'
        """;

    Map<String, Object> migration = jdbcTemplate.queryForMap(sql);
    assertThat(migration.get("version")).isEqualTo("1");
    assertThat(migration.get("description")).isEqualTo("create custmast");
    assertThat(migration.get("success")).isEqualTo(true);
  }

  @Test
  void custmastTableShouldExist() {
    // Query information_schema to verify table exists
    String sql = """
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'custmast'
        """;

    List<String> tables = jdbcTemplate.queryForList(sql, String.class);
    assertThat(tables).contains("custmast");
  }

  @Test
  void custmastTableShouldHaveCorrectColumns() {
    // Query column information
    String sql = """
        SELECT column_name, data_type, character_maximum_length, numeric_precision, numeric_scale, is_nullable
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'custmast'
        ORDER BY ordinal_position
        """;

    List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql);

    // Verify all 10 DDS fields are present
    assertThat(columns).hasSize(10);

    // Verify each column matches DDS specification
    assertColumn(columns, "custno", "bigint", null, null, null, "NO");
    assertColumn(columns, "custname", "character varying", 30, null, null, "NO");
    assertColumn(columns, "addr1", "character varying", 30, null, null, "YES");
    assertColumn(columns, "city", "character varying", 20, null, null, "YES");
    assertColumn(columns, "state", "character", 2, null, null, "YES");
    assertColumn(columns, "zip", "integer", null, null, null, "YES");
    assertColumn(columns, "phone", "character varying", 12, null, null, "YES");
    assertColumn(columns, "balance", "numeric", null, 9, 2, "YES");
    assertColumn(columns, "creditlim", "numeric", null, 9, 2, "YES");
    assertColumn(columns, "lastorder", "integer", null, null, null, "YES");
  }

  @Test
  void custmastTableShouldHavePrimaryKeyOnCustno() {
    // Query for primary key constraint
    String sql = """
        SELECT kcu.column_name
        FROM information_schema.table_constraints tc
        JOIN information_schema.key_column_usage kcu
          ON tc.constraint_name = kcu.constraint_name
          AND tc.table_schema = kcu.table_schema
        WHERE tc.constraint_type = 'PRIMARY KEY'
          AND tc.table_schema = 'public'
          AND tc.table_name = 'custmast'
        """;

    List<String> pkColumns = jdbcTemplate.queryForList(sql, String.class);
    assertThat(pkColumns).containsExactly("custno");
  }

  @Test
  void custmastTableShouldHaveIndexOnCustname() {
    // Query for index on CUSTNAME
    String sql = """
        SELECT indexname
        FROM pg_indexes
        WHERE schemaname = 'public'
          AND tablename = 'custmast'
          AND indexname = 'idx_custmast_name'
        """;

    List<String> indexes = jdbcTemplate.queryForList(sql, String.class);
    assertThat(indexes).contains("idx_custmast_name");
  }

  @Test
  void shouldBeAbleToInsertAndQueryCustomerData() {
    // Insert test customer data
    String insertSql = """
        INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    jdbcTemplate.update(
        insertSql,
        12345L,
        "Test Customer",
        "123 Main Street",
        "Springfield",
        "IL",
        62701,
        "555-123-4567",
        new BigDecimal("1234.56"),
        new BigDecimal("5000.00"),
        20251230
    );

    // Query the inserted data
    String selectSql = "SELECT * FROM CUSTMAST WHERE CUSTNO = ?";
    Map<String, Object> customer = jdbcTemplate.queryForMap(selectSql, 12345L);

    assertThat(customer.get("custno")).isEqualTo(12345L);
    assertThat(customer.get("custname")).isEqualTo("Test Customer");
    assertThat(customer.get("addr1")).isEqualTo("123 Main Street");
    assertThat(customer.get("city")).isEqualTo("Springfield");
    assertThat(customer.get("state")).isEqualTo("IL");
    assertThat(customer.get("zip")).isEqualTo(62701);
    assertThat(customer.get("phone")).isEqualTo("555-123-4567");
    assertThat(((BigDecimal) customer.get("balance")).compareTo(new BigDecimal("1234.56")))
        .isZero();
    assertThat(((BigDecimal) customer.get("creditlim")).compareTo(new BigDecimal("5000.00")))
        .isZero();
    assertThat(customer.get("lastorder")).isEqualTo(20251230);

    // Clean up
    jdbcTemplate.update("DELETE FROM CUSTMAST WHERE CUSTNO = ?", 12345L);
  }

  private void assertColumn(
      List<Map<String, Object>> columns,
      String columnName,
      String expectedType,
      Integer expectedCharLength,
      Integer expectedPrecision,
      Integer expectedScale,
      String expectedNullable) {

    Map<String, Object> column = columns.stream()
        .filter(c -> columnName.equals(c.get("column_name")))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Column not found: " + columnName));

    assertThat(column.get("data_type"))
        .as("Data type for column %s", columnName)
        .isEqualTo(expectedType);

    if (expectedCharLength != null) {
      assertThat(column.get("character_maximum_length"))
          .as("Character length for column %s", columnName)
          .isEqualTo(expectedCharLength);
    }

    if (expectedPrecision != null) {
      assertThat(column.get("numeric_precision"))
          .as("Numeric precision for column %s", columnName)
          .isEqualTo(expectedPrecision);
    }

    if (expectedScale != null) {
      assertThat(column.get("numeric_scale"))
          .as("Numeric scale for column %s", columnName)
          .isEqualTo(expectedScale);
    }

    assertThat(column.get("is_nullable"))
        .as("Nullable for column %s", columnName)
        .isEqualTo(expectedNullable);
  }
}
