FROM jboss/wildfly

ENV LANG pl_PL.UTF-8

ADD target/web.war /opt/jboss/wildfly/standalone/deployments

ADD ssl/server.keystore /opt/jboss/wildfly/standalone/configuration/
ADD ssl/standalone.xml /opt/jboss/wildfly/standalone/configuration/

RUN /opt/jboss/wildfly/bin/add-user.sh admin w!1k!7352xd --silent

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]