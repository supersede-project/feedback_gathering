# jQuery Web Library

The Feedback Mechanisms for the web are realized as a jQuery Plugin. It has the following dependencies:

* jquery
* jquery-ui
* jquery-star-rating-plugin


The library can then get integrate into a website by including:

```javascript

<link rel="stylesheet" href="css/main.css">
<script src="dist/jquery.feedback.min.js"></script>

$(document).ready(function () {
    $('#feedbackEntryPoint').feedback({'color': '#000000'});
});
```

A click on the element with the feedbackEntryPoint ID triggers then the feedback mechanism.