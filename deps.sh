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

set -a
JAR_NAMES="${JAR_NAMES}"
cat MANIFEST.MF.tmpl | envsubst > target/MANIFEST.MF
set +a

echo "Dependencies checked!"
