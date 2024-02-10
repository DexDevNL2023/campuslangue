FROM eclipse-temurin:17-jdk-focal
MAINTAINER Ismael foletia <xmael@keepsec.ca>
COPY  ~/.m2  ~/
RUN ls -a ~/
WORKDIR /app

COPY . .
RUN ls -a
RUN ./mvnw clean package
RUN ls -a
CMD ["./mvnw", "spring-boot:run"]




