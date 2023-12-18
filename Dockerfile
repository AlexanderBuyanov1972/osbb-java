## сборка проекта
#FROM maven:latest as build
#COPY src src
#COPY pom.xml pom.xml
#RUN mvn clean
#RUN mvn package dependency:copy-dependencies -DincludeScope=runtime -Dmaven.test.skip=true
# сборка образа
FROM bellsoft/liberica-openjdk-debian:latest
# добавляем пользователя в группу пользователей, так как негоже работать под привилегированным пользователем
RUN adduser --system user-spring && addgroup --system spring-boot && adduser user-spring spring-boot
# переход в режим пользователя
USER user-spring
# создаём рабочую директорию
WORKDIR /app
# копируем из директории target необходимые файлы
COPY target/*.jar ./application.jar
ENTRYPOINT ["java","-jar", "./application.jar"]
#################################################################################################################
# docker stop $(docker ps -q)
# mvn clean package dependency:copy-dependencies -DincludeScope=runtime -Dmaven.test.skip=true
# java -jar target/demo-0.0.1-SNAPSHOT.jar com.example.demo.DockerApplication
# docker run --rm -p 8080:8080 --name=web-container web:v01
# docker exec -it web-container bash
# docker build -t osbb-java:latest .
# docker build -t osbb-vue:latest .
# docker-compose up