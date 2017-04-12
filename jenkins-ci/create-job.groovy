import jenkins.model.*
import java.io.*

def jobName = "seed-job"
//def xmlStream = new FileInputStream("/tmp/seed-job-config.xml") 
//def configXml = new File("/tmp/seed-job-cofig.xml").text 

def configXml = "<?xml version='1.0' encoding='UTF-8'?>" +
"<project>" +
  "<description></description>" +
  "<keepDependencies>false</keepDependencies>" +
  "<properties/>" +
  "<scm class=\"hudson.scm.NullSCM\"/>" +
  "<canRoam>true</canRoam>" +
  "<disabled>false</disabled>" +
  "<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>" +
  "<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>" +
  "<triggers/>" +
  "<concurrentBuild>false</concurrentBuild>" +
  "<builders/>" +
  "<publishers/>" +
  "<buildWrappers/>" +
"</project>"


configXml = configXml.trim().replaceFirst("^([\\W]+)<","<")

def xmlStream = new ByteArrayInputStream( configXml.getBytes() )

Jenkins.instance.createProjectFromXML(jobName, xmlStream)

def seedJobName = "Seedjob"
def seedJobConfigXml = new File("/tmp/seed-job-config.xml").text 
seedJobConfigXml = seedJobConfigXml.trim().replaceFirst("^([\\W]+)<","<")

def xmlDataStream = new ByteArrayInputStream( seedJobConfigXml.getBytes() )

Jenkins.instance.createProjectFromXML(seedJobName, xmlDataStream)

def job = Jenkins.instance.getItem(seedJobName)
Jenkins.instance.getQueue().schedule(job) 
