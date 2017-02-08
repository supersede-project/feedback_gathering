// SCREENSHOT component extends alpaca image field
$.alpaca.Fields.ScreenshotMechanism = $.alpaca.Fields.ImageField.extend({
    getFieldType: function() {
        return "SCREENSHOT_TYPE";
    },

    getTitle: function() {
        return "Screenshot Mechanism";
    },

    getSchemaOfOptions: function () {
        var myProp = this.base();
        delete myProp.properties.allowOptionalEmpty;
        delete myProp.properties.autocomplete;
        delete myProp.properties.data;
        delete myProp.properties.disabled;
        delete myProp.properties.disallowEmptySpaces;
        delete myProp.properties.disallowOnlyEmptySpaces;
        delete myProp.properties.enum;
        delete myProp.properties.fieldClass;
        delete myProp.properties.fields;
        delete myProp.properties.focus;
        delete myProp.properties.helper;
        delete myProp.properties.helpers;
        delete myProp.properties.hidden;
        delete myProp.properties.hideInitValidationError;
        delete myProp.properties.inputType;
        delete myProp.properties.maskString;
        delete myProp.properties.name;
        delete myProp.properties.placeholder;
        delete myProp.properties.required;
        delete myProp.properties.optionLabels;
        delete myProp.properties.showMessages;
        delete myProp.properties.size;
        delete myProp.properties.typeahead;
        delete myProp.properties.validate;
        delete myProp.properties.view;

        return Alpaca.merge(myProp, {
            "properties": {
                "mandatory": { // not in uzh properties
                    "title": "Mandatory",
                    "type": "boolean",
                    "default": false
                },
                "create_in_app_screenshot": { // not in uzh properties
                    "title": "Create in app screenshot",
                    "type": "boolean",
                    "default": false
                },
                "add_freehand_drawings": { // not in uzh properties
                    "title": "User can add freehand drawings",
                    "type": "boolean",
                    "default": false
                },
                "undo_functionality": { // not in uzh properties
                    "title": "Activate undo",
                    "type": "boolean",
                    "default": false
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
        delete mySchema.properties.enum;
        delete mySchema.properties.format;
        delete mySchema.properties.minLength;
        delete mySchema.properties.maxLength;
        delete mySchema.properties.pattern;
        delete mySchema.properties.readonly;
        delete mySchema.properties.type;
        return mySchema;
    },

    setup: function() {
        this.base();
        if(!this.options.mandatory) {
            this.options.mandatory = false;
        }

        if(!this.options.create_in_app_screenshot){
            this.options.create_in_app_screenshot = false;
        }

        if(!this.options.add_freehand_drawings){
            this.options.add_freehand_drawings = false;
        }

        if(!this.options.undo_functionality){
            this.options.undo_functionality = false;
        }

        if(!this.options.hint){
            this.options.hint = "";
        }
    }

});
Alpaca.registerFieldClass("SCREENSHOT_TYPE", Alpaca.Fields.ScreenshotMechanism);