# Display Files DDS Directory

Place your DDS source for display files (5250 green-screen interfaces) here.

## Purpose
Display files in AS/400/IBM i define the user interface for interactive programs. They specify:
- Screen layouts and field positions
- Input/output fields and their attributes
- Function key definitions
- Subfile specifications for list displays
- Color, highlighting, and display attributes

## File Naming
- Typically use `.dds` or `.dspf` extensions
- Example: `CUSTDSP.dds` (customer display file)
- Often named with suffix DSP, DSPF, or D

## Conversion Notes
During conversion:
- Display files are converted to React components with modern UI
- Record formats become React pages or components
- Subfiles become data tables or lists
- Function keys map to buttons or keyboard shortcuts
- Field validations become form validation logic
- The UI agent (Phase 4) handles this conversion using the DDS specifications

## Example Structure
A display file typically contains:
- File-level keywords (size, indicators, etc.)
- Record formats (screens/panels)
- Field definitions with row/column positions
- Keywords for colors, attributes, validations
- Subfile definitions for scrollable lists
