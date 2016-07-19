define(["require", "exports", './utils/template_locals', './utils/tasks_tools'], function (require, exports, template_locals_1, tasks_tools_1) {
    "use strict";
    function __export(m) {
        for (var p in m) if (!exports.hasOwnProperty(p)) exports[p] = m[p];
    }
    __export(template_locals_1);
    __export(tasks_tools_1);
    function tsProjectFn(plugins) {
        return plugins.typescript.createProject('tsconfig.json', {
            typescript: require('typescript')
        });
    }
    exports.tsProjectFn = tsProjectFn;
});
//# sourceMappingURL=utils.js.map