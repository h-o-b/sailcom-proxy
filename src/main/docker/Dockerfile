
FROM tomcat:8.5.5-jre8-alpine

RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
COPY sailcom-proxy.war ${CATALINA_HOME}/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
