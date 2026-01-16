# Development Plan for {PROGRAM}

**Created by**: Architect Agent  
**Date**: {DATE}  
**Status**: Ready for Development

---

## 1. Executive Summary

### Program Overview
- **Program Name**: {PROGRAM}
- **Original Purpose**: {description from analysis}
- **Business Functions**: {list key functions}

### Conversion Scope
- **Entities to create/extend**: {count}
- **Services to create/extend**: {count}
- **REST endpoints**: {count}
- **Estimated Complexity**: LOW / MEDIUM / HIGH

---

## 2. Existing Artifacts Analysis

### 2.1 Artifacts Found in /src/main/java/

| Type | Name | Location | Action | Notes |
|------|------|----------|--------|-------|
| Entity | | | EXTEND / REUSE / NONE | |
| Repository | | | EXTEND / REUSE / NONE | |
| Service | | | EXTEND / REUSE / NONE | |
| Controller | | | EXTEND / REUSE / NONE | |
| DTO | | | EXTEND / REUSE / NONE | |

### 2.2 Artifacts to Create New

| Type | Name | Justification |
|------|------|---------------|
| | | |

### 2.3 Decision Summary
- [ ] Extending existing artifacts: {list}
- [ ] Creating new artifacts: {list}
- [ ] Reusing existing artifacts as-is: {list}

---

## 3. Database Layer Design

### 3.1 Entity Specifications

#### Entity: {EntityName}
- **Table Name**: {TABLE_NAME}
- **Package**: com.smeup.erp.entities
- **Action**: CREATE NEW / EXTEND EXISTING
- **Source (if extending)**: `/src/main/java/com/smeup/erp/entities/{EntityName}.java`

##### Fields
| Field Name | DB Column | Java Type | JPA Annotations | Validation | Notes |
|------------|-----------|-----------|-----------------|------------|-------|
| | | | | | |

##### Primary Key
- **Field**: {keyField}
- **Type**: {type}
- **Strategy**: NATURAL / AUTO / SEQUENCE / UUID
- **Annotations**: 
```java
@Id
@Column(name = "{COLUMN}", precision = {P}, scale = {S})
```

##### Relationships
| Relationship Type | Target Entity | Join Details | Cascade | Fetch | Notes |
|-------------------|---------------|--------------|---------|-------|-------|
| @ManyToOne | | @JoinColumn(name = "") | | LAZY | |
| @OneToMany | | mappedBy = "" | | LAZY | |

##### Validation Rules
| Field | Annotations | Business Rule |
|-------|-------------|---------------|
| | @NotNull | Required field |
| | @Size(max = N) | Max length from DDS |

##### Constructors
- Default no-args constructor (required by JPA)
- Constructor with key field(s)
- Optional: Full constructor

##### Methods
- Getters/Setters for all fields
- equals() based on primary key
- hashCode() based on primary key
- toString() for debugging

---

### 3.2 Repository Specifications

#### Repository: {EntityName}Repository
- **Package**: com.smeup.erp.repositories
- **Extends**: JpaRepository<{EntityName}, {KeyType}>
- **Action**: CREATE NEW / EXTEND EXISTING
- **Source (if extending)**: `/src/main/java/com/smeup/erp/repositories/{EntityName}Repository.java`

##### Query Methods

| Method Signature | Purpose | RPGLE Operation | Notes |
|------------------|---------|-----------------|-------|
| `Optional<{Entity}> findBy{Field}({Type} value)` | Keyed read | CHAIN | |
| `List<{Entity}> findBy{Field}({Type} value)` | Read by key | READ with key | |
| `List<{Entity}> findBy{Field}GreaterThanEqualOrderBy{Field}({Type} value)` | Range read | SETLL + READ | |
| `boolean existsBy{Field}({Type} value)` | Existence check | %FOUND | |

##### Custom @Query Methods (if needed)

```java
// Method: {methodName}
// Purpose: {purpose}
// RPGLE Equivalent: {operation}
@Query("SELECT e FROM {Entity} e WHERE {conditions}")
List<{Entity}> {methodName}(@Param("param") Type param);
```

---

## 4. Service Layer Design

### 4.1 Service Specifications

#### Service: {ProgramName}Service
- **Package**: com.smeup.erp.services
- **Action**: CREATE NEW / EXTEND EXISTING
- **Source (if extending)**: `/src/main/java/com/smeup/erp/services/{ProgramName}Service.java`

##### Class Annotations
```java
@Service
@Slf4j // or private static final Logger log = LoggerFactory.getLogger(...)
```

##### Dependencies (Autowired)
| Type | Field Name | Purpose |
|------|------------|---------|
| {Entity}Repository | {entity}Repository | Data access |
| | | |

##### Public Methods

###### Method: {methodName}
```
Signature: public {ReturnType} {methodName}({Parameters})
Purpose: {description}
RPGLE Equivalent: {main logic / subroutine name}
Transaction: @Transactional (YES / NO / READ_ONLY)

Input Parameters:
  - {paramName}: {Type} - {description and validation}

Return Value:
  - Type: {Type}
  - Description: {what is returned}

Business Logic Steps:
  1. Validate input parameters
  2. {step}
  3. {step}
  4. Build and return response

Error Handling:
  - If {condition} → throw {ExceptionType}("{message}")
  - If {condition} → throw {ExceptionType}("{message}")

Logging:
  - log.info("Starting {operation} for {entity}: {}", key)
  - log.debug("{detail}")
  - log.error("Failed to {operation}", exception)
```

##### Private Helper Methods
| Method Signature | Purpose | RPGLE Subroutine |
|------------------|---------|------------------|
| `private {Type} {methodName}({Params})` | {purpose} | {SR_NAME} |

---

## 5. Controller Layer Design

### 5.1 Controller Specifications

#### Controller: {ProgramName}Controller
- **Package**: com.smeup.erp.controllers
- **Base Path**: `/api/{resource}`
- **Action**: CREATE NEW / EXTEND EXISTING
- **Source (if extending)**: `/src/main/java/com/smeup/erp/controllers/{ProgramName}Controller.java`

##### Class Annotations
```java
@RestController
@RequestMapping("/api/{resource}")
@CrossOrigin(origins = "*")
@Slf4j
```

##### Endpoints Summary

| HTTP | Path | Method | Request | Response | Status Codes | Purpose |
|------|------|--------|---------|----------|--------------|---------|
| GET | / | list() | - | List<DTO> | 200 | List all |
| GET | /{id} | getById() | PathVar | DTO | 200, 404 | Get one |
| POST | / | create() | Body | DTO | 201, 400 | Create |
| PUT | /{id} | update() | PathVar, Body | DTO | 200, 400, 404 | Update |
| DELETE | /{id} | delete() | PathVar | - | 204, 404 | Delete |

##### Endpoint Details

###### {HTTP Method} {full path}
```
Method Name: {methodName}
Purpose: {description}
RPGLE Display Equivalent: {EXFMT / screen interaction}

Request:
  - Path Variables: 
    - {name}: {type} - {description}
  - Query Parameters:
    - {name}: {type} (optional) - {description}
  - Request Body: {DTOName}
    - {field}: {type} - {description}

Response:
  - 200 OK: {ResponseDTO} - {when returned}
  - 201 Created: {ResponseDTO} - {when returned}
  - 400 Bad Request: ErrorResponse - {when returned}
  - 404 Not Found: ErrorResponse - {when returned}
  - 500 Internal Server Error: ErrorResponse - {when returned}

Implementation Notes:
  - {notes}
```

##### Path Conflict Verification

| Existing Path | New Path | Conflict? | Resolution |
|---------------|----------|-----------|------------|
| | | NO | |

---

## 6. DTO Design

### 6.1 Request DTOs

#### DTO: {ProgramName}RequestDTO
- **Package**: com.smeup.erp.dtos
- **Purpose**: Request body for create/update operations

| Field | Type | JSON Property | Validation | Required | Notes |
|-------|------|---------------|------------|----------|-------|
| | | @JsonProperty("") | | YES/NO | |

### 6.2 Response DTOs

#### DTO: {ProgramName}ResponseDTO
- **Package**: com.smeup.erp.dtos
- **Purpose**: Response body for all operations

| Field | Type | JSON Property | Notes |
|-------|------|---------------|-------|
| | | @JsonProperty("") | |
| success | boolean | "success" | Operation status |
| errorMessage | String | "errorMessage" | Error details if any |

### 6.3 DTO Mapping

| DTO Field | Entity Field | Transformation |
|-----------|--------------|----------------|
| | | None / {conversion} |

---

## 7. Exception Handling Design

### 7.1 Custom Exceptions Needed

| Exception Class | HTTP Status | When to Throw | Package |
|-----------------|-------------|---------------|---------|
| {Entity}NotFoundException | 404 | Entity not found by ID | com.smeup.erp.exceptions |
| ValidationException | 400 | Input validation fails | com.smeup.erp.exceptions |
| BusinessRuleException | 422 | Business rule violation | com.smeup.erp.exceptions |

### 7.2 Exception Handler
- **Global Handler Exists**: YES / NO
- If NO, specify creation requirements

---

## 8. Testing Strategy

### 8.1 Repository Tests

| Test Class | Location | Test Cases |
|------------|----------|------------|
| {Entity}RepositoryTest | src/test/java/.../repositories/ | |

**Test Cases**:
- `testFindBy{Field}_WhenExists_ReturnsEntity()`
- `testFindBy{Field}_WhenNotExists_ReturnsEmpty()`
- `testSave_ValidEntity_SavesSuccessfully()`

### 8.2 Service Tests

| Test Class | Location | Test Cases |
|------------|----------|------------|
| {Service}Test | src/test/java/.../services/ | |

**Test Cases**:
- `test{Method}_ValidInput_ReturnsExpected()`
- `test{Method}_InvalidInput_ThrowsException()`
- `test{Method}_NotFound_ThrowsNotFoundException()`

### 8.3 Controller Tests

| Test Class | Location | Test Cases |
|------------|----------|------------|
| {Controller}Test | src/test/java/.../controllers/ | |

**Test Cases**:
- `test{Endpoint}_ValidRequest_Returns200()`
- `test{Endpoint}_InvalidRequest_Returns400()`
- `test{Endpoint}_NotFound_Returns404()`

---

## 9. Implementation Checklist for Developer Agent

### Pre-Implementation
- [ ] Review this development plan completely
- [ ] Identify artifacts to copy from /src/main/java/
- [ ] Set up workspace directory structure

### Phase 1: Database Layer
- [ ] Create/copy entity file(s)
- [ ] Implement all entity fields with correct annotations
- [ ] Implement entity relationships
- [ ] Add validation annotations
- [ ] Implement equals(), hashCode(), toString()
- [ ] Create/copy repository file(s)
- [ ] Implement all query methods
- [ ] Create repository test file(s)
- [ ] Write repository test cases

### Phase 2: Service Layer
- [ ] Create/copy service file(s)
- [ ] Inject repository dependencies
- [ ] Implement public methods
- [ ] Implement private helper methods
- [ ] Add logging statements
- [ ] Add @Transactional where specified
- [ ] Implement error handling
- [ ] Create service test file(s)
- [ ] Write service test cases

### Phase 3: Controller Layer
- [ ] Create/copy controller file(s)
- [ ] Inject service dependencies
- [ ] Implement all endpoints
- [ ] Verify no path conflicts
- [ ] Create DTO file(s)
- [ ] Implement DTO fields and annotations
- [ ] Create controller test file(s)
- [ ] Write controller test cases

### Phase 4: Exception Handling
- [ ] Create custom exception classes (if needed)
- [ ] Verify exception handler coverage

### Phase 5: Final Verification
- [ ] All imports are correct
- [ ] Package declarations match directory paths
- [ ] All tests pass
- [ ] No duplicate artifacts created
- [ ] Documentation updated

---

## 10. File Structure to Create

```
/work-in-progress/{PROGRAM}/03-development/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── smeup/
│   │               └── erp/
│   │                   ├── entities/
│   │                   │   └── {Entity}.java
│   │                   ├── repositories/
│   │                   │   └── {Entity}Repository.java
│   │                   ├── services/
│   │                   │   └── {Program}Service.java
│   │                   ├── controllers/
│   │                   │   └── {Program}Controller.java
│   │                   ├── dtos/
│   │                   │   ├── {Program}RequestDTO.java
│   │                   │   └── {Program}ResponseDTO.java
│   │                   └── exceptions/
│   │                       └── {Exception}.java
│   └── test/
│       └── java/
│           └── com/
│               └── smeup/
│                   └── erp/
│                       ├── repositories/
│                       │   └── {Entity}RepositoryTest.java
│                       └── services/
│                           └── {Program}ServiceTest.java
└── development-notes.md
```

---

## 11. Dependencies and Configuration

### Additional Maven Dependencies (if needed)
```xml
<!-- Add only if not already in pom.xml -->
```

### Application Properties (if needed)
```yaml
# Add only if new configuration is needed
```

---

## 12. Open Questions / Decisions Needed

| # | Question | Context | Recommendation | Priority |
|---|----------|---------|----------------|----------|
| | | | | HIGH/MEDIUM/LOW |

---

## 13. Notes for Developer Agent

### Critical Reminders
1. Always follow Maven directory structure
2. Never modify files in /src directly
3. Copy existing artifacts to workspace before extending
4. Verify no path conflicts before creating controllers
5. Run tests after each phase

### Reference Documents
- Type Mappings: `/agents/20-architect-agent/type-mappings.md`
- Conversion Patterns: `/agents/20-architect-agent/conversion-patterns.md`
- Maven Structure: `/common-patterns/maven-project-structure.md`
