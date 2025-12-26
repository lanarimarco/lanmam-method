#!/bin/bash
# Cleanup test program files and work-in-progress

PROGRAM=$1
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

if [ -z "$PROGRAM" ]; then
    echo "Usage: ./cleanup-test.sh PROGRAM_NAME"
    echo "Example: ./cleanup-test.sh CUST001"
    exit 1
fi

echo "========================================="
echo "Cleaning up test program: $PROGRAM"
echo "========================================="
echo ""

# Remove from work-in-progress
if [ -d "$PROJECT_ROOT/work-in-progress/$PROGRAM" ]; then
    echo "Removing work-in-progress/$PROGRAM..."
    rm -rf "$PROJECT_ROOT/work-in-progress/$PROGRAM"
    echo "✓ Work-in-progress removed"
else
    echo "⊘ No work-in-progress found for $PROGRAM"
fi

# Remove from source folders
echo ""
echo "Removing source files..."

if [ -f "$PROJECT_ROOT/source-rpgle/programs/$PROGRAM.rpgle" ]; then
    rm "$PROJECT_ROOT/source-rpgle/programs/$PROGRAM.rpgle"
    echo "✓ Removed source-rpgle/programs/$PROGRAM.rpgle"
fi

# Note: We don't know which DDS files belong to this program
# User should specify or we could track it
echo ""
echo "Note: DDS files not automatically removed."
echo "If needed, manually remove from:"
echo "  - source-rpgle/dds/physical-files/"
echo "  - source-rpgle/dds/display-files/"
echo "  - source-rpgle/dds/logical-files/"

echo ""
echo "========================================="
echo "Cleanup Complete!"
echo "========================================="
