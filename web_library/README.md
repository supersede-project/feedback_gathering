# jQuery Web Library

The Feedback Mechanisms for the web are realized as a jQuery Plugin.

## Setup

    $ npm install
        
Ideally the development is locally done on a virtual host (otherwise you might have problems with cross origin loads):
        
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
        
And don't forget to add an entry to the hosts file:
                
    127.0.0.1	localhost.web-library.dev
        
## Bundling
    $ webpack

## Deployment

Generate the bundle with the following command:

    $ webpack
    $ gulp deploy

The library can then get integrate into a website by including:

```javascript

<!-- header -->
<link rel="stylesheet" href="https://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<link rel="stylesheet" href="dist/main.css"/>

<!-- footer -->
<script src="dist/jquery.min.js"></script>
<script src="https://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
<script src="dist/jquery.feedback.min.js"></script>
<script>
    $(document).ready(function () {
        $('#feedbackEntryPoint').feedbackPlugin({'color': '#000000'});
    });
</script>

```

A click on the element with the feedbackEntryPoint ID triggers then the feedback mechanism.


## Testing

Unit Testing

    $ npm test
    
# Frontend Testing

Server has to run:

    $ npm start

Selenium server has to run as well: 
 
    $ webdriver-mananger start

then in another console:
    
    $ protractor protractor.conf.js
