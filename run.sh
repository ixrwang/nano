#!/bin/bash
port=$0
if [ $# -lt 1 ]; then
    port=8080
fi
main=com.alibaba.HttpServer
log=target/logs.txt
pids=`ps gx | grep $main | grep $port | awk '{print $1}'`
for pid in $pids
do
kill -9 $pid
echo kill $pid
done
mvn clean compile
rm -rf $log
nohup mvn exec:java -Dexec.mainClass="$main" -Dexec.args="$port" > $log &
