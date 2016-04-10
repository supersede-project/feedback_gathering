var Parameter = (function () {
    function Parameter(key, value, defaultValue, editableByUSer) {
        this.key = key;
        this.value = value;
        this.defaultValue = defaultValue;
        this.editableByUSer = editableByUSer;
    }
    return Parameter;
})();
exports.Parameter = Parameter;