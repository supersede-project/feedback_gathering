
# Introduction

Is a RESTfull Web API that provides endpoints for storing, receiving and updating the feedback configuration.

# Table of Contents

- [Introduction](#introduction)
- [Table of Content](#table-of-content)
- [Deployment](#deployment)
- [Directory Structure](#directory-structure)
- [License](#license)

# Deployment

To deploy the newest version of the orchestrator:

- clone the github repository:

```bash
git clone https://github.com/supersede-project/monitor_feedback/tree/master/orchestrator
cd orchestrator
```

- execute the mysql script in the /deployment/dumps folder of the project and create the database:

```bash
cd orchestrator/deployment/dumps
mysql -u username -p orchestrator < orchestrator.sql
```

- switch to the war files folder of the deployment folder, open the war file and enter your database 
  credentials (dbuser and password) in the file "ch/uzh/ifi/feedback/library/transaction/config.properties":
  
 ``` 
  dburl=jdbc:mysql://localhost:3306/
  dbuser=your_user_name
  dbpassword=your_password
 ```
 
- copy the war file to your tomcat WepApps directory. In linux systems, this is usually the directory 
  "/usr/share/tomcat7/webapps"

- start the tomcat server over the console

```bash
sudo service tomcat7 start
```

# Directory Structure

```
.
├── src                        <- source code of the application
|   |                          <- servlet class and guice modules 
│   ├── controllers            <- controller classes
│   ├── model                  <- model classes
│   ├── serialization          <- serialization classes
│   ├── services               <- service classes
│   ├── validation             <- validation classes
├── WebContent                 <- compiled classes and libs
├── deployment                 <- SQL dumps and war files for deployment
├── test                       <- API- and unit-tests
│   ├── test                   <- API tests for all controllers
```

# License

tba
