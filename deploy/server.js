login: ssh root@81.169.199.217 -p 22
cd /opt/campusApi/campus-api-new
git pull origin testing
mvn clean package
//kill process
nano my.log
kill PID
//run
nohup java -jar target/campusApi.jar > my.log 2>&1 &

//kill process
ps aux | grep java
