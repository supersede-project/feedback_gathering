
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
git clone https://github.com/supersede-project/monitor_feedback/tree/master/orchestrator
cp src/main/resources/application.properties-dist src/main/resources/application.properties
cp src/main/resources/application-test.properties-dist src/main/resources/application-test.properties
```

Fill in your DB credentials and all other required values in the newly created application.properties and application-test.properties. Do not add this file to the GIT index.

```bash
cd orchestrator
# install the project's dependencies
gradle build
```

DB dump in src/main/resources/db/migrations/feedback/supersede_orchestrator_db_structure.sql

# Tests

To run the integration tests, execute the following commands:

```bash
gradle test
```

# License

Apache License 2.0