FROM eclipse-temurin:17-jdk-focal
MAINTAINER Ismael foletia <xmael@keepsec.ca>
WORKDIR /app
COPY . .
RUN ./mvnw clean package -Dmaven.test.skip
CMD ["./mvnw", "spring-boot:run"]




