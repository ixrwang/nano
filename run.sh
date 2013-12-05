main=com.alibaba.HttpServer
port=8080
pids=`ps gx | grep $main | grep $port | awk '{print $1}'`
for pid in $pids
do
kill -9 $pid
echo kill $pid
done
mvn clean compile
rm -rf logs.txt
nohup mvn exec:java -Dexec.mainClass="$main" -Dexec.args="$port" > logs.txt &
