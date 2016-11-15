var setup = function()
{
    //Alpaca.logLevel = Alpaca.DEBUG;

    var MODAL_VIEW = "bootstrap-edit-horizontal";
    //var MODAL_VIEW = "bootstrap-edit";

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

    var schema = {
        "type": "object"
    };
    var options = {
    };
    var data = {
    };

    var setupEditor = function(id, json)
    {
        var text = "";
        if (json)
        {
            text = JSON.stringify(json, null, "    ");
        }

        var editor = ace.edit(id);
        editor.setTheme("ace/theme/textmate");
        if (json)
        {
            editor.getSession().setMode("ace/mode/json");
        }
        else
        {
            editor.getSession().setMode("ace/mode/javascript");
        }
 //       editor.renderer.setHScrollBarAlwaysVisible(false);
 //       editor.setShowPrintMargin(false);
 //       editor.setValue(text);

        setTimeout(function() {
            editor.clearSelection();
            editor.gotoLine(0,0);
        }, 100);

        return editor;
    };

    var editor1 = setupEditor("schema", schema);
    var editor2 = setupEditor("options", options);
    var editor3 = setupEditor("data", data);

    var mainViewField = null;
    var mainDesignerField = null;

    var doRefresh = function(el, buildInteractionLayers, disableErrorHandling, cb)
    {
        try
        {
            schema = JSON.parse(editor1.getValue());
        }
        catch (e)
        {
        }

        try
        {
            options = JSON.parse(editor2.getValue());
        }
        catch (e)
        {
        }

        try
        {
            data = JSON.parse(editor3.getValue());
        }
        catch (e)
        {
        }

        if (schema)
        {
            var config = {
                "schema": schema
            };
            if (options)
            {
                config.options = options;
            }
            if (data)
            {
                config.data = data;
            }
            if (!config.options) {
                config.options = {};
            }
            config.options.focus = false;
            config.postRender = function(form) {

                if (buildInteractionLayers)
                {
                    var iCount = 0;

                    // cover every control with an interaction layer
                    form.getFieldEl().find(".alpaca-container-item").each(function() {

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
                        $(optionsButton).off().click(function(e) {

                            e.preventDefault();
                            e.stopPropagation();

                            var alpacaId = $(this).attr("alpaca-ref-id");

                            editOptions(alpacaId);
                        });

                        // click on removeButton
                        $(removeButton).off().click(function(e) {

                            e.preventDefault();
                            e.stopPropagation();

                            var alpacaId = $(this).attr("alpaca-ref-id");
                            removeField(alpacaId);
                        });

                        // when hover, highlight
                        $(interaction).hover(function(e) {
                            var iCount = $(interaction).attr("icount-ref");
                            $(".cover[icount-ref='" + iCount + "']").addClass("ui-hover-state");
                        }, function(e) {
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
                    form.getFieldEl().find(".alpaca-container").each(function() {
                        var containerEl = this;

                        // first insertion point
                        $(this).prepend("<div class='dropzone'></div>");

                        // all others
                        $(containerEl).children(".alpaca-container-item").each(function() {
                            $(this).after("<div class='dropzone'></div>");
                        });

                    });

                    form.getFieldEl().find(".dropzone").droppable({
                        "tolerance": "touch",
                        "drop": function( event, ui ) {

                            var draggable = $(ui.draggable);

                            if (draggable.hasClass("form-element"))
                            {
                                var dataType = draggable.attr("data-type");
                                var fieldType = draggable.attr("data-field-type");

                                // based on where the drop occurred, figure out the previous and next Alpaca fields surrounding
                                // the drop target

                                // previous
                                var previousField = null;
                                var previousFieldKey = null;
                                var previousItemContainer = $(event.target).prev();
                                if (previousItemContainer)
                                {
                                    var previousAlpacaId = $(previousItemContainer).children().first().attr("data-alpaca-field-id");
                                    previousField = Alpaca.fieldInstances[previousAlpacaId];

                                    previousFieldKey = $(previousItemContainer).attr("data-alpaca-container-item-name");
                                }

                                // next
                                var nextField = null;
                                var nextFieldKey = null;
                                var nextItemContainer = $(event.target).next();
                                if (nextItemContainer)
                                {
                                    var nextAlpacaId = $(nextItemContainer).children().first().attr("data-alpaca-field-id");
                                    nextField = Alpaca.fieldInstances[nextAlpacaId];

                                    nextFieldKey = $(nextItemContainer).attr("data-alpaca-container-item-name");
                                }

                                // parent field
                                var parentFieldAlpacaId = $(event.target).parent().parent().attr("data-alpaca-field-id");
                                var parentField = Alpaca.fieldInstances[parentFieldAlpacaId];

                                // now do the insertion
                                insertField(schema, options, data, dataType, fieldType, parentField, previousField, previousFieldKey, nextField, nextFieldKey);
                            }
                            else if (draggable.hasClass("interaction"))
                            {
                                var draggedIndex = $(draggable).attr("icount-ref");

                                // next
                                var nextItemContainer = $(event.target).next();
                                var nextItemContainerIndex = $(nextItemContainer).attr("data-alpaca-container-item-index");
                                var nextItemAlpacaId = $(nextItemContainer).children().first().attr("data-alpaca-field-id");
                                var nextField = Alpaca.fieldInstances[nextItemAlpacaId];

                                form.moveItem(draggedIndex, nextItemContainerIndex, false, function() {

                                    var top = findTop(nextField);
                                    regenerate(top);
                                });
                            }
                        },
                        "over": function (event, ui ) {
                            $(event.target).addClass("dropzone-hover");
                        },
                        "out": function (event, ui) {
                            $(event.target).removeClass("dropzone-hover");
                        }
                    });

                    // init any in-place draggables
                    form.getFieldEl().find(".interaction").draggable({
                        "appendTo": "body",
                        "helper": function() {
                            var iCount = $(this).attr("icount-ref");
                            var clone = $(".alpaca-container-item[icount='" + iCount + "']").clone();
                            return clone;
                        },
                        "cursorAt": {
                            "top": 100
                        },
                        "zIndex": 300,
                        "refreshPositions": true,
                        "start": function(event, ui) {
                            $(".dropzone").addClass("dropzone-highlight");
                        },
                        "stop": function(event, ui) {
                            $(".dropzone").removeClass("dropzone-highlight");
                        }
                    });
                }

                cb(null, form);
            };
            config.error = function(err)
            {
                Alpaca.defaultErrorCallback(err);

                cb(err);
            };

            if (disableErrorHandling)
            {
                Alpaca.defaultErrorCallback = function(error) {
                    console.log("Alpaca encountered an error while previewing form -> " + error.message);
                };
            }
            else
            {
                Alpaca.defaultErrorCallback = Alpaca.DEFAULT_ERROR_CALLBACK;
            }

            $(el).alpaca(config);
        }
    };

    var removeFunctionFields = function(schema, options)
    {
        if (schema)
        {
            if (schema.properties)
            {
                var badKeys = [];

                for (var k in schema.properties)
                {
                    if (schema.properties[k].type === "function")
                    {
                        badKeys.push(k);
                    }
                    else
                    {
                        removeFunctionFields(schema.properties[k], (options && options.fields ? options.fields[k] : null));
                    }
                }

                for (var i = 0; i < badKeys.length; i++)
                {
                    delete schema.properties[badKeys[i]];

                    if (options && options.fields) {
                        delete options.fields[badKeys[i]];
                    }
                }
            }
        }
    };

    var editOptions = function(alpacaFieldId, callback)
    {
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
        if (fieldSchemaSchema.properties)
        {
            delete fieldSchemaSchema.properties.title;
            delete fieldSchemaSchema.properties.description;
            delete fieldSchemaSchema.properties.dependencies;
        }
        delete fieldOptionsSchema.title;
        delete fieldOptionsSchema.description;
        if (fieldOptionsSchema.properties)
        {
            delete fieldOptionsSchema.properties.title;
            delete fieldOptionsSchema.properties.description;
            delete fieldOptionsSchema.properties.dependencies;
            delete fieldOptionsSchema.properties.readonly;
        }

        if (fieldOptionsOptions.fields)
        {
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
        if (fieldSchemaOptions)
        {

            fieldConfigSchema.options = fieldSchemaOptions;
        }
        if (fieldData)
        {
            fieldConfigSchema.data = fieldData;
        }
        if (fieldOptionsOptions)
        {
            fieldConfigOptions.options = fieldOptionsOptions;
        }
        if (fieldOptionsData)
        {
            // set validation messages to false
            fieldOptionsData.showMessages = false;
            fieldConfigOptions.data = fieldOptionsData;
        }
        fieldConfigSchema.view = {
            "parent": MODAL_VIEW,
            "displayReadonly": false
        };
        fieldConfigOptions.view = {
            "parent": MODAL_VIEW,
            "displayReadonly": false
        };
        fieldConfigSchema.postRender = function(control)
        {
            var modal = $(MODAL_TEMPLATE.trim());
            modal.find(".modal-title").append(field.getTitle());
            modal.find(".modal-body").append(control.getFieldEl());

            modal.find('.modal-footer').append("<button class='btn btn-primary pull-right okay' data-dismiss='modal' aria-hidden='true'>Okay</button>");
            modal.find('.modal-footer').append("<button class='btn btn-default pull-left' data-dismiss='modal' aria-hidden='true'>Cancel</button>");

            $(modal).modal({
                "keyboard": true
            });

            $(modal).find(".okay").click(function() {

                field.schema = control.getValue();

                var top = findTop(field);
                regenerate(top);

                if (callback)
                {
                    callback();
                }
            });

            control.getFieldEl().find("p.help-block").css({
                "display": "none"
            });

            // hide unused config options TODO: better not implement the fields at all
            control.getFieldEl().find("[data-alpaca-container-item-name='readonly']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='default']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='type']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='format']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='disallow']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='enum']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='minLength']").css({
                "display": "none"
            });
 /*           control.getFieldEl().find("[data-alpaca-container-item-name='maxLength']").css({
                "display": "none"
            });
  */          control.getFieldEl().find("[data-alpaca-container-item-name='pattern']").css({
                "display": "none"
            });
        };
        fieldConfigOptions.postRender = function(control)
        {
            var modal = $(MODAL_TEMPLATE.trim());
            modal.find(".modal-title").append(field.getTitle());
            modal.find(".modal-body").append(control.getFieldEl());

            modal.find('.modal-footer').append("<button class='btn btn-primary pull-right okay' data-dismiss='modal' aria-hidden='true'>Okay</button>");
            modal.find('.modal-footer').append("<button class='btn btn-default pull-left' data-dismiss='modal' aria-hidden='true'>Cancel</button>");

            $(modal).modal({
                "keyboard": true
            });

            $(modal).find(".okay").click(function() {

                field.options = control.getValue();

                var top = findTop(field);
                regenerate(top);

                if (callback)
                {
                    callback();
                }
            });

            control.getFieldEl().find("p.help-block").css({
                "display": "none"
            });

            // hide unused config options TODO: better not implement the fields at all
            control.getFieldEl().find("[data-alpaca-container-item-name='data']").css({
                "display": "none"
            });

            control.getFieldEl().find("[data-alpaca-container-item-name='autocomplete']").css({
                "display": "none"
            });

            control.getFieldEl().find("[data-alpaca-container-item-name='disallowEmptySpaces']").css({
                "display": "none"
            });

            control.getFieldEl().find("[data-alpaca-container-item-name='validate']").css({
                "display": "none"
            });

            control.getFieldEl().find("[data-alpaca-container-item-name='showMessages']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='helper']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='helpers']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='fieldClass']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='hideInitValidationError']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='focus']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='optionLabels']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='view']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='name']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='size']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='maskString']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='placeholder']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='typeahead']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='allowOptionalEmpty']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='inputType']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='disallowOnlyEmptySpaces']").css({
                "display": "none"
            });
            // additional for checkbox
            control.getFieldEl().find("[data-alpaca-container-item-name='wordlimit']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='dataSource']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='useDataSourceAsEnum']").css({
                "display": "none"
            });
            // addition for radio
            control.getFieldEl().find("[data-alpaca-container-item-name='removeDefaultNone']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='noneLabel']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='hideNone']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='emptySelectFirst']").css({
                "display": "none"
            });
            control.getFieldEl().find("[data-alpaca-container-item-name='vertical']").css({
                "display": "none"
            });
        };

        // finds the div with fielForm class and uses it as alpaca form for the schema and option form
        var x = $("<div><div class='fieldForm'></div></div>");
        $(x).find(".fieldForm").alpaca(fieldConfigSchema);
        $(x).find(".fieldForm").alpaca(fieldConfigOptions);

    };

    var refreshView = function(callback)
    {
        if (mainViewField)
        {
            mainViewField.getFieldEl().replaceWith("<div id='viewDiv'></div>");
            mainViewField.destroy();
            mainViewField = null;
        }

        doRefresh($("#viewDiv"), false, false, function(err, form) {

            if (!err)
            {
                mainViewField = form;
            }

            if (callback)
            {
                callback();
            }

        });
    };

    var refreshDesigner = function(callback)
    {
        $(".dropzone").remove();
        $(".interaction").remove();
        $(".cover").remove();

        if (mainDesignerField)
        {
            mainDesignerField.getFieldEl().replaceWith("<div id='designerDiv'></div>");
            mainDesignerField.destroy();
            mainDesignerField = null;
        }

        doRefresh($("#designerDiv"), true, false, function(err, form) {

            if (!err)
            {
                mainDesignerField = form;
            }

            if (callback)
            {
                callback();
            }

        });
    };

    // creates the components
    var afterAlpacaInit = function()
    {
        // available components
        var types = ["textarea", "checkbox", "radio", "image", "upload"];

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

            var div = $("<div class='form-element draggable ui-widget-content' data-type='" + type + "' data-field-type='" + fieldType + "'></div>");
            $(div).append("<div><span class='form-element-title'>" + title + "</span> (<span class='form-element-type'>" + fieldType + "</span>)</div>");
            $(div).append("<div><span class='form-element-description'>Dropable element</span></div>");

            $("#basic").append(div);

            // init all of the draggable form elements
            $(".form-element").draggable({
                "appendTo": "body",
                "helper": "clone",
                "zIndex": 300,
                "refreshPositions": true,
                "start": function(event, ui) {
                    $(".dropzone").addClass("dropzone-highlight");
                },
                "stop": function(event, ui) {
                    $(".dropzone").removeClass("dropzone-highlight");
                }
            });
        }
    };

    // lil hack to force compile
    $("<div></div>").alpaca({
        "data": "test",
        "postRender": function(control)
        {
            afterAlpacaInit();
        }
    });

    var insertField = function(schema, options, data, dataType, fieldType, parentField, previousField, previousFieldKey, nextField, nextFieldKey)
    {
        var itemSchema = {
            "type": dataType
        };
        var itemOptions = {};
        if (fieldType)
        {
            itemOptions.type = fieldType;
        }
        itemOptions.label = "New ";
        if (fieldType)
        {
            itemOptions.label += fieldType;
        }
        else if (dataType)
        {
            itemOptions.label += dataType;
        }
        var itemData = null;

        var itemKey = null;
        if (parentField.getType() === "array")
        {
            itemKey = 0;
            if (previousFieldKey)
            {
                itemKey = previousFieldKey + 1;
            }
        }
        else if (parentField.getType() === "object")
        {
            itemKey = "new" + new Date().getTime();
        }

        var insertAfterId = null;
        if (previousField)
        {
            insertAfterId = previousField.id;
        }

        parentField.addItem(itemKey, itemSchema, itemOptions, itemData, insertAfterId, function() {

            var top = findTop(parentField);

            regenerate(top);
        });

    };

    var assembleSchema = function(field, schema)
    {
        // copy any properties from this field's schema into our schema object
        for (var k in field.schema)
        {
            if (field.schema.hasOwnProperty(k) && typeof(field.schema[k]) !== "function")
            {
                schema[k] = field.schema[k];
            }
        }
        // a few that we handle by hand
        schema.type = field.getType();
        // reset properties, we handle that one at a time
        delete schema.properties;
        schema.properties = {};
        if (field.children)
        {
            for (var i = 0; i < field.children.length; i++)
            {
                var childField = field.children[i];
                var propertyId = childField.propertyId;

                schema.properties[propertyId] = {};
                assembleSchema(childField, schema.properties[propertyId]);
            }
        }
    };

    var assembleOptions = function(field, options)
    {
        // copy any properties from this field's options into our options object
        for (var k in field.options)
        {
            if (field.options.hasOwnProperty(k) && typeof(field.options[k]) !== "function")
            {
                options[k] = field.options[k];
            }
        }
        // a few that we handle by hand
        options.type = field.getFieldType();
        // reset fields, we handle that one at a time
        delete options.fields;
        options.fields = {};
        if (field.children)
        {
            for (var i = 0; i < field.children.length; i++)
            {
                var childField = field.children[i];
                var propertyId = childField.propertyId;

                options.fields[propertyId] = {};
                assembleOptions(childField, options.fields[propertyId]);
            }
        }
    };

    var findTop = function(field)
    {
        // now get the top control
        var top = field;
        while (top.parent)
        {
            top = top.parent;
        }

        return top;
    };

    var regenerate = function(top)
    {
        // walk the control tree and re-assemble the schema, options + data
        var _schema = {};
        assembleSchema(top, _schema);
        var _options = {};
        assembleOptions(top, _options);
        // data is easy
        var _data = top.getValue();
        if (!_data) {
            _data = {};
        }

        editor1.setValue(JSON.stringify(_schema, null, "    "));
        editor2.setValue(JSON.stringify(_options, null, "    "));
        editor3.setValue(JSON.stringify(_data, null, "    "));

        setTimeout(function() {
            refreshDesigner();
            refreshView();
        }, 100);
    };

    var removeField = function(alpacaId)
    {
        var field = Alpaca.fieldInstances[alpacaId];

        var parentField = field.parent;
        parentField.removeItem(field.propertyId, function() {
            var top = findTop(field);
            regenerate(top);
        });
    };

    refreshDesigner();
    refreshView();

    // save button
    $(".save-button").on("click", function() {
    var config = {
            "schema": schema
        };

        if (schema)
        {
            config.schema = schema;
        }
        if (options)
        {
            config.options = options;
        }

        var configString = JSON.stringify(config);

        // Save JSON-String to local JSON-file
        var blob = new Blob([configString], {type: "application/json"});
        var saveAs = window.saveAs;
        saveAs(blob, "GUI_schema_options.json");

    });
};

$(document).ready(function() {

    // wait a bit to allow ACE to load
    setTimeout(function() {
        setup();
    }, 200);
});



