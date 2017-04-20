def project = GIT_REPO
println project
def folderProject = project.replaceAll('/','-')
def contentsAPI = new URL("https://api.github.com/repos/${GIT_REPO}/contents")

def defaultGitURL = new URL("https://github.com/${GIT_REPO}.git")
listView("${project}".replaceAll('/','-')) {
     description('OpenStack-Helm CI')
     columns {
     status()
     weather()
     name()
     lastSuccess()
     lastFailure()
     lastDuration()
     buildButton()
  }
}

folder("${folderProject}"){
     displayName("${folderProject}")
}

def repositoryContents = new groovy.json.JsonSlurper().parse(contentsAPI.newReader())

def helmExclusions = ["docs","tools","dev",".github"]

def aNode = "node { \n"
def checkoutStage = "stage('Checkout'){\n"
def lintStage = "stage('Linting') { \n"
def pkgStage = "stage('Package') { \n"
def endStage = "},\n"
def endNode = "} \n"

aNode = aNode + checkoutStage + endStage + lintStage
repositoryContents.each {
    def dirName = it.name
    if (it.type == "dir" && !helmExclusions.contains(dirName)){
      aNode = aNode +" sh 'helm lint "+dirName+"'\n"
    }
}
aNode = aNode + endStage + pkgStage

repositoryContents.each {
    def dirName = it.name
    if (it.type == "dir" && !helmExclusions.contains(dirName)){
      aNode = aNode +" sh 'helm package "+dirName+"'\n"
    }
}
aNode = aNode + endNode + endNode

def jobName = project+"-"+"Pipeline"

pipelineJob(folderProject+"/"+project.replaceAll('/','-')) {
  parameters {
        stringParam('GIT_URL', "$defaultGitURL")
        stringParam('GIT_REPO', "$GIT_REPO")
        stringParam('GIT_BRANCH', "master")
  }
  scm{
    git('${GIT_URL}', '${GIT_BRANCH}', null)
  }
  definition {
      cps {
        script( aNode )
        sandbox()
      }
  }
}

