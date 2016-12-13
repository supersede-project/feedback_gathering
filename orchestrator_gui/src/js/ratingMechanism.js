//RATING component extends the alpaca radio button
$.alpaca.Fields.RatingMechanism = $.alpaca.Fields.RadioField.extend({
    getFieldType: function() {
        return "RATING_TYPE";
    },

    getTitle: function() {
        return "Rating Mechanism";
    },

    getSchemaOfOptions: function() {
        var myProp = this.base();
        delete myProp.properties.dataSource;
        delete myProp.properties.disabled;
        delete myProp.properties.emptySelectFirst;
        delete myProp.properties.fieldClass;
        delete myProp.properties.focus;
        delete myProp.properties.helper;
        delete myProp.properties.helpers;
        delete myProp.properties.hidden;
        delete myProp.properties.hideInitValidationError;
        delete myProp.properties.hideNone;
        delete myProp.properties.required;
        delete myProp.properties.name;
        delete myProp.properties.noneLabel;
        delete myProp.properties.optionLabels;
        delete myProp.properties.removeDefaultNone;
        delete myProp.properties.showMessages;
        delete myProp.properties.useDataSourceAsEnum;
        delete myProp.properties.validate;
        delete myProp.properties.vertical;
        delete myProp.properties.view;
        return Alpaca.merge(myProp, {
            /* different properties for the rating... only defautl Rating, maxRating defined in uzh doku */
            "properties": {
                "mandatory": {
                    "title": "Mandatory",
                    "type": "boolean",
                    "default": false
                },
                "optionLabels": {
                    "title": "Option Labels",
                    "type": "array"
                },
                "enum": {
                    "title": "Option Values",
                    "type": "array"
                },
                "removeDefaultNone": {
                    "title": "Remove the option 'None'",
                    "type": "boolean",
                    "default": false,
                    "description": "If true, the default 'None' option will not be shown."
                },
                "hint": {
                    "title": "Tooltip",
                    "type": "string",
                    "description": "Hint of the field"
                }
            }
        });
    },

    getSchemaOfSchema: function () {
        var mySchema = this.base();
        delete mySchema.properties.default;
        delete mySchema.properties.disallow;
        delete mySchema.properties.format;
        delete mySchema.properties.readonly;
        delete mySchema.properties.type;
        return mySchema;
    },

    setup: function() {
        this.base();
        if(!this.options.mandatory) {
            this.options.mandatory = false;
        }

        if(!this.options.enum){
            this.options.enum = [];
        }

        if(!this.options.hint){
            this.options.hint = "";
        }
    }

});
Alpaca.registerFieldClass("RATING_TYPE", Alpaca.Fields.RatingMechanism);
