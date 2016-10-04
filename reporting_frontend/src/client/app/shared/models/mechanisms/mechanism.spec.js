define(["require", "exports", './mechanism'], function (require, exports, mechanism_1) {
    "use strict";
    describe('Mechanism', function () {
        it('should return the corresponding parameter object', function () {
            var parameters = [{ key: 'maxLength', value: 100 }, { key: 'title', value: 'Feedback' }, { key: 'hint', value: 'Enter your feedback' }];
            var mechanism = new mechanism_1.Mechanism(1, 'TEXT_TYPE', true, 1, true, parameters);
            expect(mechanism.getParameter('maxLength').value).toEqual(100);
            expect(mechanism.getParameter('title').value).toEqual('Feedback');
            expect(mechanism.getParameter('hint').value).toEqual('Enter your feedback');
        });
        it('should return the corresponding parameter value or null', function () {
            var parameters = [{ key: 'maxLength', value: 100 }, { key: 'title', value: 'Feedback' }, { key: 'hint', value: 'Enter your feedback' }];
            var mechanism = new mechanism_1.Mechanism(2, 'TEXT_TYPE', true, 1, true, parameters);
            expect(mechanism.getParameterValue('maxLength')).toEqual(100);
            expect(mechanism.getParameterValue('title')).toEqual('Feedback');
            expect(mechanism.getParameterValue('hint')).toEqual('Enter your feedback');
            expect(mechanism.getParameterValue('notExistingParameter')).toBeNull();
        });
    });
});
//# sourceMappingURL=mechanism.spec.js.map