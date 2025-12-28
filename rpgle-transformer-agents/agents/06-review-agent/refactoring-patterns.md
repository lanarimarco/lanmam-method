# Refactoring Patterns

Common refactoring patterns to apply during code review.

## 1. Extract Method

**When to Use**: Long methods (>30 lines) or complex logic blocks

**Before**:
```java
public CustomerDTO processCustomer(Long id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new CustomerNotFoundException(id));

    // Complex validation logic
    if (customer.getName() == null || customer.getName().isEmpty()) {
        throw new ValidationException("Name is required");
    }
    if (customer.getEmail() != null && !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        throw new ValidationException("Invalid email format");
    }

    // Mapping logic
    CustomerDTO dto = new CustomerDTO();
    dto.setId(customer.getId());
    dto.setName(customer.getName());
    dto.setEmail(customer.getEmail());
    dto.setPhoneNumber(customer.getPhoneNumber());

    return dto;
}
```

**After**:
```java
public CustomerDTO processCustomer(Long id) {
    Customer customer = findCustomerOrThrow(id);
    validateCustomer(customer);
    return mapToDTO(customer);
}

private Customer findCustomerOrThrow(Long id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new CustomerNotFoundException(id));
}

private void validateCustomer(Customer customer) {
    if (customer.getName() == null || customer.getName().isEmpty()) {
        throw new ValidationException("Name is required");
    }
    if (customer.getEmail() != null && !isValidEmail(customer.getEmail())) {
        throw new ValidationException("Invalid email format");
    }
}

private boolean isValidEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
}

private CustomerDTO mapToDTO(Customer customer) {
    CustomerDTO dto = new CustomerDTO();
    dto.setId(customer.getId());
    dto.setName(customer.getName());
    dto.setEmail(customer.getEmail());
    dto.setPhoneNumber(customer.getPhoneNumber());
    return dto;
}
```

## 2. Replace Magic Numbers with Constants

**When to Use**: Numeric/string literals used multiple times or with business meaning

**Before**:
```java
if (customer.getStatus() == 1) {
    // active customer logic
}

if (order.getQuantity() > 100) {
    applyBulkDiscount(0.15);
}
```

**After**:
```java
private static final int STATUS_ACTIVE = 1;
private static final int BULK_ORDER_THRESHOLD = 100;
private static final double BULK_DISCOUNT_RATE = 0.15;

if (customer.getStatus() == STATUS_ACTIVE) {
    // active customer logic
}

if (order.getQuantity() > BULK_ORDER_THRESHOLD) {
    applyBulkDiscount(BULK_DISCOUNT_RATE);
}
```

## 3. Replace Conditional with Polymorphism

**When to Use**: Multiple if/else or switch statements based on type

**Before**:
```java
public BigDecimal calculatePrice(Product product) {
    if (product.getType().equals("STANDARD")) {
        return product.getBasePrice();
    } else if (product.getType().equals("PREMIUM")) {
        return product.getBasePrice().multiply(new BigDecimal("1.5"));
    } else if (product.getType().equals("DISCOUNT")) {
        return product.getBasePrice().multiply(new BigDecimal("0.8"));
    }
    return BigDecimal.ZERO;
}
```

**After**:
```java
// Interface
public interface PricingStrategy {
    BigDecimal calculatePrice(BigDecimal basePrice);
}

// Implementations
public class StandardPricing implements PricingStrategy {
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        return basePrice;
    }
}

public class PremiumPricing implements PricingStrategy {
    public BigDecimal calculatePrice(BigDecimal basePrice) {
        return basePrice.multiply(new BigDecimal("1.5"));
    }
}

// Usage
public BigDecimal calculatePrice(Product product) {
    return product.getPricingStrategy().calculatePrice(product.getBasePrice());
}
```

## 4. Introduce Parameter Object

**When to Use**: Methods with many parameters (>4)

**Before**:
```java
public void createOrder(Long customerId, String customerName, String email,
                       String address, String city, String state, String zip,
                       List<OrderItem> items, BigDecimal total) {
    // ...
}
```

**After**:
```java
public class OrderRequest {
    private CustomerInfo customer;
    private ShippingAddress address;
    private List<OrderItem> items;
    private BigDecimal total;
    // getters/setters
}

public void createOrder(OrderRequest request) {
    // ...
}
```

## 5. Replace Error Code with Exception

**When to Use**: Methods returning error codes or nulls to indicate errors

**Before**:
```java
public Customer findCustomer(Long id) {
    Customer customer = customerRepository.findById(id).orElse(null);
    if (customer == null) {
        return null; // Error: not found
    }
    return customer;
}

// Caller must check
Customer customer = service.findCustomer(id);
if (customer == null) {
    // handle error
}
```

**After**:
```java
public Customer findCustomer(Long id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new CustomerNotFoundException(id));
}

// Caller can use directly or catch exception
try {
    Customer customer = service.findCustomer(id);
    // use customer
} catch (CustomerNotFoundException e) {
    // handle error
}
```

## 6. Extract Interface

**When to Use**: To decouple implementation from usage, enable testing

**Before**:
```java
@Service
public class CustomerService {
    public Customer findById(Long id) { ... }
    public void save(Customer customer) { ... }
}

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService; // Coupled to implementation
}
```

**After**:
```java
public interface ICustomerService {
    Customer findById(Long id);
    void save(Customer customer);
}

@Service
public class CustomerService implements ICustomerService {
    public Customer findById(Long id) { ... }
    public void save(Customer customer) { ... }
}

@RestController
public class CustomerController {
    @Autowired
    private ICustomerService customerService; // Depends on interface
}
```

## 7. Consolidate Duplicate Code

**When to Use**: Same code appears in multiple places

**Before**:
```java
// In CustomerService
public CustomerDTO getCustomer(Long id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new CustomerNotFoundException(id));
    return mapToDTO(customer);
}

// In OrderService
public CustomerDTO getCustomerForOrder(Long customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException(customerId));
    return mapToDTO(customer);
}
```

**After**:
```java
// Extract to shared service or utility
@Service
public class CustomerService {
    public CustomerDTO findCustomerDTO(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
        return mapToDTO(customer);
    }
}

// Both services use shared method
@Autowired
private CustomerService customerService;

public CustomerDTO getCustomer(Long id) {
    return customerService.findCustomerDTO(id);
}
```

## 8. Replace Nested Conditionals with Guard Clauses

**When to Use**: Deep nesting makes code hard to read

**Before**:
```java
public void processOrder(Order order) {
    if (order != null) {
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            if (order.getCustomer() != null) {
                if (order.getCustomer().isActive()) {
                    // actual processing logic
                }
            }
        }
    }
}
```

**After**:
```java
public void processOrder(Order order) {
    if (order == null) return;
    if (order.getItems() == null || order.getItems().isEmpty()) return;
    if (order.getCustomer() == null) return;
    if (!order.getCustomer().isActive()) return;

    // actual processing logic at same indentation level
}
```

## 9. Simplify Boolean Expressions

**When to Use**: Complex boolean logic

**Before**:
```java
if (customer.getStatus() == STATUS_ACTIVE && customer.getBalance() > 0 ||
    customer.getStatus() == STATUS_PREMIUM) {
    // ...
}

public boolean isEligible(Customer customer) {
    if (customer.isActive()) {
        return true;
    } else {
        return false;
    }
}
```

**After**:
```java
boolean isActiveWithBalance = customer.getStatus() == STATUS_ACTIVE && customer.getBalance() > 0;
boolean isPremium = customer.getStatus() == STATUS_PREMIUM;

if (isActiveWithBalance || isPremium) {
    // ...
}

public boolean isEligible(Customer customer) {
    return customer.isActive();
}
```

## 10. Use Streams for Collection Processing (Java)

**When to Use**: Complex iteration logic

**Before**:
```java
List<CustomerDTO> activePremiumCustomers = new ArrayList<>();
for (Customer customer : customers) {
    if (customer.getStatus() == STATUS_ACTIVE && customer.isPremium()) {
        activePremiumCustomers.add(mapToDTO(customer));
    }
}
```

**After**:
```java
List<CustomerDTO> activePremiumCustomers = customers.stream()
    .filter(c -> c.getStatus() == STATUS_ACTIVE)
    .filter(Customer::isPremium)
    .map(this::mapToDTO)
    .collect(Collectors.toList());
```

## React/TypeScript Refactoring Patterns

### 11. Extract Custom Hook

**Before**:
```typescript
function CustomerList() {
    const [customers, setCustomers] = useState<Customer[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        customerService.getAll()
            .then(setCustomers)
            .catch(e => setError(e.message))
            .finally(() => setLoading(false));
    }, []);

    // render logic
}
```

**After**:
```typescript
// Custom hook
function useCustomers() {
    const [customers, setCustomers] = useState<Customer[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        customerService.getAll()
            .then(setCustomers)
            .catch(e => setError(e.message))
            .finally(() => setLoading(false));
    }, []);

    return { customers, loading, error };
}

// Component
function CustomerList() {
    const { customers, loading, error } = useCustomers();
    // render logic
}
```

### 12. Extract Component

**When to Use**: JSX becoming too complex

**Before**:
```typescript
return (
    <div>
        {customers.map(customer => (
            <div key={customer.id} className="customer-card">
                <h3>{customer.name}</h3>
                <p>{customer.email}</p>
                <p>{customer.phone}</p>
                <button onClick={() => handleEdit(customer)}>Edit</button>
                <button onClick={() => handleDelete(customer)}>Delete</button>
            </div>
        ))}
    </div>
);
```

**After**:
```typescript
function CustomerCard({ customer, onEdit, onDelete }: CustomerCardProps) {
    return (
        <div className="customer-card">
            <h3>{customer.name}</h3>
            <p>{customer.email}</p>
            <p>{customer.phone}</p>
            <button onClick={() => onEdit(customer)}>Edit</button>
            <button onClick={() => onDelete(customer)}>Delete</button>
        </div>
    );
}

return (
    <div>
        {customers.map(customer => (
            <CustomerCard
                key={customer.id}
                customer={customer}
                onEdit={handleEdit}
                onDelete={handleDelete}
            />
        ))}
    </div>
);
```

## General Refactoring Guidelines

1. **One refactoring at a time** - Don't mix multiple refactorings
2. **Test after each change** - Ensure tests still pass
3. **Commit frequently** - Small, focused commits
4. **Document reasons** - Explain why in refactoring-log.md
5. **Maintain behavior** - Refactoring should not change functionality
6. **Improve readability** - Code should be easier to understand after refactoring
