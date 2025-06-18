package com.vplay;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;

@WebServlet("/hello")
public class MyServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<h1>Hello from MyServlet</h1>");
    }
}


/*javac -cp /home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.servlet-api-5.0.0.jar \
  -d /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/classes \
  /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java/com/vplay/MyServlet.java
  
  javac -cp /home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.servlet-api-5.0.0.jar \
  -d WEB-INF/classes \
  src/main/java/com/vplay/database/*.java \
  src/main/java/com/vplay/utils/*.java \
  src/main/java/com/vplay/*.java

javac -cp "lib/mysql-connector-java-9.0.0.jar" src/main/java/com/vplay/database/DatabaseConnection.java
java -cp "WEB-INF/lib/mysql-connector-java-9.0.0.jar:src/main/java" com.vplay.database.DatabaseConnection

javac -cp "WEB-INF/lib/*" -d WEB-INF/classes src/main/java/com/vplay/utils/*.java

javac -cp "/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.servlet-api-5.0.0.jar:/home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/lib/*"   -d /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/classes   /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java/com/vplay/MailServlet.java

javac -cp ".:\
/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.servlet-api-5.0.0.jar:\
/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.websocket-api-1.1.2.jar:\
/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/websocket-api.jar:\
/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/websocket-client-api.jar" -d /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/classes /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java/com/vplay/call/SignalingServlet.java /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java/com/vplay/WebSocketConfig.java


sudo netstat -tuln | grep 8080
tail -f /home/ayyappankalai/Server/apache-tomcat-10.1.34/logs/catalina.out


~/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java$ 
javac -cp "/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.servlet-api-5.0.0.jar:/home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/lib/*" \
-d /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/classes \
com/vplay/database/DatabaseConnection.java com/vplay/database/SQLQueries.java com/vplay/call/CallServlet.java
Note: com/vplay/call/CallServlet.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.


javac \
  -cp "/home/ayyappankalai/Server/apache-tomcat-10.1.34/lib/jakarta.servlet-api-5.0.0.jar:/home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/lib/*" \
  -d /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/WEB-INF/classes \
  /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java/com/vplay/utils/*.java \
  /home/ayyappankalai/Server/apache-tomcat-10.1.34/webapps/vplay/src/main/java/com/vplay/bot/BotServlet.java

  */
  
  /*
vplay/
├── index.html
├── html
|   └──register.html
|   └──viewPage.html
|   └──friendList.html
|   └──meeting.html
├── css/
│   └── style.css
|    └──viewPage.css
|    └──friendRequest.css
|    └──friendList.css
|    └──call.css
├── js/
│   └── login.js
|    └──register.js
|    └──viewpage.js
|    └──friendList.js
|    └──friendRequest.js
|    └──call.js
|    └──callConnect.js
├── WEB-INF/
│   └── classes/
│       └── com/
│           └── vplay/
│               ├── MyServlet.class
│               └── database/
│                   ├── DatabaseConnection.class
│                   └── SQLQueries.class
|     
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── vplay/
│                   ├── MyServlet.java
│                   ├── AnotherServlet.java
│                   └── database/
│                       ├── DatabaseConnection.java
│                       └── SQLQueries.java

  */
