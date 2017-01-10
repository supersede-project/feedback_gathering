import {CategoryMechanism} from '../../models/mechanisms/category_mechanism';
import {CategoryView} from './category_view';
import {readJSON} from '../../services/mocks/mocks_loader';
import {MechanismFactory} from '../../models/mechanisms/mechanism_factory';
import {CategoryFeedback} from '../../models/feedbacks/category_feedback';


/*
describe('Category View', () => {
    let categoryMechanism:CategoryMechanism;
    let categoryView:CategoryView;
    let $ = $j;

    beforeAll(() => {
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

    beforeEach(() => {
        var categoryMechanismData = readJSON('app/services/mocks/test/category_mechanism_mock.json', '/base/');
        categoryMechanism = <CategoryMechanism>MechanismFactory.createByData(categoryMechanismData);
        categoryView = new CategoryView(categoryMechanism);
    });

    it('should return its category feedback object', () => {
        $('#optionFEATURE_REQUEST_CATEGORY').prop("checked", true);

        var categoryFeedbacks:CategoryFeedback[] = categoryView.getCategoryFeedbacks();
        var json = JSON.stringify(categoryFeedbacks);
        var expectedJson = '[{"parameterId":2,"text":""}]';

        expect(json).toEqual(expectedJson);
    });

    it('should coordinate the behaviour between radio boxes and the text input for the own category', () => {
        categoryView.coordinateOwnInputAndRadioBoxes();

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

*/