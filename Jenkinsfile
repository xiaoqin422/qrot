pipeline {
    agent any
    options {
        //设置流水线运行的超时时间, 在此之后，Jenkins将中止流水线。
        timeout(time: 1, unit: 'HOURS')
        //所有输出每行都会打印时间戳
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '3')) //保留三个历史构建版本
        //流水线触发后静默时间。注意手动触发的构建不生效
        quietPeriod(10)
        //失败后重试次数 尽量不使用，有BUG
        //retry(3)
    }
    //定义一些环境信息
    environment {
        WS = "${WORKSPACE}"
        //dockerBuild之后的镜像名称,必须全是小写
        IMAGE_NAME = "${BUILD_TAG}".toLowerCase()
        IMAGE_VERSION = "v1.0"
        CONTAINER_NAME = "${JOB_NAME}".replaceAll("/", "_")
        IsPush = "是"
    }
    //声明需要使用的工具
//    tools {
//        maven 'maven-3.8.6'
//        jdk   'jdk1.8'
//    }
    //定义流水线的加工流程
    stages {
        //流水线的所有阶段
        stage('准备') {
            steps {
                echo "正在检测基本信息"
                sh 'java -version'
                sh 'git --version'
                sh 'docker version'
                sh 'mvn -v'
                sh 'pwd && ls -alh'
                //踩坑！！！ 注意env变量的覆盖问题
                script {
                    // Git committer email
                    env.COMMIT_EMAIL = sh(script: "git --no-pager show -s --format='%ae' ${GIT_COMMIT}",
                            returnStdout: true).trim()
                    // Git committer name
                    env.COMMIT_NAME = sh(script: "git --no-pager show -s --format='%an' ${GIT_COMMIT}",
                            returnStdout: true).trim()
                }
                sh 'printenv'
            }
        }
        //1、编译 "abc"
        stage('编译') {
            //jenkins不配置任何环境的情况下， 仅适用docker 兼容所有场景
//            agent {
//                docker {
//                    image 'maven:3.8.6-jdk-8'
//                    args '-v /var/jenkins_home/app/mavenPath:/root/.m2'
//                }
//            }
            steps {
                sh 'printenv'
                //git下载来的代码目录下
                sh 'pwd && ls -alh'
                sh 'mvn -v'
                //只要jenkins迁移，不会对我们产生任何影响
                sh 'mvn clean package -s "/var/jenkins_home/appconfig/maven/settings.xml"  -D maven.test.skip=true '
            }
        }

        //2、测试，每一个 stage的开始，都会重置到默认的WORKSPACE位置
        stage('测试') {
            steps {
                sh 'pwd && ls -alh'
                echo "测试..."
            }
        }

        //3、打包
        stage('打包') {
            steps {
                echo "打包..."
                //检查Jenkins的docker命令是否能运行
                sh 'docker version'
                sh 'pwd && ls -alh'
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }
        stage('部署') {
            input {
                message "确认联调环境部署"
                ok "确认"
                //可以加身份选择
            }
            steps {
                sh "printenv"
                sh "docker rm -f ${CONTAINER_NAME} || true"
                sh "docker run -d -p 8082:8080 -v /data/docker/qrot/cache:/qrot/cache -v /data/docker/qrot/logs:/qrot/logs --restart always --name ${CONTAINER_NAME} ${IMAGE_NAME}"
            }
        }
        stage('推送') {
            environment {
                BITBUCKET_COMMON_CREDS = credentials('47cceff6-611b-4480-a42f-0bbe321f1250')
            }
            //设置为true，两个任务有一个失败将直接结束
            failFast false
            parallel {
                stage('镜像推送') {
                    agent any
                    options { skipDefaultCheckout true }
                    input {
                        message "镜像推送确认"
                        ok "提交"
                        id "imagePush"
                        parameters {
                            string(name: 'IMAGE_VERSION', defaultValue: '1.0', description: '镜像版本号')
                            choice(name: 'IsPush', choices: ['是', '否'], description: '是否推送')
                        }
                    }
                    steps {
                        script {
                            if ("${IsPush}" == "是") {
                                sh 'pwd && ls -alh'
                                sh "docker login -u ${BITBUCKET_COMMON_CREDS_USR} -p ${BITBUCKET_COMMON_CREDS_PSW} ccr.ccs.tencentyun.com"
                                sh "docker tag ${IMAGE_NAME} ccr.ccs.tencentyun.com/qinxiaoxiao/qrot:${IMAGE_VERSION}"
                                sh "docker push ccr.ccs.tencentyun.com/qinxiaoxiao/qrot:${IMAGE_VERSION}"
                            }
//                            catchError(buildResult: 'SUCCESS', catchInterruptions: false) {
//                                echo '镜像推送失败，请及时处理！'
//                            }
                        }
                    }
                }
                stage('日志推送') {
                    agent any
                    // 跳过默认的代码检出行为
                    options { skipDefaultCheckout true }
                    steps {
                        echo 'TODO 第三方接口调用'
                    }
                }
            }
        }
        stage('发版') {
            //  注意when 执行优先级  比较低（option、input、agent）
            when {
                branch 'master'
                beforeInput true
            }
            input {
                message "是否部署到线上环境"
                ok "是"
                parameters {
                    choice choices: ['腾讯云（82.156.25.172）', '阿里云（106.14.31.50）'], description: '线上环境', name: 'DEPLOY_IP'
                }
            }
            steps {
                script {
                    //groovy
                    String where = "${DEPLOY_IP}"
                    if (where == "腾讯云（82.156.25.172）") {
                        sh "echo 我帮你部署到腾讯云了"
                    } else if (where == "阿里云（106.14.31.50）") {
                        sh "echo 我帮你部署到阿里云了"
                    }
                }
            }
        }

    }


//后置处理过程
    post {
        always {
            cleanWs notFailBuild: true
            emailext attachLog: true, body: '''<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${PROJECT_NAME}-第${BUILD_NUMBER}次构建日志</title>
</head>
 
<body leftmargin="8" marginwidth="0" topmargin="8" marginheight="4"
    offset="0">
    <table width="95%" cellpadding="0" cellspacing="0"
        style="font-size: 11pt; font-family: Tahoma, Arial, Helvetica, sans-serif">
        <h3>本邮件由系统自动发出，请勿回复！</h3>
        <tr>
                           <br/>
                            ${ENV, var="COMMIT_NAME"}，您好，以下为${PROJECT_NAME }项目构建信息</br>
                            <td><font color="#CC0000">构建结果 - ${BUILD_STATUS}</font></td>
                        </tr>
        <tr>
            <td><h2>
                    <font color="#0000FF">构建结果 - ${BUILD_STATUS}</font>
                </h2></td>
        </tr>
        <tr>
            <td><br />
            <b><font color="#0B610B">构建信息</font></b>
            <hr size="2" width="100%" align="center" /></td>
        </tr>
        <tr>
            <td>
                <ul>
                    <li>项目名称 ： ${PROJECT_NAME}</li>
                    <li>构建编号 ： 第${BUILD_NUMBER}次构建</li>
                    <li>触发原因： ${CAUSE}</li>
                    <li>构建日志： <a href="${BUILD_URL}console">${BUILD_URL}console</a></li>
                    <li>构建  Url ： <a href="${BUILD_URL}">${BUILD_URL}</a></li>
                    <li>工作目录 ： <a href="${PROJECT_URL}ws">${PROJECT_URL}ws</a></li>
                    <li>项目  Url ： <a href="${PROJECT_URL}">${PROJECT_URL}</a></li>
                </ul>
            </td>
        </tr>
        <tr>
            <td><b><font color="#0B610B">Changes Since Last Successful Build:</font></b>
            <hr size="2" width="100%" align="center" /></td>
        </tr>
        <tr>
            <td>
                <ul>
                    <li>历史变更记录 : <a href="${PROJECT_URL}changes">${PROJECT_URL}changes</a></li>
                </ul> ${CHANGES_SINCE_LAST_SUCCESS,reverse=true, format="Changes for Build #%n:<br />%c<br />",showPaths=true,changesFormat="<pre>[%a]<br />%m</pre>",pathFormat="    %p"}
            </td>
        </tr>
        <tr>
            <td> <hr size="2" width="100%" align="center" /></td>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td><b><font color="#0B610B">构建情况总览:</font></b>${TEST_COUNTS,var="fail"}<br/>
            <hr size="2" width="100%" align="center" /></td>
        </tr>
        <tr>
            <td><textarea cols="80" rows="30" readonly="readonly"
                    style="font-family: Courier New">${BUILD_LOG,maxLines=23}</textarea>
            </td>
        </tr>
    </table>
</body>
</html>''', compressLog: true, subject: '${ENV, var="JOB_NAME"}-第${BUILD_NUMBER}次构建日志', to: '${ENV, var="COMMIT_EMAIL"}'
        }
        success {
            echo "构建成功 ${currentBuild.result}"
        }
        failure {
            echo "构建失败 ${currentBuild.result}"
        }
    }


}