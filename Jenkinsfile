def SERVICE_GROUP = "devops"
def SERVICE_NAME = "vault-jenkins-plugin"
def IMAGE_NAME = "${SERVICE_GROUP}-${SERVICE_NAME}"
def REPOSITORY_URL = "https://github.com/devos-asgard/hashicorp-vault-plugin.git"


@Library("bespin-pipeline-library")
def butler = new com.bespin.devops.JenkinsPipeline()
def label = "maven-jdk8"

properties([
  buildDiscarder(logRotator(daysToKeepStr: "60", numToKeepStr: "30"))
])
podTemplate {
  node(label) {
    stage("Prepare") {
      container("builder") {
        butler.prepare(IMAGE_NAME)
      }
    }
    stage("Checkout") {
      container("builder") {
        try {
          git(url: REPOSITORY_URL, branch: BRANCH_NAME)
        } catch (e) {
          throw e
        }

        butler.scan("java")
      }
    }
    stage("Build") {
      container("maven") {
        try {
          butler.mvn_build()
        } catch (e) {
          throw e
        }
      }
    }
    if (BRANCH_NAME == "master") {
      stage("deploy Plugin") {
        container("maven") {
          try {
            butler.mvn_deploy("", "-Dspotbugs.skip=true")
          } catch (e) {
            throw e
          }
        }
      }
    }
  }
}
