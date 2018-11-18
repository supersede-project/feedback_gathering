import {CategoryMechanism} from './category_mechanism';
import {readJSON} from '../../services/mocks/mocks_loader';
import {MechanismFactory} from './mechanism_factory';
import {CategoryFeedback} from '../feedbacks/category_feedback';


describe('Category Mechanism', () => {
    let categoryMechanism:CategoryMechanism;
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
    });

    it('should return its options', () => {
        var options = categoryMechanism.getOptions();

        expect(options.length).toBe(3);
        expect(options[0].key).toEqual('BUG_CATEGORY');
        expect(options[0].value).toEqual('Bug');
        expect(options[1].key).toEqual('FEATURE_REQUEST_CATEGORY');
        expect(options[1].value).toEqual('Feature Request');
        expect(options[2].key).toEqual('GENERAL_CATEGORY');
        expect(options[2].value).toEqual('General Feedback');
    });

    it('should return its category feedback object', () => {
        $('#optionFEATURE_REQUEST_CATEGORY').prop("checked", true);

        var categoryFeedbacks:CategoryFeedback[] = categoryMechanism.getCategoryFeedbacks();
        var json = JSON.stringify(categoryFeedbacks);
        var expectedJson = '[{"parameterId":2,"text":""}]';

        expect(json).toEqual(expectedJson);
    });
});
