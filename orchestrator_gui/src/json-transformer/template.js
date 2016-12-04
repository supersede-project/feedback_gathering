var tmpl = {
    path: '.',
    aggregate: {
        total: function(key, value, existing) {
            if (!isArray(value)) {
                return value;
            } else {
                return value.sort().reverse()[0];
            }
        },
        pages: function(key, value, existing) {
            if (!isArray(value)) {
                return value;
            } else {
                return value.sort().reverse()[0];
            }
        }
    },
    as: {
        configurations: {

            path: 'options.fields',
            choose: function (node, value, key) {
                return key;
            },
            format: function (node, value, key) {
                return {
                    key: "mechanisms",
                    value: key.replace(/new/, '')
                };
            },
            nested: true,
            as: {
                type: 'type',
                parameters: {
                    as: {
                        title: "label"
                    }

                }
            },
            mechanisms2: {
                path: 'options'
            }

        }
    }
};