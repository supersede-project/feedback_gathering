$(document).ready(function() {
    var data = {
        "name": "Diego Maradona",
        "feedback": "Very impressive.",
        "ranking": "excellent"
    };

    var schema = {
        "title":"User Feedback",
        "description":"What do you think about Alpaca?",
        "type":"object",
        "properties": {
            "name": {
                "type":"string",
                "title":"Name",
                "required":true
            },
            "feedback": {
                "type":"string",
                "title":"Feedback"
            },
            "ranking": {
                "type":"string",
                "title":"Ranking",
                "enum":['excellent','ok','so so'],
                "required":true
            }
        }
    };

    var options = {
        "form": {
            "attributes": {
                "action": "http://httpbin.org/post",
                "method": "post"
            },
            "buttons": {
                "submit": {
                    "title": "Send Form Data",
                    "click": function () {
                        var val = this.getValue();
                        if (this.isValid(true)) {
                            alert("Valid value: " + JSON.stringify(val, null, "  "));
                            this.ajaxSubmit().done(function () {
                                alert("Posted!");
                            });
                        } else {
                            alert("Invalid value: " + JSON.stringify(val, null, "  "));
                        }
                    }
                }
            }
        }
    };


    $("#form").alpaca({
        "schema": schema,
        "options": options,
        "data" : data
    });

    // save button
    $(".save-button").off().click(function() {

        if (!localStorage)
        {
            alert("Your browser must support HTML5 local storage in order to use this feature");
            return;
        }

        var config = {};
        if (schema)
        {
            config.schema = schema;
        }
        if (options)
        {
            config.options = options;
        }
        var configString = JSON.stringify(config);

        localStorage.setItem("alpacaDesignerConfig", configString);
        alert("Your form was saved in HTML5 local storage");
        alert(localStorage.getItem("alpacaDesignerConfig"));

        // TODO: create a WebService to write the JSON file or to the database
    });

});