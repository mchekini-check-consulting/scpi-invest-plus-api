node("ci-node"){
    def GIT_COMMIT_HASH = ""

    stage("Checkout"){
        checkout scm
        GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
    }

    stage("Unit tests"){
        sh "chmod 777 mvnw && ./mvnw clean test"
    }

    stage("Quality Analyses"){
        sh "./mvnw clean verify sonar:sonar \\\n" +
                "  -Dsonar.projectKey=scpi-invest-plus-api \\\n" +
                "  -Dsonar.projectName='scpi-invest-plus-api' \\\n" +
                "  -Dsonar.host.url=https://sonar.check-consulting.net \\\n" +
                "  -Dsonar.token=squ_172db7cd93144a9b96e321012b2667d1fca60359"
    }

    stage("Build Jar file"){
        sh "./mvnw package -DskipTests"
    }

    stage("Build Docker Image"){
        sh "sudo docker build -t mchekini/scpi-invest-plus-api:$GIT_COMMIT_HASH ."
    }

    stage("Push Docker image"){
        withCredentials([usernamePassword(credentialsId: 'mchekini', passwordVariable: 'password', usernameVariable: 'username')]) {
            sh "sudo docker login -u $username -p $password"
            sh "sudo docker push mchekini/scpi-invest-plus-api:$GIT_COMMIT_HASH"
            sh "sudo docker rmi mchekini/scpi-invest-plus-api:$GIT_COMMIT_HASH"
        }
    }
}