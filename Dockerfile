FROM ubuntu:14.04

MAINTAINER thecarlhall

RUN apt-get -y install openjdk-7-jre-headless curl
RUN mkdir -p /usr/share/tomcat
RUN curl http://apache.osuosl.org/tomcat/tomcat-8/v8.0.5/bin/apache-tomcat-8.0.5.tar.gz | tar zxf - --strip=1 -C /usr/share/tomcat/

CMD ["/usr/bin/java","-Djava.util.logging.config.file=/usr/share/tomcat/conf/logging.properties","-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager","-Djava.endorsed.dirs=/usr/share/tomcat/endorsed","-classpath","/usr/share/tomcat/bin/bootstrap.jar:/usr/share/tomcat/bin/tomcat-juli.jar","-Dcatalina.base=/usr/share/tomcat","-Dcatalina.home=/usr/share/tomcat","-Djava.io.tmpdir=/usr/share/tomcat/temp","org.apache.catalina.startup.Bootstrap","start"]

EXPOSE 8080 5701
