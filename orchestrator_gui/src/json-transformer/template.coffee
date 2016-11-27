tmpl =
  path: '.'
  as:
    configurations:
      mechanisms:
        path: 'schema'
        as:
          key: 'properties'
          type: 'options.fields.type'

new ObjectTyplate(templ).transform data
