#!/bin/bash

# Create JAR file without Maven

echo "Creating JAR file for Good Day To Learn..."

# Compile first if needed
if [ ! -d "target/classes" ]; then
    echo "Compiling project first..."
    ./compile.sh
fi

# Create manifest file
mkdir -p target
cat > target/MANIFEST.MF << EOF
Manifest-Version: 1.0
Main-Class: com.gooddaytolearn.Main
EOF

# Create JAR file
echo "Creating JAR file..."
cd target/classes
jar cfm ../GoodDayToLearn.jar ../MANIFEST.MF .
cd ../..

if [ $? -eq 0 ]; then
    echo "JAR file created successfully: target/GoodDayToLearn.jar"
    echo ""
    echo "To run the JAR file:"
    echo "  java -jar target/GoodDayToLearn.jar"
else
    echo "Error creating JAR file!"
    exit 1
fi
