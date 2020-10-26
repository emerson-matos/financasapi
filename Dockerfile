FROM tomcat:9.0.37-jdk11-openjdk
ARG FILE=target/gestao-financeira-*.war
COPY ${FILE} /usr/local/tomcat/webapps/gestao-financeira.war
COPY spring/server.xml /usr/local/tomcat/conf/
RUN ls /usr/local/tomcat/webapps/