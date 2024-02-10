FROM eclipse-temurin:17-jdk-focal
MAINTAINER Ismael foletia <xmael@keepsec.ca>
WORKDIR /app
COPY . .
# RUN cp .m2  ~/
# RUN ls -a ~/
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.repo.local=/app/.m2
# RUN ls -a
CMD ["./mvnw", "spring-boot:run"]




