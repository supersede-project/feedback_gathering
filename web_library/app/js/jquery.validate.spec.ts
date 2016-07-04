import {validatePluginModule} from './jquery.validate';


describe('jQuery Validate Plugin', () => {
    let element:any;
    let $ = $j;

    beforeEach(() => {
        validatePluginModule($, window, document);

        element = $(
            '<textarea class="validate" id="textTypeText" data-mandatory="1" data-mandatory-validate-on-skip="0" ' +
            'data-mandatory-default-text="This field can\'t be blank" ' +
            'data-mandatory-manual-text="Please fill in this field" ></textarea>');
    });

    it('should validate an element', () => {
        expect(element.val()).toBe('');
        expect(element.hasClass('validate')).toBeTruthy();
        expect(element.hasClass('invalid')).toBeFalsy();

        element.validate();
        expect(element.hasClass('invalid')).toBeTruthy();

        element.val('The textarea content is set now');
        element.validate();
        expect(element.hasClass('invalid')).toBeFalsy();
    });
});

