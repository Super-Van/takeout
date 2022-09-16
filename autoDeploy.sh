#!bin/bash
echo "================================"
echo "start automatic deployment shell"
echo "================================"

APP_NAME=takeout

echo "stop the former application"
# 杀死项目上次启动产生的进程
pid=`$(ps -ef | grep $APP_NAME | grep -v | grep -v kill | awk '{print $2}')`
if [ $pid ]
then
    echo "stop process"
    kill -15 $pid
fi
# 怕没有杀死
pid=`$(ps -ef | grep $APP_NAME | grep -v | grep -v kill | awk '{print $2}')` 
if [ $pid ]
then
    echo "kill process"
    kill -9 $pid
else
    echo "stop success"
fi

echo "pull the lastest code from github"
cd /usr/local/$APP_NAME
# 已经自行克隆了
git pull
echo "pull finished"

echo "start package"
# 本例跳过测试
output=`mvn clean package -Dmaven.test.skip=true`
cd target

echo "start the application"
# 在后台运行，将日志输出到文件
nohup java -jar $APP_NAME-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &> $APP_NAME.log &
echo "application started"