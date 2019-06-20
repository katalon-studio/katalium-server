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
                            sh '''
                                ls web/build
                                mkdir -p src/main/resources
                                rm -rf src/main/resources/static && mv web/build src/main/resources/static
                                mvn -Duser.home=/tmp/docker-cache-maven clean package
                            '''
                            stash includes: 'target/kata-server.jar', name: 'zip'
                        }
                    }
                    post {
                        always {
                            unstash 'zip'
                            sh '''
                                rm -f katalium.zip
                                rm -rf katalium-server
                                mkdir -p katalium-server
                                cp target/kata-server.jar katalium-server
                                cp script/* katalium-server
                                chmod +x katalium-server/*
                            '''
                            zip zipFile: 'katalium.zip', archive: true, glob: 'katalium-server/**'
                        }
                    }
                }
            }
        }
    }
}
