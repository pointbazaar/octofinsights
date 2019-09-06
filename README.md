# Octofinsights 

Basic web application which allows for the 
Tracking of Sales, Leads, Expenses and Projects

## Features

- [x] Track Leads
- [x] Track Sales
- [x] Track Expenses
- [ ] Track Projects
- [x] Dashboard
    - [x] Monthly Profits Graph
    - [x] Widgets
    - [x] Business Value over time
- [ ] use Google Calendar API to track work on Projects
- [ ] Login with OAuth via Google



### Feedback and Feature Requests
Further Features are planned.
Feedback is welcome. Please open an issue or contact
alex23667@gmail.com 


### Steps to self-host this web app:

You can self host this web app easily.

- clone this repository
- [optional] setup a reverse proxy and subdomains for octofinsights.yourdomain.tld
- create a file `credentials.txt`
    - on the first line, your db username
    - on the second line, your db password
- edit `library.xml` and put in your jdbc url, username,password
- `./generate_classes.sh` 
    - this creates some classes which are needed to build this project
    - for this, it needs to connect to your mysql database
- `./serverScript.sh`
    - this builds the project
- `./serverScriptDeploy.sh`
    - this starts the server

https://console.developers.google.com/apis/dashboard?project=octofinsights&supportedpurview=project
