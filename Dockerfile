FROM eclipse-temurin:17-jdk-focal
MAINTAINER Ismael foletia <xmael@keepsec.ca>
WORKDIR /app
#docker run --name mincontrol-dev-postgres -v /custom/mount:/var/lib/postgresql/data -e POSTGRES_PASSWORD=2+2Font4 -e POSTGRES_USER=postgres -e POSTGRES_DB=mincontrol -p 5432:5432 -d postgres
#docker run --name pgadmin-mincontrol-dev-postgres -p 5433:80 --hostname pgadmin-mincontrol-dev.devops.kamer-center.net  -e VIRTUAL_HOST=pgadmin-mincontrol-dev.devops.kamer-center.net -e LETSENCRYPT_HOST=pgadmin-mincontrol-dev.devops.kamer-center.net -e "PGADMIN_DEFAULT_EMAIL=xmaelxd@gmail.com" -e "PGADMIN_DEFAULT_PASSWORD=2+2Font4" -d dpage/pgadmin4
COPY . .
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package
RUN ls -a
# ENV SPRING_DATASOURCE_URL jdbc:postgresql://85.214.29.254:5432/campus
# ENV SPRING_DATASOURCE_USERNAME postgres
# ENV SPRING_DATASOURCE_PASSWORD 2+2Font4
# ENV SPRING_JPA_HIBERNATE_DDL_AUTO update
#RUN mvn -v
#COPY src ./src

CMD ["./mvnw", "spring-boot:run"]



