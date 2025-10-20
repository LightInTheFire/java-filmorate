FROM amazoncorretto:21.0.8 AS builder

# Set working directory
WORKDIR /app

# Install tar and gzip (required for mvnw to unpack Maven)
RUN yum install -y tar gzip && yum clean all

COPY mvnw ./
COPY .mvn ./.mvn
COPY pom.xml ./
COPY lombok.config ./

# Ensure mvnw is executable
RUN chmod +x mvnw

# Set up Maven local repository for caching
ENV MAVEN_OPTS="-Dmaven.repo.local=/app/.m2/repository"

# Download dependencies (caching layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests -Dcheckstyle.skip=true

# Stage 2: Layers stage - Use Amazon Corretto JDK for layer extraction
FROM amazoncorretto:21.0.8-alpine AS layers
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
# Extract layers from the JAR for better caching in runtime (Spring Boot specific best practice)
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Runtime stage - Use a slim Amazon Corretto image for smaller footprint
FROM amazoncorretto:21.0.8-alpine

# Set non-root user for security
RUN addgroup -g 1001 appgroup && adduser -u 1001 -G appgroup -s /bin/sh -D appuser
USER appuser

# Set working directory
WORKDIR /app

# Copy extracted layers from builder stage
COPY --from=layers /app/dependencies/ ./
COPY --from=layers /app/spring-boot-loader/ ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/ ./

# Expose the application port
EXPOSE 8080

# Entry point to run the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
