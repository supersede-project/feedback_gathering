# Introduction

Is a RESTfull Web API that provides endpoints for storing, receiving and deleting user feedbacks.

# Table of Contents

- [Introduction](#introduction)
- [Table of Content](#table-of-content)
- [Installation](#installation)
- [Deployment](#deployment)
- [running tests](#tests)
- [Directory Structure](#directory-structure)
- [License](#license)

# Installation

- first install the server_library in your local maven repository: https://github.com/supersede-project/monitor_feedback/tree/master/server_library
- run the following commands:

```bash
# clone the repository
git clone https://github.com/supersede-project/monitor_feedback/tree/master/repository
cd repository
# install the project's dependencies
mvn clean install
```

# Deployment

To deploy the newest version of the repository:

- first create the database on your deployment server. The sql file is in the "deployment/dumps" folder of the project. To install the database on your local machine, run the following commands with your database credentials:

```bash
cd repository/deployment/dumps
mysql -u username -p orchestrator < orchestrator.sql
```

- copy the war file to your tomcat WepApps directory on your deployment server. In linux systems, this is usually the directory "/usr/share/tomcat7/webapps". The war file is in the folder "repository/target" after the installation.

- start the tomcat server over the console

```bash
sudo service tomcat7 start
```

# Tests

- to run the unit tests, execute the following commands:

```bash
cd repository
mvn test
```

API tests

- Open the web.xml file of the war file after the installation. The config is in the folder "/WebContent/WEB-INF".
- Search for the following section and set the param-value to "true".

```
<context-param>
  <param-name>debug</param-name>
  <param-value>false</param-value>
</context-param>
```

- Deploy the war file on your local machine (#deployment)
- follow the instructions in the "test" section of the repository_api_test project (https://github.com/supersede-project/monitor_feedback/tree/master/repository_api_tests)


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
