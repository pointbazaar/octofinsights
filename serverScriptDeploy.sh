cd /home/vanautrui/octofinsights
pwd 

#kill all apps running on this port
lsof -ti tcp:9377 | xargs kill -9

nohup mvn exec:java &
