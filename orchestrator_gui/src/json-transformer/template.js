    var tmpl;

    tmpl = {
        path: '.',
        as: {
            configurations: {
                as: {
                    type: 'schema.type'
                },
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
