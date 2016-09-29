define(["require", "exports", '../../services/mocks/mocks_loader', './mechanism_factory'], function (require, exports, mocks_loader_1, mechanism_factory_1) {
    "use strict";
    describe('Category Mechanism', function () {
        var categoryMechanism;
        var $ = $j;
        beforeAll(function () {
            var html = '<section id="categoryMechanism992" class="feedback-mechanism horizontal category-type">' +
                '<p>Please choose from the following categories</p>' +
                '<input id="optionBUG_CATEGORY" data-parameter-id="1" type="radio" value="BUG_CATEGORY" name="category_mechanism_992">' +
                '<label for="optionBUG_CATEGORY"> Bug</label>' +
                '<input id="optionFEATURE_REQUEST_CATEGORY" data-parameter-id="2" type="radio" value="FEATURE_REQUEST_CATEGORY" name="category_mechanism_992">' +
                '<label for="optionFEATURE_REQUEST_CATEGORY"> Feature Request</label>' +
                '<input id="optionGENERAL_CATEGORY" data-parameter-id="3" type="radio" value="GENERAL_CATEGORY" name="category_mechanism_992">' +
                '<label for="optionGENERAL_CATEGORY"> General Feedback</label>' +
                '<label for="ownInput1">Other:</label>' +
                '<input class="own-category" id="ownInput1" type="text" />' +
                '</section>';
            $('body').append(html);
        });
        beforeEach(function () {
            var categoryMechanismData = mocks_loader_1.readJSON('app/services/mocks/test/category_mechanism_mock.json', '/base/');
            categoryMechanism = mechanism_factory_1.MechanismFactory.createByData(categoryMechanismData);
        });
        it('should return its options', function () {
            var options = categoryMechanism.getOptions();
            expect(options.length).toBe(3);
            expect(options[0].key).toEqual('BUG_CATEGORY');
            expect(options[0].value).toEqual('Bug');
            expect(options[1].key).toEqual('FEATURE_REQUEST_CATEGORY');
            expect(options[1].value).toEqual('Feature Request');
            expect(options[2].key).toEqual('GENERAL_CATEGORY');
            expect(options[2].value).toEqual('General Feedback');
        });
        it('should return its category feedback object', function () {
            $('#optionFEATURE_REQUEST_CATEGORY').prop("checked", true);
            var categoryFeedbacks = categoryMechanism.getCategoryFeedbacks();
            var json = JSON.stringify(categoryFeedbacks);
            var expectedJson = '[{"parameterId":2,"text":""}]';
            expect(json).toEqual(expectedJson);
        });
        it('should coordinate the behaviour between radio boxes and the text input for the own category', function () {
            categoryMechanism.coordinateOwnInputAndRadioBoxes();
            var featureRequestRadio = $('#optionFEATURE_REQUEST_CATEGORY');
            var bugRadio = $('#optionBUG_CATEGORY');
            var myOwnInput = $('#ownInput1');
            featureRequestRadio.prop("checked", true);
            expect(myOwnInput.val()).toEqual("");
            myOwnInput.val('My category');
            myOwnInput.trigger("change");
            expect(featureRequestRadio.is(':checked')).toBeFalsy();
            bugRadio.trigger("click");
            expect(myOwnInput.val()).toEqual("");
            myOwnInput.val('My category');
            myOwnInput.trigger("change");
            expect(bugRadio.is(':checked')).toBeFalsy();
            featureRequestRadio.trigger("click");
            expect(myOwnInput.val()).toEqual("");
        });
    });
});
//# sourceMappingURL=category_mechanism.spec.js.map