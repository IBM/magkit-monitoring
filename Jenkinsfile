@Library('jenkins-library-acid-base-github@0.43.3') _
pipeline {

    agent {
        label 'apertomagkit'
    }

    tools {
        maven 'maven360'
        jdk '8u152'
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(artifactNumToKeepStr: '1', numToKeepStr: '10'))
    }

    stages {
        stage('Build') {
            when {
                anyOf {
                    branch 'dev'; branch 'master'; branch 'PR-*'
                }
            }
            steps {
                script {
                    def secrets = [
                        [$class: 'VaultSecret', path: "mobile-engineering/tools/nexus/acid.build", engineVersion: 1, secretValues: [
                            [$class: 'VaultSecretValue', envVar: 'DEPLOY_PASSWORD', vaultKey: 'password'],
                            [$class: 'VaultSecretValue', envVar: 'DEPLOY_USERNAME', vaultKey: 'username']
                        ]]
                    ]
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: secrets]) {
                        def mavenCompile = false
                        def mavenDeploy = true
                        def mavenParams = " --batch-mode -U -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD"

                        if (env.BRANCH_NAME.matches("PR-(.*)")) {
                            mavenCompile = true
                            mavenDeploy = false
                            mavenParams = "clean package --batch-mode -U -Duser=$DEPLOY_USERNAME -Dpw=$DEPLOY_PASSWORD"
                        }

                        acidExecuteMaven(this, [
                            configFileId: '5ff62c62-4015-4854-8ab8-29bd275a1a92',
                            compile: mavenCompile,
                            deploy: mavenDeploy,
                            params: mavenParams,
                            suppressionsEnabled: false
                        ])
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
