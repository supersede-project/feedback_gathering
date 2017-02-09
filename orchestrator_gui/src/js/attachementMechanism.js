// ATTACHMENT mechanism extend upload field
$.alpaca.Fields.AttachmentMechanism = $.alpaca.Fields.UploadField.extend({
    getFieldType: function () {
        return "ATTACHMENT_TYPE";
    },

    getTitle: function () {
        return "Attachment Mechanism";
    },

    getSchemaOfOptions: function () {
        var myProp = this.base();
        delete myProp.properties.disabled;
        delete myProp.properties.errorHandler;
        delete myProp.properties.fieldClass;
        delete myProp.properties.fileTypes;
        delete myProp.properties.focus;
        delete myProp.properties.helper;
        delete myProp.properties.helpers;
        delete myProp.properties.hidden;
        delete myProp.properties.hideInitValidationError;
        delete myProp.properties.maxFileSize;
        delete myProp.properties.maxNumberOfFiles;
        delete myProp.properties.multiple;
        delete myProp.properties.name;
        delete myProp.properties.optionLabels;
        delete myProp.properties.showMessages;
        delete myProp.properties.showUploadPreview;
        delete myProp.properties.sort;
        delete myProp.properties.type;
        delete myProp.properties.validate;
        delete myProp.properties.view;
        return Alpaca.merge(myProp, {
            "properties": {
                "mandatory": { // not in uzh properties
                    "title": "Mandatory", // note: alpaca documentation says "required", but its in fact "mandatory"
                    "type": "boolean",
                    "default": false
                },
                "multiple": { // not in uzh properties
                    "title": "Multiple files allowed",
                    "type": "boolean",
                    "description": "Whether to allow multiple file uploads. If maxNumberOfFiles is not specified, multiple will toggle between 1 and unlimited."
                },
                "fileTypes": { // not in uzh properties
                    "title": "Allowed Filetypes",
                    "type": "string",
                    "description": "A regular expression limiting the file types that can be uploaded based on filename"
                },
                "maxNumberOfFiles": { // not in uzh properties
                    "title": "Maximum number of files (-1 = unlimited)",
                    "type": "number",
                    "description": "The maximum number of files to allow to be uploaded. If greater than zero, the maximum number will be constrained. If -1, then no limit is imposed."
                },
                "maxFileSize": { // not in uzh properties
                    "title": "Maximum size of file (in bytes; -1 = unlimited)",
                    "type": "number",
                    "description": "The maximum file size allowed per upload. If greater than zero, the maximum file size will be limited to the given size in bytes. If -1, then no limit is imposed."
                },
                "showUploadPreview": { // not in uzh properties
                    "title": "Show file preview",
                    "type": "boolean",
                    "default": true,
                    "description": "Whether to show thumbnails for uploaded assets (requires preview support)"
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
        delete mySchema.properties.readonly;
        delete mySchema.properties.required;
        delete mySchema.properties.type;
        return mySchema;
    },

    setup: function() {
        this.base();
        if(!this.options.mandatory) {
            this.options.mandatory = false;
        }

        if(!this.options.hint){
            this.options.hint = "";
        }
    }

});
Alpaca.registerFieldClass("ATTACHMENT_TYPE", Alpaca.Fields.AttachmentMechanism);
