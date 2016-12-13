function transformConfig(configuration) {
    var config = {};
    var configFields = [];
    var id = 0;

    $.each(configuration, function(key, value){
        var component = {};
        component.type = value.type;
        component.active = true; // new
        component.order = ++id; // new
        component.canBeActivated = false; // new
        var params = [];
        $.each(value, function(key, value){
            var keyValProp = {};
            if (key == "label"){
                keyValProp.key = "title";
            } else {
                keyValProp.key = key;
            }

            keyValProp.value = value;
            params.push(keyValProp);
        });
        component.parameters = params;
        configFields.push(component);
    });

    var mechanismsObject = {};
    mechanismsObject.type = "PUSH";
    mechanismsObject.mechanisms = configFields;

    config.configurations = [];
    config.configurations.push(mechanismsObject);

    /* general config */
    config.id = 12;
    config.state = 1;
    config.generalConfiguration = {};
    config.generalConfiguration.parameters = [];

    var parameters = [];
    var labelFontColorParam = {};
    labelFontColorParam.key = "labelFontColor";
    labelFontColorParam.value = labelFontColor;
    parameters.push(labelFontColorParam);

    labelFontTypeParam = {};
    labelFontTypeParam.key = "labelFontType";
    labelFontTypeParam.value = labelFontType;
    parameters.push(labelFontTypeParam);

    labelFontSizeParam = {};
    labelFontSizeParam.key = "labelFontSize";
    labelFontSizeParam.value = labelFontSize;
    parameters.push(labelFontSizeParam);

    submitTextParam = {};
    submitTextParam.key = "submitText";
    submitTextParam.value = submitText;
    parameters.push(submitTextParam);

    config.generalConfiguration.parameters = parameters;
    console.log(config);

    return config;
}