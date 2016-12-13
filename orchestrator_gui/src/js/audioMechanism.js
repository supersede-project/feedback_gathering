// AUDIO component extends the object field
$.alpaca.Fields.AudioMechanism = $.alpaca.Fields.ObjectField.extend({
    getFieldType: function() {
        return "AUDIO_TYPE";
    },

    getTitle: function() {
        return "Audio Mechanism";
    },

    getSchemaOfOptions: function () {
        var myProp = this.base();
        delete myProp.properties.fields;

        return Alpaca.merge(myProp, {
            "properties": {
                "label": {
                    "title": "Label",
                    "type": "text"
                },
                "mandatory": { // not in uzh properties
                    "title": "Mandatory",
                    "type": "boolean",
                    "default": false
                },
                "hidden": { // not in uzh properties
                    "title": "Hidden",
                    "type": "boolean",
                    "default": false,
                    "description": "Field will be hidden if true."
                },
                "disabled": { // not in uzh properties
                    "title": "Disabled",
                    "type": "boolean",
                    "default": false,
                    "description": "Field will be disabled if true."
                },
                "maxTime": {
                    "title": "Maximal recording time",
                    "type": "number"
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
        delete mySchema.properties.dependencies;
        delete mySchema.properties.disallow;
        delete mySchema.properties.format;
        delete mySchema.properties.maxProperties;
        delete mySchema.properties.minProperties;
        delete mySchema.properties.properties;
        delete mySchema.properties.readonly;
        delete mySchema.properties.required;
        delete mySchema.properties.type;
        return mySchema;
    }
});
Alpaca.registerFieldClass("AUDIO_TYPE", Alpaca.Fields.AudioMechanism);