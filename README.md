# e_taskmanager
Task manager app built with Angular, Android and Java WS.
This project consists of an Android App to conclude the tasks, a Angular site to create and manage tasks communicating with a Java Spring Web Service, database is MySQL.

- Create the taskmanager database using the following script:
db/taskmanager.sql

- To run the server (port 8080):
java -jar ./server/target/server-0.1.0.jar

- To run the site (port 8888):
cd ./site
gulp

- To run the app:
Change the serverUrl in app/app/src/main/java/com/hello/taskmanager/MainActivity.java

If you change the gulp port you need to change the server (CORS).
WS is set to access MySQL in port 3306;