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

```bash
# clone the repository
git clone https://github.com/supersede-project/monitor_feedback/tree/master/repository
cp src/main/resources/application.properties-dist src/main/resources/application.properties
```

Fill in your DB credentials in the newly created application.properties. Do not add this file to the GIT index. 
Create the test database according to the credentials in src/main/resources/application-test.properties.

```bash
cd repository
# install the project's dependencies
gradle build
```

# Tests

To run the integration tests, execute the following commands:

```bash
gradle test
```

# License

tba
