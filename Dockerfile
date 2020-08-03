FROM tomcat:9.0.37-jdk11-openjdk
ARG JAR_FILE=target/*.war
COPY ${JAR_FILE} /usr/local/tomcat/webapps/
COPY spring/server.xml /usr/local/tomcat/conf/