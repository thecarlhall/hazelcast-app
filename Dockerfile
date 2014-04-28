FROM ubuntu:14.04

MAINTAINER thecarlhall

RUN apt-get -y install jetty8
RUN sed -i "s/NO_START=1/NO_START=0/" /etc/default/jetty8
RUN sed -i "s/#JETTY_HOST=\$(uname -n)/JETTY_HOST=0.0.0.0/" /etc/default/jetty8
ADD target/hazelcast-app.war /var/lib/jetty8/webapps/
RUN service jetty8 restart

ENTRYPOINT ["service","jetty8","start"]

EXPOSE 8080
