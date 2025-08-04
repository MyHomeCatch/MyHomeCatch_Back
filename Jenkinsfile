pipeline {
    agent any

    tools {
        jdk 'JDK-17'
    }
    
    // --- 환경 변수 설정 ---
    // 파이프라인 전체에서 사용할 변수들을 정의합니다.
    environment {
        // Tomcat의 웹 애플리케이션 배포 디렉터리 경로
        TOMCAT_DEPLOY_DIR  = '/home/ec2-user/tomcat/webapps'
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scm
                echo 'Git Checkout Success!'
            }
        }
          stage('Test') {
        steps {
          sh './gradlew test'
          echo 'test success'
        }
      }
        stage('Build') {
            steps {
              sh 'chmod +x ./gradlew'
                sh './gradlew clean build -x test'
                echo 'build success'
            }
        }
        
        // 빌드된 .war 파일을 Tomcat 서버에 배포
        stage('Deploy') {
            steps {
                    script {
                        echo "Starting deployment to Tomcat..."
                        sshPublisher(
                            continueOnError: false, 
                            failOnError: true,
                            publishers: [
                                sshPublisherDesc(
                                    configName: 'mhc-ec2-server',
                                    verbose: true,
                                    transfers: [
                                        sshTransfer(
                                            // scp 명령어로 Jenkins 작업 공간에 생성된 .war 파일을
                                            sourceFiles: "build/libs/*.war",
                                            removePrefix: "build/libs",
                                            remoteDirectory: "app",
                                            execCommand: "sh ~/app/deploy.sh"
                                            )
                                        ]
                                    )
                                ]
                            )
                        }
                    }
                }
            }

    // --- 빌드 후 조치 ---
    // 파이프라인 실행이 성공하든 실패하든 항상 실행됩니다.
    post {
        always {
            // Jenkins 서버의 디스크 공간을 절약하기 위해 작업 공간(Workspace)을 정리합니다.
            cleanWs()
            echo "🧹 Workspace cleaned up."
        }
    }
}
