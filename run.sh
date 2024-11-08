#! /bin/bash


setup_dependencies() {
    echo "Checking dependencies..."
    [[ ! -f ./target/sqlite-jdbc-3.46.0.jar ]] && curl --output sqlite-jdbc-3.46.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.0.0/sqlite-jdbc-3.46.0.0.jar -SsL
    [[ ! -f ./target/slf4j-api-2.0.13.jar ]] && curl --output slf4j-api-2.0.13.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar -SsL
    echo "Dependencies checked!"
}


build() {
    echo "Building the project..."
    . ./build.sh
    echo "Project built successfully!"
}

test() {
    . ./test.sh || exit 1
}

package() {
    echo "Packaging the project..."
    PROEJCTNAME=$(basename $(pwd))
    cd target
    jar cfm "$PROEJCTNAME.jar" MANIFEST.MF -C . .
    cd ..
    mv target/"$PROEJCTNAME.jar" .
    echo "Project packaged successfully!"
}

main() {
    ARG=$1
    setup_dependencies
    build test
    test || exit 1
    # java -cp "target:./*" Main
    package
}

main
