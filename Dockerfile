FROM eclipse-temurin:17.0.13_11-jdk-ubi9-minimal AS build
WORKDIR /app
RUN curl --output sqlite-jdbc-3.46.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.0.0/sqlite-jdbc-3.46.0.0.jar -SsL
RUN curl --output slf4j-api-2.0.13.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar -SsL
COPY . .
RUN bash build.sh

FROM eclipse-temurin:17.0.13_11-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /app/target/sqlite-jdbc-3.46.0.jar /app/
COPY --from=build /app/target/slf4j-api-2.0.13.jar /app/
COPY --from=build /app/target /app/
ENTRYPOINT ["java", "-cp", ".:./*", "Main"]
