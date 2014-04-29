FROM ubuntu:14.04

MAINTAINER thecarlhall

RUN apt-get -y install jetty8
RUN sed -i "s/NO_START=1/NO_START=0/" /etc/default/jetty8
RUN sed -i "s/#JETTY_HOST=\$(uname -n)/JETTY_HOST=0.0.0.0/" /etc/default/jetty8
ADD target/hazelcast-app.war /var/lib/jetty8/webapps/

ENTRYPOINT ["/usr/lib/jvm/default-java/bin/java"]
CMD ["-Xmx256m","-Djava.awt.headless=true","-Djava.io.tmpdir=/var/cache/jetty8/data","-Djava.library.path=/usr/lib","-DSTART=/etc/jetty8/start.config","-Djetty.home=/usr/share/jetty8","-Djetty.logs=/var/log/jetty8","-Djetty.host=0.0.0.0","-Djetty.port=8080","-cp","/usr/share/java/commons-daemon.jar:/usr/share/jetty8/start.jar:/usr/lib/jvm/default-java/lib/tools.jar","org.eclipse.jetty.start.Main","--daemon","/etc/jetty8/jetty-logging.xml","/etc/jetty8/jetty.xml","/etc/jetty8/jetty-shared-webapps.xml"]

EXPOSE 8080
