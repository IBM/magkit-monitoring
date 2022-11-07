@Library('jenkins-library-acid-base-github@latest') _
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
  }

  environment {
    MAVEN_VERSION = 'maven360'
    JDK_VERSION = 'openJDK11'
  }

  stages {
    stage('Verify') {
      when {
        branch 'PR-*'
      }
      steps {
        script {
          def secrets = [
            [$class: 'VaultSecret', path: "acid/tools/nexus/live", engineVersion: 2, secretValues: [
              [$class: 'VaultSecretValue', envVar: 'DEPLOY_PASSWORD', vaultKey: 'password'],
              [$class: 'VaultSecretValue', envVar: 'DEPLOY_USERNAME', vaultKey: 'username']
            ]]
          ]
          wrap([$class: 'VaultBuildWrapper', vaultSecrets: secrets]) {
            def mavenParams = "clean verify -U -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD"
            withMaven {
              sh "mvn $mavenParams"
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
      }
      steps {
        script {
          def secrets = [
            [$class: 'VaultSecret', path: "acid/tools/nexus/live", engineVersion: 2, secretValues: [
              [$class: 'VaultSecretValue', envVar: 'DEPLOY_PASSWORD', vaultKey: 'password'],
              [$class: 'VaultSecretValue', envVar: 'DEPLOY_USERNAME', vaultKey: 'username']
            ]]
          ]
          wrap([$class: 'VaultBuildWrapper', vaultSecrets: secrets]) {
            def mavenParams = "clean deploy -U -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD"
            withMaven {
              sh "mvn $mavenParams"
            }
          }
        }
      }
    }

    stage('Sonar') {
      when {
        anyOf {
          branch 'dev'; branch 'master';
        }
      }
      steps {
        script {
          def secrets = [
            [$class: 'VaultSecret', path: "acid/tools/nexus/live", engineVersion: 2, secretValues: [
              [$class: 'VaultSecretValue', envVar: 'DEPLOY_PASSWORD', vaultKey: 'password'],
              [$class: 'VaultSecretValue', envVar: 'DEPLOY_USERNAME', vaultKey: 'username']
            ]]
          ]
          wrap([$class: 'VaultBuildWrapper', vaultSecrets: secrets]) {
            def mavenParams = "-Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD"
            acidExecuteSonar(this, 'magnolia', [
                withCoverage: false,
                jdk: JDK_VERSION,
                maven: MAVEN_VERSION,
                additionalMavenParameters: mavenParams
              ]
            )
          }
        }
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}
