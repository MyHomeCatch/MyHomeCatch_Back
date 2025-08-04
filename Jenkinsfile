pipeline {
    agent any

    tools {
        jdk 'JDK-17'
    }
    
    // --- 환경 변수 설정 ---
    // 파이프라인 전체에서 사용할 변수들을 정의합니다.
    environment {
        EC2_SERVER_IP      = '43.200.8.39'
        // Tomcat의 웹 애플리케이션 배포 디렉터리 경로
        TOMCAT_DEPLOY_DIR  = '/home/ec2-user/tomcat/webapps'
        
        SSH_CREDENTIAL_ID  = 'github-cicd-token' 
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
        stage('Deploy to Tomcat') {
            steps {
                // SSH Agent 플러그인을 사용하여 안전하게 서버에 접속합니다.
                sshagent(credentials: [SSH_CREDENTIAL_ID]) {
                    script {
                        echo "Starting deployment to Tomcat..."
                        // scp 명령어로 Jenkins 작업 공간에 생성된 .war 파일을
                        // EC2 서버의 Tomcat webapps 디렉터리로 복사합니다.
                        // 'ROOT.war'로 이름을 변경하면 웹 애플리케이션이 루트 경로(/)에 배포됩니다.
                        sh "scp -o StrictHostKeyChecking=no build/libs/*.war ec2-user@${EC2_SERVER_IP}:${TOMCAT_DEPLOY_DIR}/ROOT.war"
                        echo "Backend deployment complete!"
                    }
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
