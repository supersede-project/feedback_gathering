# Introduction

Is a jQuery plugin that provides the integration of feedback mechanisms in the web context. 

It provides the following features:

- Text feedback mechanism
- Rating feedback mechanism
- Screenshot feedback mechanism
- Audio feedback mechanism
- Categorization of feedback mechanism
- Uploading files(s) 
- Flexible configuration of feedback requests
- Pull and push feedback request presentation

# How to start

In order to get the project running use:

```bash
git clone https://github.com/supersede-project/monitor_feedback/tree/master/web_library
cd web_library
# install the project's dependencies
npm install
```

Create a virtual host on your local machine to avoid problems with cross origin loads:

    <VirtualHost *:80>
        DocumentRoot /<your_path>/web_library
        ServerName localhost.web-library.dev
        <Directory "/<your_path>/web_library">
            Options Indexes FollowSymLinks
            AllowOverride All
            Order allow,deny
            Allow from all
        </Directory>
    </VirtualHost>

Add an entry to your hosts file:

    127.0.0.1	localhost.web-library.dev

Bundle the project:

```bash
webpack
```

Open the index.html file in the browser of your choice.

# Table of Contents

- [Introduction](#introduction)
- [How to start](#how-to-start)
- [Table of Content](#table-of-content)
- [Integration](#integration)
- [Configuration](#configuration)
- [Internationalization](#internationalization)
- [Running tests](#running-tests)
- [Deployment](#deployment)
- [Directory Structure](#directory-structure)
- [License](#license)

# Integration

```bash
# create the dist bundle
webpack
```

```javascript

<!-- header -->
<link rel="stylesheet" href="https://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<link rel="stylesheet" href="dist/main.css"/>

<!-- footer -->
<script src="dist/jquery.min.js"></script>
<script src="dist/spectrum.min.js"></script>
<script src="https://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
<script src="dist/screenshot/fabric.js"></script>
<script src="dist/screenshot/customiseControls.js"></script>
<script src="dist/jquery.feedback.min.js"></script>
<script>
    $(document).ready(function () {
        $('#feedbackEntryPoint').feedbackPlugin({
            // optional options
            'color': '#000000',
            'distPath': 'dist/'
        });
    });
</script>

```

A click on the element with the feedbackEntryPoint ID triggers then the feedback mechanism.

# Configuration

The following options are available to configure the jQuery plugin on the client side:

|option   |description   |
|---|---|
|distPath   |Path to the ressources that are used within the library (css, img, etc.)   |
|userId|ID that gets sent to the repository component and will be store alongside the feedback|
|lang|Language to be used|
|fallbackLang|Language to be used if 'lang' is not available in the locales folder|
|color|Font color of the feedback button|
|backgroundColor|Background color of the feedback button|

# Internationalization

The translations are located in dist/locales/. To adjust an existing translation please update values in the translation.json files. 
To add new languages please create a folder using the ISO-691-1 language code as the name of the folder. Then copy an existing translation files from another language and adjust the values. 

# Running tests

```bash
npm test
```

# Deployment

To deploy a demo page please copy env/state.json-dist to env/state.json and fill in the required information. Then execute

```bash
webpack
gulp deploy
```

# Directory Structure

```
.
├── app                        <- source code of the application
│   ├── config
│   ├── e2e
│   ├── img
│   ├── js
│   ├── locales
│   ├── models
│   ├── services
│   ├── templates
│   ├── views
├── env
│   ├── stage.json-dist        <- demo page deployment settings
├── tools
├── typings                    <- typings directory. Contains all the external typing definitions defined with typings
├── gulpfile.js                <- gulp tasks
├── index.html                 <- html page to run the plugin in during development
├── karma.conf.js              <- test config
├── tsconfig.json              <- configuration of the typescript project
├── tslint.json                <- tslint configuration
└── webpack.config.js          <- bundler configuration
```


# License

tba
















