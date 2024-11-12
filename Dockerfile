ARG BASE_JDK=eclipse-temurin:17.0.13_11-jdk-ubi9-minimal
ARG BASE_JRE=eclipse-temurin:17.0.13_11-jre-ubi9-minimal

FROM ${BASE_JDK} AS deps
WORKDIR /app
COPY dependencies.list .
COPY MANIFEST.MF.tmpl .
COPY run.sh .
RUN bash ./run.sh --skipTests --skipBuild

FROM ${BASE_JDK} AS build
WORKDIR /app
COPY . .
COPY --from=deps /app/target/MANIFEST.MF /app/target/MANIFEST.MF
COPY --from=deps /app/*.jar /app/
RUN bash ./run.sh --package --skipDeps

FROM ${BASE_JRE}
WORKDIR /app
COPY --from=build /app/*.jar /app/
ENTRYPOINT ["java", "-jar", "app.jar"]
