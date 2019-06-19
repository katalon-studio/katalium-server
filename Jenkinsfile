pipeline {
    agent none
    stages {
        stage ('Prepare') {
            agent any
            steps {
                sh 'mkdir -p $HOME/docker-cache-node && chmod 777 $HOME/docker-cache-node'
                sh 'mkdir -p $HOME/docker-cache-maven && chmod 777 $HOME/docker-cache-maven'
            }
        }

        stage('Build') {
            stages {
                stage ('Web') {
                    agent {
                        docker {
                            image 'node:10.15.3'
                            args '-v /$HOME/docker-cache-node:/tmp/docker-cache-node -e "HOME=/tmp/docker-cache-node"'
                        }
                    }
                    steps {
                        sh 'cd web && npm install && npm run build'
                        stash includes: 'web/build/**/*', name: 'frontend'
                    }
                }
                stage ('Backend') {
                    agent {
                        docker {
                                image 'maven'
                                args '-v /$HOME/docker-cache-maven:/tmp/docker-cache-maven'
                            }
                    }
                    steps {
                        script {
                            unstash 'frontend'
                            sh 'ls web/build'
                            sh 'mkdir -p src/main/resources'
                            sh 'rm -rf src/main/resources/static && mv web/build src/main/resources/static'
                            sh 'mvn -Duser.home=/tmp/docker-cache-maven clean package'
                            stash includes: 'target/kata-server.jar', name: 'jar'
                        }
                    }
                    post {
                        always {
                            unstash 'jar'
                            archiveArtifacts artifacts: 'target/kata-server.jar'
                            // archiveArtifacts artifacts: 'script'
                        }
                    }
                }
            }
        }
    }
}
