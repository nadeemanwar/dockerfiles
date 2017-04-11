FROM jenkins
MAINTAINER na1431@att.com


ENV JENKINS_USER admin
ENV JENKINS_PASS admin

# Skip initial setup
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

COPY executors.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY default-user.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY create-job.groovy /usr/share/jenkins/ref/init.groovy.d/

COPY plugins.txt /usr/share/jenkins/plugins.txt
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/plugins.txt

COPY jenkins-cli.jar /tmp/jenkins-cli.jar
COPY seed-job-config.xml /tmp/seed-job-config.xml

