import {CategoryMechanism} from './category_mechanism';
import {readJSON} from '../../services/mocks/mocks_loader';
import {MechanismFactory} from './mechanism_factory';


describe('Category Mechanism', () => {
    let categoryMechanism:CategoryMechanism;

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
});

