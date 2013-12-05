mvn compile
nohup mvn exec:java -Dexec.mainClass="com.alibaba.HttpServer" -Dexec.args="80" &
