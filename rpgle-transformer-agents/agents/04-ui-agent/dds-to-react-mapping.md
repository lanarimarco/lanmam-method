# DDS Display File to React Mapping

## Field Type Mappings

| DDS Field Type | React Component | Notes |
|----------------|-----------------|-------|
| Input field | `<input type="text">` | Standard text input |
| Output field | `<span>` or `<div>` | Read-only display |
| Both (I/O) | `<input>` with conditional readonly | Can toggle |
| Hidden field | Hidden state variable | Don't render |

## DDS Keywords to React Props

### EDTCDE (Edit Code)
| DDS EDTCDE | React Formatting |
|------------|------------------|
| Y (Date) | Format using date-fns or similar |
| 1-4,A-D (Numeric) | Format with commas, decimals |
| Z (Zero suppress) | Display empty if zero |

### EDTWRD (Edit Word)
- Custom formatting based on pattern
- Use input masking libraries (e.g., react-input-mask)

### DSPATR (Display Attributes)
| DDS DSPATR | React/CSS Equivalent |
|------------|---------------------|
| HI (High intensity) | `font-weight: bold` |
| RI (Reverse image) | Inverse colors |
| UL (Underline) | `text-decoration: underline` |
| BL (Blink) | CSS animation (use sparingly!) |
| PC (Position cursor) | `autoFocus` prop |

### COLOR
| DDS COLOR | CSS Color |
|-----------|-----------|
| BLU | `color: blue` |
| GRN | `color: green` |
| RED | `color: red` |
| WHT | `color: white` or black (depends on theme) |

## Function Keys

### Standard Mappings
| RPG Function Key | React Implementation |
|------------------|---------------------|
| F3 (Exit) | Cancel/Close button |
| F5 (Refresh) | Refresh button or auto-refresh |
| F6 (Add) | Create/Add button |
| F12 (Cancel) | Cancel/Back button |
| Enter | Submit button or form submission |
| PageUp/PageDown | Pagination controls |

### Implementation Pattern
```typescript
const handleKeyPress = (event: KeyboardEvent) => {
    if (event.key === 'F3' || event.key === 'Escape') {
        handleExit();
    } else if (event.key === 'F12') {
        handleCancel();
    } else if (event.key === 'Enter') {
        handleSubmit();
    }
};

useEffect(() => {
    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
}, []);
```

## Screen Flow Patterns

### Single Screen (Simple Inquiry)
```
User Input → Validation → API Call → Display Results
```

### Multi-Screen Workflow
Use React Router or state management:
```typescript
enum Screen {
    SELECTION,
    DETAIL,
    CONFIRMATION
}

const [currentScreen, setCurrentScreen] = useState<Screen>(Screen.SELECTION);
```

## Layout Considerations

### Convert Grid Layout
5250 is 24 rows × 80 columns
- Use CSS Grid or Flexbox
- Make responsive for modern screens
- Group related fields visually

### Example Conversion
```
DDS (5250):
Row 5, Col 10: "Customer Number:"
Row 5, Col 30: Input field (7 chars)

React:
<div className="form-row">
    <label>Customer Number:</label>
    <input type="text" maxLength={7} />
</div>
```

## Modern UX Enhancements

While preserving functionality:
1. **Add visual feedback**: Loading spinners, success messages
2. **Improve validation**: Inline validation, helpful error messages
3. **Responsive design**: Works on mobile, tablet, desktop
4. **Accessibility**: Proper labels, ARIA attributes, keyboard navigation
5. **Auto-complete**: For frequently used fields
6. **Smart defaults**: Remember user preferences
