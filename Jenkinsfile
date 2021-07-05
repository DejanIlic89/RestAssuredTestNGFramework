// CODE_CHANGES = getGitChanges()
pipeline {
    agent any
    parameters {
        string(name: 'VERSION', defaultValue: '', description: 'version to deploy on prod')
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: 'version to choose')
        booleanParam(name: 'executeTests', defaultValue: true, description: '')
    }
    environment {
        NEW_VERSION = '1.3.0'
    }
    tools {
        maven 'maven-3.6.3'
        jdk 'java-8u221'
    }
    stages {
        stage("Build") {
            when {
                expression {
                    env.BRANCH_NAME == 'master' //&& CODE_CHANGES == true
                }
            }
            steps {
                echo 'Building the application...'
                echo "Building version ${NEW_VERSION}"
            }
        }
        stage("Test") {
            when {
                expression {
                    env.BRANCH_NAME == 'master' && params.executeTests
                }
            }
            steps {
                echo 'Testing the application...'
            }
        }
        stage("Deploy") {
            steps {
                echo 'Deploying the application...'
                echo 'Deploying version ${params.VERSION}'
            }
        }
    }
    post {
        always {
            //
        }
        success {
            //
        }
        failure {
            //
        }
    }
}