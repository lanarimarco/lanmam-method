#!/bin/bash
# Test the conversion workflow with sample program CUST001

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "========================================="
echo "Testing Conversion Workflow with CUST001"
echo "========================================="
echo ""

# Step 1: Copy test files to source folders
echo "Step 1: Copying test files to source folders..."
cp "$SCRIPT_DIR/programs/CUST001.rpgle" "$PROJECT_ROOT/source-rpgle/programs/"
cp "$SCRIPT_DIR/dds/physical-files/CUSTMAST.dds" "$PROJECT_ROOT/source-rpgle/dds/physical-files/"
cp "$SCRIPT_DIR/dds/display-files/CUSTDSP.dds" "$PROJECT_ROOT/source-rpgle/dds/display-files/"
echo "✓ Test files copied"
echo ""

# Step 2: Run the pipeline
echo "Step 2: Running full pipeline setup..."
cd "$PROJECT_ROOT/scripts"
./run-full-pipeline.sh CUST001

echo ""
echo "========================================="
echo "Test Setup Complete!"
echo "========================================="
echo ""
echo "Next Steps:"
echo ""
echo "1. Review the generated prompts:"
echo "   ls -la ../work-in-progress/CUST001/"
echo ""
echo "2. Test with single session (master prompt):"
echo "   cat ../work-in-progress/CUST001/MASTER-PIPELINE-PROMPT.md | pbcopy"
echo "   # Then paste into your LLM"
echo ""
echo "3. OR test phase-by-phase:"
echo "   cat ../work-in-progress/CUST001/01-analysis/PROMPT.md | pbcopy"
echo "   # Complete each phase, then move to next"
echo ""
echo "4. Validate results using checklist in:"
echo "   ../test-programs/README.md"
echo ""
echo "5. Clean up when done:"
echo "   cd ../test-programs"
echo "   ./cleanup-test.sh CUST001"
echo ""
