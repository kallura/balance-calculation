# balance-calculation

##Before start up application ensure that MYSQL server is running and database with name "db" exist.
##To start service run org.task.config.Application without parameters or use maven command : mvn spring-boot:run

##For start task balance calculation call:
```
url: {serverUrl}/balance
method: POST
body:
{
"customerName" : "Ciklum",
"time": "2016-10-26T19:22:54"
"timeout" : 1000
}
response: task id
```
#### Field "customerName" is mandatory
#### Field "time" is mandatory (format : "yyyy-mm-ddThh:ss:mm")
#### Field "timeout" is optional
##For get calculation result call:
```
url: {serverUrl}/balance/{taskId}
method: GET
response:
{
"customerName" : "",
"time" : "",
"balance" : ""
}
```
##For cancel task call:
```
url: {serverUrl}/balance/{taskId}
method: DELETE
```


