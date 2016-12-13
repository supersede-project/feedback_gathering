var setup = function () {
    // view setting
    var MODAL_VIEW = "bootstrap-edit-horizontal";

    var MODAL_TEMPLATE = ' \
        <div class="modal fade"> \
            <div class="modal-dialog"> \
                <div class="modal-content"> \
                    <div class="modal-header"> \
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button> \
                        <h4 class="modal-title"></h4> \
                    </div> \
                    <div class="modal-body"> \
                    </div> \
                    <div class="modal-footer"> \
                    </div> \
                </div> \
            </div> \
        </div> \
    ';

    // Initial schema setup
    var schema = {
        "type": "object"
    };

    // Initial options setup
    var options = {};

    // Initial data setup
    var data = {};

    var setupEditor = function (id, json) {
        var text = "";
        if (json) {
            text = JSON.stringify(json, null, "    ");
        }

        var editor = ace.edit(id);
        editor.setTheme("ace/theme/textmate");
        if (json) {
            editor.getSession().setMode("ace/mode/json");
        }
        else {
            editor.getSession().setMode("ace/mode/javascript");
        }

        setTimeout(function () {
            editor.clearSelection();
            editor.gotoLine(0, 0);
        }, 100);

        return editor;
    };

    var editor1 = setupEditor("schema", schema);
    var editor2 = setupEditor("options", options);

    var mainViewField = null;
    var mainDesignerField = null;

    var doRefresh = function (el, buildInteractionLayers, cb) {
        try {
            schema = JSON.parse(editor1.getValue());
        }
        catch (e) {
        }

        try {
            options = JSON.parse(editor2.getValue());
        }
        catch (e) {
        }

        if (schema) {
            var config = {
                "schema": schema
            };
            if (options) {
                config.options = options;
            }
            if (!config.options) {
                config.options = {};
            }
            config.options.focus = false;
            config.postRender = function (form) {

                if (buildInteractionLayers) {
                    var iCount = 0;

                    // cover every control with an interaction layer
                    form.getFieldEl().find(".alpaca-container-item").each(function () {

                        var alpacaFieldId = $(this).children().first().attr("data-alpaca-field-id");

                        //iCount++;
                        $(this).attr("icount", iCount);

                        var width = $(this).outerWidth() - 22;
                        var height = $(this).outerHeight() + 25;

                        // cover div
                        var cover = $("<div></div>");
                        $(cover).addClass("cover");
                        $(cover).attr("alpaca-ref-id", alpacaFieldId);
                        $(cover).css({
                            "position": "absolute",
                            "margin-top": "-" + height + "px",
                            "margin-left": "-10px",
                            "width": width,
                            "height": height + 10,
                            "opacity": 0,
                            "background-color": "white",
                            "z-index": 900
                        });
                        $(cover).attr("icount-ref", iCount);
                        $(this).append(cover);

                        // interaction div
                        var interaction = $("<div class='interaction'></div>");
                        var buttonGroup = $("<div class='btn-group pull-right'></div>");

                        // optionsButton
                        var optionsButton = $('<button class="btn btn-default btn-xs button-options" alpaca-ref-id="' + alpacaFieldId + '"><i class="glyphicon glyphicon-wrench"></i></button>');
                        buttonGroup.append(optionsButton);

                        // removeButton
                        var removeButton = $('<button class="btn btn-danger btn-xs button-remove" alpaca-ref-id="' + alpacaFieldId + '"><i class="glyphicon glyphicon-remove"></i></button>');
                        buttonGroup.append(removeButton);

                        interaction.append(buttonGroup);
                        interaction.append("<div style='clear:both'></div>");
                        $(interaction).addClass("interaction");
                        $(interaction).attr("alpaca-ref-id", alpacaFieldId);
                        $(interaction).css({
                            "position": "absolute",
                            "margin-top": "-" + height + "px",
                            "margin-left": "-10px",
                            "width": width,
                            "height": height + 10,
                            "opacity": 1,
                            "z-index": 901
                        });
                        $(interaction).attr("icount-ref", iCount);
                        $(this).append(interaction);
                        $(buttonGroup).css({
                            "margin-top": 5 + (($(interaction).height() / 2) - ($(buttonGroup).height() / 2)),
                            "margin-right": "16px"
                        });


                        // click on optionsButton
                        $(optionsButton).off().click(function (e) {

                            e.preventDefault();
                            e.stopPropagation();

                            var alpacaId = $(this).attr("alpaca-ref-id");

                            editOptions(alpacaId);
                        });

                        // click on removeButton
                        $(removeButton).off().click(function (e) {

                            e.preventDefault();
                            e.stopPropagation();

                            var alpacaId = $(this).attr("alpaca-ref-id");
                            removeField(alpacaId);
                        });

                        // when hover, highlight
                        $(interaction).hover(function (e) {
                            var iCount = $(interaction).attr("icount-ref");
                            $(".cover[icount-ref='" + iCount + "']").addClass("ui-hover-state");
                        }, function (e) {
                            var iCount = $(interaction).attr("icount-ref");
                            $(".cover[icount-ref='" + iCount + "']").removeClass("ui-hover-state");
                        });

                        iCount++;
                    });

                    // add dashed
                    form.getFieldEl().find(".alpaca-container").addClass("dashed");
                    form.getFieldEl().find(".alpaca-container-item").addClass("dashed");

                    // for every container, add a "first" drop zone element
                    // this covers empty containers as well as 0th index insertions
                    form.getFieldEl().find(".alpaca-container").each(function () {
                        var containerEl = this;

                        // first insertion point
                        $(this).prepend("<div class='dropzone'></div>");

                        // all others
                        $(containerEl).children(".alpaca-container-item").each(function () {
                            $(this).after("<div class='dropzone'></div>");
                        });

                    });

                    form.getFieldEl().find(".dropzone").droppable({
                        "tolerance": "touch",
                        "drop": function (event, ui) {

                            var draggable = $(ui.draggable);

                            if (draggable.hasClass("form-element")) {
                                var dataType = draggable.attr("data-type");
                                var fieldType = draggable.attr("data-field-type");

                                // based on where the drop occurred, figure out the previous and next Alpaca fields surrounding
                                // the drop target

                                // previous
                                var previousField = null;
                                var previousFieldKey = null;
                                var previousItemContainer = $(event.target).prev();
                                if (previousItemContainer) {
                                    var previousAlpacaId = $(previousItemContainer).children().first().attr("data-alpaca-field-id");
                                    previousField = Alpaca.fieldInstances[previousAlpacaId];

                                    previousFieldKey = $(previousItemContainer).attr("data-alpaca-container-item-name");
                                }

                                // next
                                var nextField = null;
                                var nextFieldKey = null;
                                var nextItemContainer = $(event.target).next();
                                if (nextItemContainer) {
                                    var nextAlpacaId = $(nextItemContainer).children().first().attr("data-alpaca-field-id");
                                    nextField = Alpaca.fieldInstances[nextAlpacaId];

                                    nextFieldKey = $(nextItemContainer).attr("data-alpaca-container-item-name");
                                }

                                // parent field
                                var parentFieldAlpacaId = $(event.target).parent().parent().attr("data-alpaca-field-id");
                                var parentField = Alpaca.fieldInstances[parentFieldAlpacaId];

                                // now do the insertion
                                insertField(schema, options, dataType, fieldType, parentField, previousField, previousFieldKey, nextField, nextFieldKey);
                            }
                            else if (draggable.hasClass("interaction")) {
                                var draggedIndex = $(draggable).attr("icount-ref");

                                // next
                                var nextItemContainer = $(event.target).next();
                                var nextItemContainerIndex = $(nextItemContainer).attr("data-alpaca-container-item-index");
                                var nextItemAlpacaId = $(nextItemContainer).children().first().attr("data-alpaca-field-id");
                                var nextField = Alpaca.fieldInstances[nextItemAlpacaId];

                                form.moveItem(draggedIndex, nextItemContainerIndex, false, function () {

                                    var top = findTop(nextField);
                                    regenerate(top);
                                });
                            }
                        },
                        "over": function (event, ui) {
                            $(event.target).addClass("dropzone-hover");
                        },
                        "out": function (event, ui) {
                            $(event.target).removeClass("dropzone-hover");
                        }
                    });

                    // init any in-place draggables
                    form.getFieldEl().find(".interaction").draggable({
                        "appendTo": "body",
                        "helper": function () {
                            var iCount = $(this).attr("icount-ref");
                            var clone = $(".alpaca-container-item[icount='" + iCount + "']").clone();
                            return clone;
                        },
                        "cursorAt": {
                            "top": 100
                        },
                        "zIndex": 300,
                        "refreshPositions": true,
                        "start": function (event, ui) {
                            $(".dropzone").addClass("dropzone-highlight");
                        },
                        "stop": function (event, ui) {
                            $(".dropzone").removeClass("dropzone-highlight");
                        }
                    });
                }

                cb(null, form);
            };
            config.error = function (err) {
                Alpaca.defaultErrorCallback(err);
                cb(err);
            };

            Alpaca.defaultErrorCallback = Alpaca.DEFAULT_ERROR_CALLBACK;
            $(el).alpaca(config);
        }
    };

    // delete the schema and option fields
    var removeFunctionFields = function (schema, options) {
        if (schema) {
            if (schema.properties) {
                var badKeys = [];

                for (var k in schema.properties) {
                    if (schema.properties[k].type === "function") {
                        badKeys.push(k);
                    }
                    else {
                        removeFunctionFields(schema.properties[k], (options && options.fields ? options.fields[k] : null));
                    }
                }

                for (var i = 0; i < badKeys.length; i++) {
                    delete schema.properties[badKeys[i]];

                    if (options && options.fields) {
                        delete options.fields[badKeys[i]];
                    }
                }
            }
        }
    };

    // for the edit popup
    var editOptions = function (alpacaFieldId, callback) {
        var field = Alpaca.fieldInstances[alpacaFieldId];

        var fieldSchemaSchema = field.getSchemaOfSchema();
        var fieldSchemaOptions = field.getOptionsForSchema();
        var fieldOptionsSchema = field.getSchemaOfOptions();
        var fieldOptionsOptions = field.getOptionsForOptions();

        removeFunctionFields(fieldSchemaSchema, fieldSchemaOptions);
        removeFunctionFields(fieldOptionsSchema, fieldOptionsOptions);

        var fieldData = field.schema;
        var fieldOptionsData = field.options;

        delete fieldSchemaSchema.title;
        delete fieldSchemaSchema.description;
        if (fieldSchemaSchema.properties) {
            delete fieldSchemaSchema.properties.title;
            delete fieldSchemaSchema.properties.description;
            delete fieldSchemaSchema.properties.dependencies;
        }
        delete fieldOptionsSchema.title;
        delete fieldOptionsSchema.description;
        if (fieldOptionsSchema.properties) {
            delete fieldOptionsSchema.properties.title;
            delete fieldOptionsSchema.properties.description;
            delete fieldOptionsSchema.properties.dependencies;
            delete fieldOptionsSchema.properties.readonly;
        }

        if (fieldOptionsOptions.fields) {
            delete fieldOptionsOptions.fields.title;
            delete fieldOptionsOptions.fields.description;
            delete fieldOptionsOptions.fields.dependencies;
            delete fieldOptionsOptions.fields.readonly;
        }

        var fieldConfigSchema = {
            schema: fieldSchemaSchema
        };
        var fieldConfigOptions = {
            schema: fieldOptionsSchema
        };
        if (fieldSchemaOptions) {

            fieldConfigSchema.options = fieldSchemaOptions;
        }
        if (fieldOptionsOptions) {
            fieldConfigOptions.options = fieldOptionsOptions;
        }
        if (fieldOptionsData) {
            // set validation messages to false
            fieldOptionsData.showMessages = false;
            fieldConfigOptions.data = fieldOptionsData;
        }

        fieldConfigOptions.view = {
            "parent": MODAL_VIEW,
            "displayReadonly": false
        };

        fieldConfigOptions.postRender = function (control) {
            var modal = $(MODAL_TEMPLATE.trim());
            modal.find(".modal-title").append(field.getTitle());
            modal.find(".modal-body").append(control.getFieldEl());

            modal.find('.modal-footer').append("<button class='btn btn-primary pull-right okay' data-dismiss='modal' aria-hidden='true'>Okay</button>");
            modal.find('.modal-footer').append("<button class='btn btn-default pull-left' data-dismiss='modal' aria-hidden='true'>Cancel</button>");

            $(modal).modal({
                "keyboard": true
            });

            $(modal).find(".okay").click(function () {

                field.options = control.getValue();
                field.schema.required = field.options.required;
                if(field.options.enum){
                    field.schema.enum = field.options.enum;
                }

                if(field.options.mandatory || field.schema.required){
                    field.schema.required = field.options.mandatory;
                }

                if(field.options.hint){
                    field.options.helper = field.options.hint;
                }

                if(field.options.fieldWidth){
                    field.options.cols = field.options.fieldWidth;
                }

                var top = findTop(field);
                regenerate(top);

                if (callback) {
                    callback();
                }
            });

            control.getFieldEl().find("p.help-block").css({
                "display": "none"
            });
        };

        // finds the div with fielForm class and uses it as alpaca form for the schema and option form
        var x = $("<div><div class='fieldForm'></div></div>");
        $(x).find(".fieldForm").alpaca(fieldConfigOptions);
    };

    var refreshView = function (callback) {
        if (mainViewField) {
            mainViewField.getFieldEl().replaceWith("<div id='viewDiv'></div>");
            mainViewField.destroy();
            mainViewField = null;
        }

        doRefresh($("#viewDiv"), false, function (err, form) {

            if (!err) {
                mainViewField = form;
            }

            if (callback) {
                callback();
            }

        });
    };

    var refreshDesigner = function (callback) {
        $(".dropzone").remove();
        $(".interaction").remove();
        $(".cover").remove();

        if (mainDesignerField) {
            mainDesignerField.getFieldEl().replaceWith("<div id='designerDiv'></div>");
            mainDesignerField.destroy();
            mainDesignerField = null;
        }

        doRefresh($("#designerDiv"), true, function (err, form) {

            if (!err) {
                mainDesignerField = form;
            }

            if (callback) {
                callback();
            }

        });
    };

    // creates the components
    var afterAlpacaInit = function () {
        // available components
        var types = ["TEXT_TYPE", "CATEGORY_TYPE", "RATING_TYPE", "SCREENSHOT_TYPE", "ATTACHMENT_TYPE", "AUDIO_TYPE"];

        // do for all of the types mentioned above
        for (var i = 0; i < types.length; i++) {

            var type = types[i];
            var instance = new Alpaca.fieldClassRegistry[type]();

            var schemaSchema = instance.getSchemaOfSchema();
            var schemaOptions = instance.getOptionsForSchema();
            var optionsSchema = instance.getSchemaOfOptions();
            var optionsOptions = instance.getOptionsForOptions();
            var title = instance.getTitle();
            var type = instance.getType();
            var fieldType = instance.getFieldType();

            // component box
            var div = $("<div class='form-element draggable ui-widget-content' data-type='" + type + "' data-field-type='" + fieldType + "'></div>");
            $(div).append("<div><span class='form-element-title'>" + title + "</span></div>");
            $(div).append("<div><span class='form-element-description'>Dropable element</span></div>");

            $("#basic").append(div);

            // init all of the draggable form elements
            $(".form-element").draggable({
                "appendTo": "body",
                "helper": "clone",
                "zIndex": 300,
                "refreshPositions": true,
                "start": function (event, ui) {
                    $(".dropzone").addClass("dropzone-highlight");
                },
                "stop": function (event, ui) {
                    $(".dropzone").removeClass("dropzone-highlight");
                }
            });
        }
    };

    // lil hack to force compile
    $("<div></div>").alpaca({
        "data": "test",
        "postRender": function (control) {
            afterAlpacaInit();
        }
    });

    // add a component to the form
    var insertField = function (schema, options, dataType, fieldType, parentField, previousField, previousFieldKey, nextField, nextFieldKey) {
        var itemSchema = {
            "type": dataType
        };
        var itemOptions = {};
        if (fieldType) {
            itemOptions.type = fieldType;
        }
        itemOptions.label = "New ";
        if (fieldType) {
            itemOptions.label += fieldType.substring(0, fieldType.indexOf('_')).toLowerCase();
        }
        else if (dataType) {
            itemOptions.label += dataType;
        }
        var itemData = null;

        var itemKey = null;
        if (parentField.getType() === "array") {
            itemKey = 0;
            if (previousFieldKey) {
                itemKey = previousFieldKey + 1;
            }
        }
        else if (parentField.getType() === "object") {
            itemKey = "new" + new Date().getTime();
        }

        var insertAfterId = null;
        if (previousField) {
            insertAfterId = previousField.id;
        }

        parentField.addItem(itemKey, itemSchema, itemOptions, itemData, insertAfterId, function () {

            var top = findTop(parentField);

            regenerate(top);
        });

    };

    var assembleSchema = function (field, schema) {
        // copy any properties from this field's schema into our schema object
        for (var k in field.schema) {
            if (field.schema.hasOwnProperty(k) && typeof(field.schema[k]) !== "function") {
                schema[k] = field.schema[k];
            }
        }
        // a few that we handle by hand
        schema.type = field.getType();
        // reset properties, we handle that one at a time
        delete schema.properties;
        schema.properties = {};
        if (field.children) {
            for (var i = 0; i < field.children.length; i++) {
                var childField = field.children[i];
                var propertyId = childField.propertyId;

                schema.properties[propertyId] = {};
                assembleSchema(childField, schema.properties[propertyId]);
            }
        }
    };

    var assembleOptions = function (field, options) { // FIXME
        // copy any properties from this field's options into our options object
        for (var k in field.options) {
            if (field.options.hasOwnProperty(k) && typeof(field.options[k]) !== "function") {
                options[k] = field.options[k];
            }
        }
        // a few that we handle by hand
        options.type = field.getFieldType();

        // reset fields, we handle that one at a time
        delete options.fields;
        options.fields = {};

        if (field.children) {
            for (var i = 0; i < field.children.length; i++) {
                var childField = field.children[i];
                var propertyId = childField.propertyId;
                options.fields[propertyId] = {};
                assembleOptions(childField, options.fields[propertyId]);
            }
        }
    };

    var findTop = function (field) {
        // now get the top control
        var top = field;
        while (top.parent) {
            top = top.parent;
        }

        return top;
    };

    var regenerate = function (top) {
        // walk the control tree and re-assemble the schema, options + data
        var _schema = {};
        assembleSchema(top, _schema);
        var _options = {};
        assembleOptions(top, _options);

        editor1.setValue(JSON.stringify(_schema, null, "    "));
        editor2.setValue(JSON.stringify(_options, null, "    "));

        setTimeout(function () {
            refreshDesigner();
            refreshView();
        }, 100);
    };

    var removeField = function (alpacaId) {
        var field = Alpaca.fieldInstances[alpacaId];

        var parentField = field.parent;
        parentField.removeItem(field.propertyId, function () {
            var top = findTop(field);
            regenerate(top);
        });
    };

    refreshDesigner();
    refreshView();

    // click on save button (for developer who creates the form)
    $(".save-button").on("click", function () {
        var config = transformConfig(options.fields);
        var configString = JSON.stringify(config);
        console.log(configString);

        // PUT-REQUEST to orchestrator
/*
        var request = new XMLHttpRequest();

        request.open('PUT', 'http://ec2-54-89-190-92.compute-1.amazonaws.com/orchestrator/feedback/en/applications/');

        request.setRequestHeader('Authorization', '8c69087d-73c1-42dc-9a94-53e48967fc7a');
        request.setRequestHeader('Content-Type', 'application/json');

        request.onreadystatechange = function () {
            if (this.readyState === 4) {
                console.log('Status:', this.status);
                console.log('Headers:', this.getAllResponseHeaders());
                console.log('Body:', this.responseText);
                alert("Status: " + this.status + " " + this.statusText + "\nHeaders: " + this.getAllResponseHeaders() + "\nBody: " + this.responseText);
            }
        };

        request.send(configString);
*/
    });

    // click on send button (for user to send form data)
    $(".submit-button").on("click", function () {
        var submitText = $("#SubmitText").val();
        alert(submitText);
    });
};

$(document).ready(function () {
    // load everything before call the orchestrator
    setTimeout(function () {
        setup();
    }, 200);
});

// Configurations for Options-PopUp

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
    }
});
Alpaca.registerFieldClass("ATTACHMENT_TYPE", Alpaca.Fields.AttachmentMechanism);

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
        delete mySchema.properties.mandatory;
        delete mySchema.properties.type;
        return mySchema;
    }
});
Alpaca.registerFieldClass("AUDIO_TYPE", Alpaca.Fields.AudioMechanism);

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
    }
});
Alpaca.registerFieldClass("SCREENSHOT_TYPE", Alpaca.Fields.ScreenshotMechanism);

/// CATEGORY component extends the alpaca select
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
        delete myProp.properties.mandatory;
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
                "mandatory": { // not in uzh properties, note: alpaca documentation says "required", but its in fact "mandatory"
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
        delete myProp.properties.mandatory; // note: alpaca documentation says "required", but its in fact "mandatory"
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
                    "title": "Mandatory", // not in uzh properties, note: alpaca documentation says "required", but its in fact "mandatory"
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
    }
});
Alpaca.registerFieldClass("RATING_TYPE", Alpaca.Fields.RatingMechanism);

// TEXTINPUT component extends the alpaca textarea
$.alpaca.Fields.TextInputMechanism = $.alpaca.Fields.TextAreaField.extend({
    getFieldType: function(){
        return "TEXT_TYPE";
    },
    getTitle: function () {
        return "Text Mechanism";
    },

    getSchemaOfOptions: function () {
        var myProp = this.base();
        delete myProp.properties.allowOptionalEmpty;
        delete myProp.properties.autocomplete;
        delete myProp.properties.cols;
        delete myProp.properties.data;
        delete myProp.properties.disabled;
        delete myProp.properties.disallowEmptySpaces;
        delete myProp.properties.disallowOnlyEmptySpaces;
        delete myProp.properties.fieldClass;
        delete myProp.properties.focus;
        delete myProp.properties.helper;
        delete myProp.properties.helpers;
        delete myProp.properties.hidden;
        delete myProp.properties.hideInitValidationError;
        delete myProp.properties.inputType;
        delete myProp.properties.mandatory; // note: alpaca documentation says "required", but its in fact "mandatory"
        delete myProp.properties.maskString;
        delete myProp.properties.name;
        delete myProp.properties.optionLabels;
        delete myProp.properties.placeholder;
        delete myProp.properties.rows;
        delete myProp.properties.showMessages;
        delete myProp.properties.size;
        delete myProp.properties.typeahead;
        delete myProp.properties.validate;
        delete myProp.properties.view;
        delete myProp.properties.wordlimit;
        return Alpaca.merge(myProp, {
            "properties": {
                "mandatory": { // note: alpaca documentation says "required", but its in fact "mandatory"
                    "title": "Mandatory",
                    "type": "boolean",
                    "default": false
                },
                "hidden": {
                    "title": "Hidden",
                    "type": "boolean",
                    "default": false,
                    "description": "Field will be hidden if true."
                },
                "disabled": {
                    "title": "Disabled",
                    "type": "boolean",
                    "default": false,
                    "description": "Field will be disabled if true."
                },
                "rows": {
                    "title": "Rows",
                    "type": "number",
                    "default": "5",
                    "description": "Number of rows"
                },
                "fieldWidth": {
                    "title": "Input width",
                    "type": "number",
                    "default": "40",
                    "description": "Width of the text input field"
                },
                "maxLength": {
                    "title": "Maximum number of characters",
                    "type": "number",
                    "description": "Maximum number of characters allowed in the text input"
                },
                "maxLengthStrict": {
                    "title": "Strict length of character",
                    "type": "boolean",
                    "default": false,
                    "description": "Whether to prevent typing if max length is reached"
                },
                "maxLengthVisible": {
                    "title": "Display max number of characters",
                    "type": "boolean",
                    "default": false,
                    "description": "If the maximum number of allowed characters is visible in the text input field"
                },
                "textLengthVisible": {
                    "title": "Display character counter",
                    "type": "boolean",
                    "default": true,
                    "description": "If the current characters counter is visible in the text input field"
                },
                "hint": {
                    "title": "Tooltip",
                    "type": "string",
                    "description": "Hint of the field"
                }
            }
        });
    },

    getSchemaOfSchema: function(){
        var mySchema = this.base();
        delete mySchema.properties.default;
        delete mySchema.properties.disallow;
        delete mySchema.properties.enum;
        delete mySchema.properties.format;
        delete mySchema.properties.minLength;
        delete mySchema.properties.pattern;
        delete mySchema.properties.readonly;
        delete mySchema.properties.type;
        return mySchema;
    }
});
Alpaca.registerFieldClass("TEXT_TYPE", Alpaca.Fields.TextInputMechanism);
