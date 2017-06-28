start /b /i  cmd

Chcp 437 &&mvn clean compile exec:exec -Dexec.executable="java" -U-e -Dexec.args="-Xms1024m -Xmx1024m -classpath %classpath io.netty.example.http.websocketx.benchmarkserver.WebSocketServer" -Dmaven.test.skip=true -Dcheckstyle.skip=true
