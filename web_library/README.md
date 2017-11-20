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

The web library runs well with node v6.10.2, npm 4.5.0 and gulp 3.9.1. 

# How to start

In order to get the project running use:

```bash
git clone https://github.com/supersede-project/monitor_feedback/tree/master/web_library
cd web_library
# install the project's dependencies
npm install
```

Create a virtual host on your local machine to avoid problems with cross origin loads. Ideally use localhost as the ServerName to avoid problems with getUserMedia() in Google Chrome.

    <VirtualHost *:80>
        DocumentRoot /<your_path>/web_library
        ServerName localhost
        <Directory "/<your_path>/web_library">
            Options Indexes FollowSymLinks
            AllowOverride All
            Order allow,deny
            Allow from all
        </Directory>
    </VirtualHost>

Add an entry to your hosts file (if you do not use localhost as the ServerName above):

    127.0.0.1	localhost.web-library.dev

You then have to define some application settings and the Orchestrator as well as the Repository endpoint in the app/js/config.ts or in a configuration file under app/js/configurations:

    export const apiEndpointOrchestrator = 'http://example.com/';
    export const applicationPath = 'orchestrator/feedback/{lang}/applications/';
    export const applicationId = 1;
    export const applicationName = 'example.com Website';
    
    export const apiEndpointRepository = 'http://example.com/';
    export const feedbackPath = "feedback_repository/{lang}/feedbacks";

Configure the project:

```bash
gulp configure --configuration=<configuration_name>
```

Use the filename as an argument, e.g.

```bash
gulp configure --configuration=default
```  

Compile all the .ts files to .js and bundle the project:

```bash
gulp build.dev
```

Open the index.html file in the browser of your choice.

Note: This setup is tested and runs successfully using node v7.0.0 and npm 3.10.8. It might also run with different versions. It utilizes the typescript compiler of the WebStorm IDE.

# Table of Contents

- [Introduction](#introduction)
- [How to start](#how-to-start)
- [Table of Content](#table-of-content)
- [Compatibility](#compatibility)
- [Integration](#integration)
- [Configuration](#configuration)
- [Styling](#styling)
- [Internationalization](#internationalization)
- [Running tests](#running-tests)
- [WebStorm](#webstorm)
- [Deployment](#deployment)
- [Directory Structure](#directory-structure)
- [Troubleshooting](#troubleshooting)
- [License](#license)

# Compatibility

The library should work fine on the following browsers (with Promise polyfill):

* Firefox 3.5+
* Google Chrome
* Opera 12+
* IE9+
* Safari 6+


# Integration

Attention: The current dist package is cleaned and overridden. If you have adjusted translations in the locales you need to save them and copy them again after the build.

```bash
# create the dist bundle
gulp build.prod
```

```javascript

<!-- header -->
<link rel="stylesheet" href="dist/jqueryui/jquery-ui.min.css"/>
<link rel="stylesheet" href="dist/main.min.css"/>

<!-- footer -->
<script src="dist/jquery-1.9.1.js"></script>
<script src="dist/jqueryui/jquery-ui.min.js"></script>
<script src="dist/audio/recorder.js"></script>
<script src="dist/audio/Fr.voice.js"></script>
<script src="dist/screenshot/fabric.min.js"></script>
<script src="dist/screenshot/customiseControls.js"></script>
<script src="dist/screenshot/spectrum.js"></script>
<script src="dist/jquery.feedback.min.js"></script>
<script>
    $(document).ready(function () {
        $('#feedbackEntryPoint').feedbackPlugin({
            // optional options
            'distPath': 'dist/',
            'userId': '99999999',
            'lang': 'en',
            'fallbackLang': 'de',
            'dialogCSSClass': 'my-custom-dialog-class',
            'colorPickerCSSClass': 'my-color-picker',
            'defaultStrokeWidth': 4
        });
    });
</script>

```

A click on the element with the feedbackEntryPoint ID triggers then the feedback mechanism.

# Configuration

The following options are available to configure the jQuery plugin on the client side:

|option   |description   |
|---|---|
|apiEndpointOrchestrator|Orchestrator endpoint url, e.g. https://platform.supersede.eu:8443/|
|apiEndpointRepository|Repository endpoint url|
|applicationId|Application ID used for the Orchestrator endpoint, e.g. 20 for Senercon|
|backgroundColor|Background color of the feedback button|
|color|Font color of the feedback button|
|colorPickerCSSClass| Assign a custom css class to the color picker|
|defaultStrokeWidth| Adjust the stroke width of the screenshot annotations|
|dialogCSSClass|Assign a custom css class to the dialog|
|dialogPositionMy|Point of the dialog that is used for positioning. E.g. if you want to use the top left corner of the dialog to align, you specify 'left top'.|
|dialogPositionAt|Point of the target area defined in dialogPositionOf that is used for the positioning. E.g. if you want to align the dialogPositionMy point at the top of the target area with an offset of 30 pixels and centered horizontally, you specify 'center top+30' |
|dialogPositionOf|Target area that is used for positioning. E.g. window, $('body'), $('html'), etc. |
|distPath   |Path to the ressources that are used within the library (css, img, etc.)   |
|fallbackLang|Language to be used if 'lang' is not available in the locales folder|
|lang|Language to be used|
|userId|ID that gets sent to the repository component and will be store alongside the feedback|

# Styling

In order to adjust the styling of the feedback dialog, please edit app/css/_config.scss. Further adjustments could be done in main.scss or any other .scss file.

Note: This setup assumes that you have a SCSS to CSS compiler in your IDE. So, the main.css should get built.

# Internationalization

The translations are located in dist/locales/. To adjust an existing translation please update values in the translation.json files. 
To add new languages please create a folder using the ISO-691-1 language code as the name of the folder. Then copy an existing translation files from another language and adjust the values. 

# Running tests

```bash
npm test
```

# WebStorm

To build the web library you can either execute 'gulp build.dev' on the command line or create corresponding run configurations in WebStorm.

The configuration parameter takes the file from app/js/configurations and from app/css/configurations:
![Run configuration with JS conf](https://raw.githubusercontent.com/supersede-project/monitor_feedback/master/images/web_lib_docu_run_conf_1.png)

The configuration parameter takes the file from app/js/configurations and a different one from app/css/configurations: 
![Run configuration with JS conf and SCSS conf](https://raw.githubusercontent.com/supersede-project/monitor_feedback/master/images/web_lib_docu_run_conf_2.png)

The typescript compiler should be enabled in WebStorm to compile .ts files to .js files. 
![Typescript settings](https://raw.githubusercontent.com/supersede-project/monitor_feedback/master/images/web_lib_docu_typescript.png)



# Deployment

To deploy a demo page please copy env/stage/stage.json-dist to env/stage/stage.json and fill in the required information. Then execute

```bash
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

# Troubleshooting

If the web library does not run:

```bash
npm install -g webpack gulp-cli typescript
```


# License

Apache License 2.0
















