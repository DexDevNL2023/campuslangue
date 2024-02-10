FROM eclipse-temurin:17-jdk-focal
MAINTAINER Ismael foletia <xmael@keepsec.ca>
WORKDIR /app
COPY . .
RUN mv -R .m2  ~/
RUN ls -a ~/
# RUN ./mvnw clean package -Dmaven.test.skip
# RUN ls -a
CMD ["./mvnw", "spring-boot:run"]




