#! /bin/bash

echo "Running tests..."

cd target
java -cp ".:../*" -ea tests/Test || exit 1
cd - > /dev/null

echo "Tests passed!"
