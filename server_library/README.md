# Introduction

The web library provides the base classes for the orchestrator and the repository as well as the core of the
REST framework used by them. It also stores the database configuration file that has to be edited before
the installation.

# Table of Contents

- [Introduction](#introduction)
- [Table of Content](#table-of-content)
- [Running tests](#running-tests)
- [Installation](#installation)
- [Directory Structure](#directory-structure)
- [License](#license)


# installation

- Clone the repository:

```bash
git clone https://github.com/supersede-project/monitor_feedback/tree/master/server_library
cd server_library
```

- Copy the config.properties-dist file in the to config.properties (do NOT add this newly created file to the git index)
- Fill in your database credentials (dbuser, password)) in the file config.properties

- Install the library into you local maven repository:

```bash
mvn clean install
```

# Running tests

The tests are all executed during installation of the library. To execute the unit tests
without installation, do the following:

```bash
cd server_library
mvn test
```

# Directory Structure

```
.
├── src/uzh/ifi/feedback/library        <- source code of the application
│   ├── rest                            <- classes belonging to the REST framework (Manager, base classes)
│   │   ├── annotations                 <- annotations for controllers and model classes
│   │   ├── authorization               <- classes for authorization of controller methods
│   │   ├── routing                     <- classes associated with the routing logic used by the RestManager class
│   │   ├── serialization               <- base class for json serialization/deserialization
│   │   ├── service                     <- base classes for the service layer
│   │   ├── test                        <- base class for the API tests used in the api test projects
│   │   ├── validaton                   <- base class for the validation of model objects
│   ├── transaction                     <- Classes related to the database configuration and transation management
├── test                                <- contains dummy classes and unit tests for the validation logic
├── README.md                           <- this file
└── pom.xml                             <- maven project file containing all dependencies of the project
```

# License

tba
