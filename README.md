# [Octofinsights](http://octofinsights.com)

[![CodeFactor](https://www.codefactor.io/repository/github/vanautrui/octofinsights/badge)](https://www.codefactor.io/repository/github/vanautrui/octofinsights)
[![](https://jitpack.io/v/vanautrui/octofinsights.svg)](https://jitpack.io/#vanautrui/octofinsights)
[![Known Vulnerabilities](https://snyk.io/test/github/vanautrui/octofinsights/badge.svg)](https://snyk.io/test/github/vanautrui/octofinsights)

Basic web application which allows for the 
Tracking of Sales, Leads, Expenses and Projects

## Features

- [x] Track Leads
- [x] Track Sales
- [x] Track Expenses
- [x] Track Projects
- [x] Dashboard Widgets
    - [x] Monthly Profits Graph
    - [x] Open Leads Widget
    - [x] Business Value over time Graph
- [ ] use Google Calendar API to track work on Projects


### Feedback and Feature Requests
Further Features are planned.
Feedback is welcome. Please open an issue or contact
alex23667@gmail.com 


### Steps to self-host this web app:

You can self host this web app easily.

- obtain a database
  - either install mysql on your server,
  - or get a managed database from a cloud provider,
  - or get a free database from [db4free.net](https://www.db4free.net/signup.php)

- packages you should install on your server: `jdk >= 11` , `maven`, 
- clone this repository to your server
- it could be that you need some jar files for JOOQ to function correctly
    - `jaxb-api-2.2.12.jar`, `jaxb-core-2.2.11.jar`, 
    `jaxb-impl-2.3.0.jar`, `jooq-3.11.11.jar`,
    `jooq-codegen-3.11.11.jar`, `jooq-meta-3.11.11.jar`
    - you can find most of these on maven central probably https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api
- [optional] setup a reverse proxy and subdomains for octofinsights.yourdomain.tld
- create the 'octofinsights' database on your mysql server by running `setup_tables.sql`
    - https://stackoverflow.com/questions/11407349/how-to-export-and-import-a-sql-file-from-command-line-with-options
- create a file `credentials.txt`
    - on the first line, your db username
    - on the second line, your db password
- edit `library.xml` and put in your jdbc url, username,password
- `./generate_classes.sh` 
    - this creates some classes which are needed to build this project
    - for this, it needs to connect to your mysql database
- `./build.sh`
    - this builds the project
- `./serverScriptDeploy.sh`
    - this starts the server

### Other Stuff

https://console.developers.google.com/apis/dashboard?project=octofinsights&supportedpurview=project
