# balance-calculation

##To start service run org.task.config.Application without parameters

##For start task balance calculation call:
######url: {serverUrl}/balance
######method: POST
######body:
######{
######"customerName" : "", (mandatory)
######"time" : "", (mandatory) (format : "yyyy-mm-ddThh:ss:mm")
######"timeout" : "" (optional)
######}
######response: task id
##For get calculation result call:
######url: {serverUrl}/balance/{taskId}
######method: GET
######response:
######{
######"customerName" : "",
######"time" : "",
######"balance" : ""
######}
##For cancel task call:
######url: {serverUrl}/balance/cancel/{taskId}
######method: PUT


