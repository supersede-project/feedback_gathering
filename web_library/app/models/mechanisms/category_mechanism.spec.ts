import {CategoryMechanism} from './category_mechanism';
import {readJSON} from '../../services/mocks/mocks_loader';
import {MechanismFactory} from './mechanism_factory';
import {CategoryFeedback} from '../feedbacks/category_feedback';


fdescribe('Category Mechanism', () => {
    let categoryMechanism:CategoryMechanism;
    let $ = $j;

    beforeAll(() => {
        var html = '<section id="categoryMechanism992" class="feedback-mechanism horizontal category-type">' +
            '<p>Please choose from the following categories</p>' +
            '<input id="optionBUG_CATEGORY" type="radio" value="BUG_CATEGORY" name="category_mechanism_992">' +
            '<label for="optionBUG_CATEGORY"> Bug</label>' +
            '<input id="optionFEATURE_REQUEST_CATEGORY" type="radio" value="FEATURE_REQUEST_CATEGORY" name="category_mechanism_992">' +
            '<label for="optionFEATURE_REQUEST_CATEGORY"> Feature Request</label>' +
            '<input id="optionGENERAL_CATEGORY" type="radio" value="GENERAL_CATEGORY" name="category_mechanism_992">' +
            '<label for="optionGENERAL_CATEGORY"> General Feedback</label>' +
            '</section>';
        $('body').append(html);
    });

    beforeEach(() => {
        var categoryMechanismData = readJSON('app/services/mocks/category_mechanism_mock.json', '/base/');
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

        var categoryFeedback:CategoryFeedback = categoryMechanism.getCategoryFeedback();
        var json = JSON.stringify(categoryFeedback);
        var expectedJson = '{"mechanismId":992,"categories":[{"mechanismId":992,"text":null,"categoryType":{"key":"FEATURE_REQUEST_CATEGORY","text":"Feature Request"}}]}';

        expect(json).toEqual(expectedJson);
    });
});

