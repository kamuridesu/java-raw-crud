#! /bin/bash

PROJECTNAME=$(basename $(pwd))

setup_dependencies() {
    [[ "$1" == "true" ]] && return 0
    mkdir -p target
    DEPENDENCIES=$(cat dependencies.list)
    JAR_NAMES=""
    for DEP in $DEPENDENCIES; do
        NAME=$(echo $DEP | cut -d'<' -f1)
        URL=$(echo $DEP | cut -d'<' -f2)
        JAR_NAMES="$JAR_NAMES $NAME"
        if [[ ! -f $NAME ]]; then
            echo "[INFO] Downloading $NAME from $URL..."
            curl --output $NAME $URL -SsL
        fi
    done

    cp -r MANIFEST.MF.tmpl target/MANIFEST.MF
    if [[ "$JAR_NAMES" == "" ]]; then
        echo "[WARN] No dependencies found!"
        return 0
    fi

    echo "Class-Path:$JAR_NAMES" >> target/MANIFEST.MF

    echo "[INFO] Dependencies checked!"
}

build() {
    [[ "$1" == "true" ]] && return 0
    echo "[INFO] Building the project..."
    [[ "$2" == "true" ]] && javac -d . tests/Test.java
    javac -d . -sourcepath . Main.java
    mkdir -p target
    mv Main.class target 2> /dev/null
    for folder in $(ls -d */); do
        grep ".git" <<< $folder > /dev/null && continue
        grep "target" <<< $folder > /dev/null && continue
        mkdir -p target/$folder
        mv $folder/*.class target/$folder 2> /dev/null
    done
    echo "[INFO] Project built successfully!"
}

test() {
    [[ "$1" == "false" ]] && return 0
    echo "[INFO] Running tests..."

    cd target
    java -cp ".:../*" -ea tests/Test || exit 1
    cd - > /dev/null

    echo "[INFO] Tests passed!"
}

package() {
    [[ "$1" == "false" ]] && return 0
    echo "[INFO] Packaging the project..."
    if [[ ! -d target ]]; then
        echo "[ERROR] No target directory found. Please build the project first."
        exit 1
    fi
    cd target
    if [[ ! -f MANIFEST.MF ]]; then
        echo "[WARN] No MANIFEST.MF file found. Please make sure you have a MANIFEST.MF file in the target directory."
        cd - > /dev/null
        return 0
    fi
    jar cfm "$PROJECTNAME.jar" MANIFEST.MF -C . .
    cd ..
    mv target/"$PROJECTNAME.jar" .
    echo "[INFO] Project packaged successfully!"
}

run() {
    [[ "$1" == "false" ]] && return 0
    if [[ ! -f "$PROJECTNAME.jar" ]]; then
        if [[ ! -d target ]]; then
            echo "[ERROR] No target directory found. Please build the project first."
            exit 1
        fi
        cd target
        echo "[INFO] Running the project"
        java -cp ".:../*" Main
    else
        echo "[INFO] Running the project from the jar file"
        java -jar "$PROJECTNAME.jar"
    fi
}

usage() {
    echo "Usage: $0 [options]"
    echo "Options:"
    echo "  -p, --package    Package the project"
    echo "  -s, --skipTests  Skip running tests"
    echo "  -b, --skipBuild  Skip building the project"
    echo "  -d, --skipDeps   Skip checking dependencies"
    echo "  -r, --run        Run the project"
    echo "  -c, --clean      Clean the project"
    echo "  -x, --delTests   Delete the tests directory after running tests"
}

main() {
    PACKAGE=false
    TEST=true
    SKIP_BUILD=false
    SKIP_DEPS=false
    RUN=false
    CLEAN=false
    DELETE_AFTER_TESTS=false

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            -p|--package) PACKAGE="true"; shift ;;
            -s|--skipTests) TEST="false"; shift ;;
            -b|--skipBuild) SKIP_BUILD="true"; shift ;;
            -d|--skipDeps) SKIP_DEPS="true"; shift ;;
            -r|--run) RUN="true"; shift ;;
            -c|--clean) CLEAN="true"; shift ;;
            -x|--delTests) DELETE_AFTER_TESTS="true"; shift ;;
            *) usage; exit 1 ;;
        esac
    done

    [[ "$CLEAN" == "true" ]] && rm -rf target && rm -f "$PROJECTNAME.jar"

    setup_dependencies $SKIP_DEPS || exit 1
    build $SKIP_BUILD $TEST || exit 1
    test $TEST || exit 1

    [[ "$DELETE_AFTER_TESTS" == "true" ]] && rm -rf target/tests

    package $PACKAGE || exit 1
    run $RUN
}

main "$@"
