
# Introduction

Is a RESTfull Web API that provides endpoints for storing, receiving and updating the feedback configuration.
For a detailed API documentation, please visit: http://docs.supersedeorchestratorapi.apiary.io/#

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

# copy the configuration files for your configuration
cp src/main/resources/application.properties-dist src/main/resources/application.properties
cp src/main/resources/application-test.properties-dist src/main/resources/application-test.properties
```

Create a test database and a local database to run the application locally if needed. Fill in your DB credentials and all other required values in the newly created application.properties and application-test.properties. Do not add this file to the GIT index.

```bash
cd orchestrator
# install the project's dependencies and generate the war file in build/libs/
gradle build
```

DB dump in src/main/resources/db/migrations/supersede_orchestrator_db_structure.sql 

# Tests

To run the integration tests, execute the following commands:

```bash
gradle test
```

# License

Apache License 2.0