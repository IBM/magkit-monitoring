@Library('ix-jenkins-library@main') _
def SECRETS = [
  [$class: 'VaultSecret', path: "acid/tools/nexus/live", engineVersion: 2, secretValues: [
    [$class: 'VaultSecretValue', envVar: 'DEPLOY_PASSWORD', vaultKey: 'password'],
    [$class: 'VaultSecretValue', envVar: 'DEPLOY_USERNAME', vaultKey: 'username']
  ]]
]

pipeline {
  agent {
    label 'apertomagkit'
  }
  tools {
    maven 'maven360'
    jdk 'openJDK11'
  }
  options {
    disableConcurrentBuilds()
    buildDiscarder(logRotator(artifactNumToKeepStr: '1', numToKeepStr: '10'))
    timeout time: 1, unit: 'HOURS'
  }
  parameters {
    choice(name: 'INPUT_ENV', description: 'Choice of additional action', choices: ['Build', 'Release'])
  }
  environment {
    // notification Slack variables
    SLACK_TOKEN = 'pn1bvDADqWbd1aNeKkInOF8F'
    SLACK_BASE_URL = 'https://ixgreen.slack.com/services/hooks/jenkins-ci/'
    SLACK_CHANNEL = '#cop-magnolia'
  }

  stages {
    stage('Verify PR') {
      when {
        branch 'PR-*'
      }
      expression {
        params.INPUT_ENV != 'Release'
      }
      steps {
        script {
          withVault([vaultSecrets: SECRETS]) {
            withMaven {
              sh 'mvn verify -Pci,coverage -Djenkins.gitBranch=${GIT_BRANCH} -Djenkins.buildNumber=${BUILD_NUMBER} -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD'
            }
          }
        }
      }
    }

    stage('Deploy to Nexus') {
      when {
        anyOf {
          branch 'dev'; branch 'master';
        }
        expression {
          params.INPUT_ENV != 'Release'
        }
      }
      steps {
        script {
          withVault([vaultSecrets: SECRETS]) {
            withMaven {
              sh 'mvn deploy -Pci,coverage -Djenkins.gitBranch=${GIT_BRANCH} -Djenkins.buildNumber=${BUILD_NUMBER} -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD'
            }
          }
        }
      }
      post {
        always {
          ixSendNotifications (this, [projectOs: 'magnolia', sendNoSlackSuccessNotification: true])
        }
      }
    }

    stage('Sonar') {
      when {
        expression {
          params.INPUT_ENV != 'Release'
        }
      }
      steps {
        withVault([vaultSecrets: SECRETS]) {
          withSonarQubeEnv('SonarQube') {
            sh 'mvn sonar:sonar -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD'
          }
        }
      }
    }

    stage('Release') {
      when {
        branch 'master'
        expression {
          params.INPUT_ENV == 'Release'
        }
      }
      post {
        always {
          ixSendNotifications (this, [projectOs: 'magnolia', sendNoSlackSuccessNotification: false])
        }
      }
      steps {
        script {
          echo 'Release monitoring module'
          withVault([vaultSecrets: SECRETS]) {
            withMaven {
              sh 'mvn release:clean release:prepare release:perform -Pci -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD -Djenkins.gitBranch=${GIT_BRANCH} -Djenkins.buildNumber=${BUILD_NUMBER}'
            }
          }
        }
      }
    } // end stage release
  }

  post {
    always {
      cleanWs()
    }
  }
}
