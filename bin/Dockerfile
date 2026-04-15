# Stage 1: Build
FROM openjdk:27-ea-jdk AS builder
WORKDIR /app
COPY . .
RUN javac *.java
RUN jar cfe projetJava.jar App *.class

# Stage 2: Run
FROM openjdk:27-ea-jdk
WORKDIR /app
COPY --from=builder /app/projetJava.jar /app

EXPOSE 10000

CMD ["java", "-jar", "projetJava"]