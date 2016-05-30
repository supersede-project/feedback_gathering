# jQuery Web Library

The Feedback Mechanisms for the web are realized as a jQuery Plugin. It has the following dependencies:

## Setup

    $ npm install    
    $ npm start

## Deployment

The library can then get integrate into a website by including:

```javascript

<link rel="stylesheet" href="css/main.css">
<script src="dist/jquery.feedback.min.js"></script>

$(document).ready(function () {
    $('#feedbackEntryPoint').feedbackPlugin({'color': '#000000'});
});
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
