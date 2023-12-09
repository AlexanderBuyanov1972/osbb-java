FROM openjdk:17-oracle
WORKDIR /app
COPY src .
CMD ["openjdk", "OsbbJavaApplication.java"]