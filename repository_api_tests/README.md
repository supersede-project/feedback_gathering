# Introduction

Is a test project that contains API tests for the repository server (https://github.com/supersede-project/monitor_feedback/tree/master/repository). The API tests are executed against the repository server that is deployed on the local machine.


# Table of Contents

- [Introduction](#introduction)
- [Table of Content](#table-of-content)
- [Running tests](#running-tests)
- [Directory Structure](#directory-structure)
- [License](#license)

# running-tests

- to run the tests, the server_library (https://github.com/supersede-project/monitor_feedback/tree/master/server_library) and the repository (https://github.com/supersede-project/monitor_feedback/tree/master/repository) have to be installed in the local maven repository. The repository war file has to be deployed on the local machine and the "debug" parameter of the web.xml of the war file has to be "true".

- clone the repository and execute the tests with maven:

```bash
git clone https://github.com/supersede-project/monitor_feedback/tree/master/repository_api_tests
cd repository_api_tests
mvn test
```

# Directory Structure

```
.
├── test                       <- contains API tests for all controllers of the repository
├── README.md                  <- this file
├── pom.xml                    <- maven project file
```

# License

tba
