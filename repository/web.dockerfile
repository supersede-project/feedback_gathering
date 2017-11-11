FROM java:8-jre
MAINTAINER Ronnie Schaniel <ronnieschaniel@gmail.com>

VOLUME /tmp
ADD build/libs/feedback_repository-2.0.0.jar app.jar

RUN apt-get update
RUN apt-get install mysql-client -y
RUN apt-get install libmysql-java -y

RUN bash -c 'touch /app.jar'

RUN mkdir /supersede_files
RUN chmod ugoa+w /supersede_files

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom -Djava.net.preferIPv4Stack=true", "-jar", "/app.jar"]