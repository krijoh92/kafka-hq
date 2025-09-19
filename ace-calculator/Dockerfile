# Multi-stage build for Java 24 Spring Boot Kafka Streams app
FROM eclipse-temurin:24-jdk AS build

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies (cached layer if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:24.0.2_12-jre

# Set working directory
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/ace-calculator-*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Install curl and create non-root user
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* && \
    groupadd -g 1001 app && \
    useradd -r -u 1001 -g app app && \
    chown -R app:app /app

# Switch to non-root user
USER app

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]