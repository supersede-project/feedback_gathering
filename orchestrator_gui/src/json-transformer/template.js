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
            as: {
                mechanisms: {
                    path: "options.fields",
   /*                 choose: function(node, value, key) {
                        if (key.indexOf("new") >= 0) {
                            return key;
                        }
                    },
*/
//                    nested: true,
                    as: {
                        type: "type",
                        label: "label"
                    }

                    // all: true

                    /*
                     choose: ["type", "label"],
                     as: {
                     type: "type",
                     label: "label"
                     }
                     */

                }
            }
        }
    }

    /* Lollys iplementations
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
    } */
};