#! /bin/bash

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
            echo "Downloading $NAME from $URL..."
            curl --output $NAME $URL -SsL
        fi
    done

    cat MANIFEST.MF.tmpl | sed -e "s/\${JAR_NAMES}/${JAR_NAMES}/" > target/MANIFEST.MF

    echo "Dependencies checked!"
}

build() {
    [[ "$1" == "true" ]] && return 0
    echo "Building the project..."
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
    echo "Project built successfully!"
}

test() {
    [[ "$1" == "false" ]] && return 0
    echo "Running tests..."

    cd target
    java -cp ".:../*" -ea tests/Test || exit 1
    cd - > /dev/null

    echo "Tests passed!"
}

package() {
    [[ "$1" == "false" ]] && return 0
    echo "Packaging the project..."
    PROEJCTNAME=$(basename $(pwd))
    cd target
    jar cfm "$PROEJCTNAME.jar" MANIFEST.MF -C . .
    cd ..
    mv target/"$PROEJCTNAME.jar" .
    echo "Project packaged successfully!"
}

main() {
    PACKAGE=false
    TEST=true
    SKIP_BUILD=false
    SKIP_DEPS=false

    while [[ "$#" -gt 0 ]]; do
        case $1 in
            -p|--package) PACKAGE="true"; shift ;;
            -s|--skipTests) TEST="false"; shift ;;
            -b|--skipBuild) SKIP_BUILD="true"; shift ;;
            -d|--skipDeps) SKIP_DEPS="true"; shift ;;
            *) echo "Unknown parameter passed: $1"; exit 1 ;;
        esac
    done

    setup_dependencies $SKIP_DEPS || exit 1
    build $SKIP_BUILD $TEST || exit 1
    test $TEST || exit 1
    package $PACKAGE || exit 1
}

main "$@"
