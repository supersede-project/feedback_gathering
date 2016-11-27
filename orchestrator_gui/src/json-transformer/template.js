    var tmpl;

    tmpl = {
        path: '.',
        as: {
            configurations: {
                mechanisms: {
                    path: 'schema',
                    as: {
                        key: 'properties',
                        type: 'options.fields.type'
                    }
                }
            }
        }
    };
    new ObjectTemplate(tmpl).transform("GUI_schema_options.json");
