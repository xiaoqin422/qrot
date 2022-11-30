# qrot
**基于simbot3框架的机器人项目**

### 一、项目结构
    -logs                       日志文件
     --*_error.log
     --*_info.log
    -java               业务代码
        --config                配置层
        --controller            控制层
        --dao                   数据层
        --service               业务层
        --model                 实体对象（entity、dto）
        --utils                 工具类
    QRotApplication             主启动类
    -resource           项目资源
        simbot-bot              机器人配置
        logback-spring.xml      日志配置
        application.yaml        项目配置
    -test               单元测试
    -Dockerfile                 容器构建
    -Jenkinsfile                DevOps流水线