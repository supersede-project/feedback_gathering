# Introduction

The Orchestrator GUI is a web-based visual interface to create feedback forms. It basically uses the [Alpaca-Plugin](http://www.alpacajs.org/), but is highly customized to our needs. The Orchestrator GUI sends the form schema as JSON to the orchestrator backend. The JSON-schema-output produced by the Alpaca-Plugin is transformed to match the JSON-schema of the orchestrator backend.

# How to start

In order to get the project running, follow these steps:

```bash
git clone https://github.com/supersede-project/monitor_feedback
cd orchestrator_gui
```

Start local webserver (i.e. XAMPP), then open:

    index.html

Nota: The Orchestrator GUI is currently developed in a separate branch "orchestrator_gui_develop".

# Table of Contents

- [Introduction](#introduction)
- [How to start](#how-to-start)
- [Table of Content](#table-of-content)
- [Compatibility](#compatibility)
- [Development](#development)
- [Usage](#usage)
- [Running tests](#running-tests)
- [Directory Structure](#directory-structure)
- [License](#license)

# Compatibility

The Orchestrator GUI has been developed with the following browsers:

* Firefox
* Google Chrome

# Development

- For further Alpaca configuration, visit the [Alpaca Documentation](http://www.alpacajs.org/documentation.html).
- For further interface configuration with the orchestrator backend, visit the [orchestrator API](http://docs.supersedeorchestratorapi.apiary.io/#).

# Usage

Once the Orchestrator GUI is opened, the feedback form can be created by dragging and dropping the desired components from the column "Form Components" to the column "Form Designer". By clicking the option-Button, the feedback component can be customized to the corresponding use. In the column "Form Preview", you can see how the form looks like in the end.

By clicking on "Save Form", the form schema is send as JSON to the orchestrator backend into the corresponding application.


# Running tests

```bash
// tbd
```

# Directory Structure

```
.
├── build                        
│   ├── alpaca      <- JavaScript and CSS-Files
│   ├── tmp                    
├── deployment
│   gulp                       
├── lib             <- Project library
├── node_modules               
├── src             <- Project Source Code
│   ├── css
    ├── js
    ├── templates
    index.html      <- Webinterface Orchestrator GUI
├── test            <- Tests
├── thirdparty                 
```


# License

Apache License 2.0















