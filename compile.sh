#!/bin/bash

# Simple Java compilation and run script (without Maven)
# For systems where Maven is not available

echo "Compiling Good Day To Learn Java Application..."

# Create output directory
mkdir -p target/classes

# Compile all Java files
echo "Compiling Java source files..."
find src/main/java -name "*.java" -print0 | xargs -0 javac -d target/classes -cp target/classes

if [ $? -ne 0 ]; then
    echo "Error: Compilation failed!"
    exit 1
fi

# Copy resources
echo "Copying resources..."
cp -r src/main/resources/* target/classes/ 2>/dev/null || true

echo "Compilation completed successfully!"
echo ""
echo "To run the application:"
echo "  java -cp target/classes com.gooddaytolearn.Main"
echo ""
echo "Or use the run script:"
echo "  ./run.sh"
