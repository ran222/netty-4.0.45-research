start /b /i  cmd

Chcp 437 && mvn clean compile exec:java -Dexec.mainClass="io.netty.example.http.websocketx.benchmarkserver.WebSocketServer" -U-e  -Dmaven.test.skip=true -Dcheckstyle.skip=true