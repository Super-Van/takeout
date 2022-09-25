#!bin/bash
echo "=================="
echo "自动化部署脚本启动"
echo "=================="

APP_NAME=takeout

echo "停止此前服务"
# 杀死项目上次启动产生的进程
pid=`ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}'`
if [ $pid ]
then
    echo "停止成功"
    kill -15 $pid
fi
sleep 2
# 怕没有杀死
pid=`ps -ef | grep $APP_NAME | grep -v grep | grep -v kill | awk '{print $2}'` 
if [ $pid ]
then
    echo "杀死进程"
    kill -9 $pid
fi

echo "从github拉取最新代码"
cd /usr/program/$APP_NAME
# 已经自行克隆了
git pull
echo "拉取完成"

echo "开始打包"
# 本例跳过测试
output=`mvn clean package -Dmaven.test.skip=true`
cd target

echo "启动服务"
# 在后台运行，将日志输出到文件
nohup java -jar $APP_NAME-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod &> $APP_NAME.log &
echo "服务已启动"