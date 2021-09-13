FROM openjdk:11 AS builder

COPY . .

RUN ["./gradlew", "assemble"]

FROM openjdk:11

COPY --from=builder /build/libs/carbon-zero-api-staging.jar .

CMD ["java", "-jar", "carbon-zero-api-staging.jar"]
