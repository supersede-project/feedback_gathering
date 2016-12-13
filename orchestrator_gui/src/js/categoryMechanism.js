// CATEGORY component extends the alpaca select
$.alpaca.Fields.CategoryMechanism = $.alpaca.Fields.SelectField.extend({
    getFieldType: function() {
        return "CATEGORY_TYPE";
    },

    getTitle: function() {
        return "Category Mechanism";
    },

    getSchemaOfOptions: function(){
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
        delete myProp.properties.multiselect;
        delete myProp.properties.multiple;
        delete myProp.properties.name;
        delete myProp.properties.noneLabel;
        delete myProp.properties.optionLabels;
        delete myProp.properties.removeDefaultNone;
        delete myProp.properties.showMessages;
        delete myProp.properties.size;
        delete myProp.properties.sort;
        delete myProp.properties.type;
        delete myProp.properties.useDataSourceAsEnum;
        delete myProp.properties.validate;
        delete myProp.properties.view;

        return Alpaca.merge(myProp, {
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
                "multiple": {
                    "title": "Allow Multiple Values",
                    "type": "boolean",
                    "default": false,
                    "description": "Allow multiple selection if true."
                },
                "size": {
                    "title": "Number of displayed options",
                    "type": "number",
                    "description": "Number of options to be shown."
                },
                "emptySelectFirst": { // not in uzh properties
                    "title": "Select first item in list if data empty",
                    "type": "boolean",
                    "default": false,
                    "description": "If the data is empty, then automatically select the first item in the list."
                },
                "noneLabel": { // not in uzh properties
                    "title": "Label for the 'None'-Option",
                    "type": "string",
                    "default": "None",
                    "description": "The label to use for the 'None' option in a list (select, radio or otherwise)."
                },
                "hideNone": { // not in uzh properties
                    "title": "Hide the option 'None'",
                    "type": "boolean",
                    "default": false,
                    "description": "Whether to hide the None option from a list (select, radio or otherwise). This will be true if the field is required and false otherwise."
                },
                "removeDefaultNone": { // not in uzh properties
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
        delete mySchema.properties.dependencies;
        delete mySchema.properties.disallow;
        delete mySchema.properties.format;
        delete mySchema.properties.readonly;
        delete mySchema.properties.type;
        return mySchema;
    }
});
Alpaca.registerFieldClass("CATEGORY_TYPE", Alpaca.Fields.CategoryMechanism);
