import {validatePluginModule} from './jquery.validate';


describe('jQuery Validate Plugin', () => {
    let elementMandatory:any;
    let elementMaxLength:any;
    let $ = $j;

    beforeEach(() => {
        validatePluginModule($, window, document);

        elementMandatory = $(
            '<textarea class="validate" id="textTypeText" data-mandatory="1" data-mandatory-validate-on-skip="0" ' +
            'data-mandatory-default-text="This field can\'t be blank" ' +
            'data-mandatory-manual-text="Please fill in this field" ></textarea>');

        elementMaxLength = $(
            '<textarea class="validate" data-validation-max-length="30"></textarea>');
    });

    it('should validate an element if it is mandatory', () => {
        expect(elementMandatory.val()).toBe('');
        expect(elementMandatory.hasClass('validate')).toBeTruthy();
        expect(elementMandatory.hasClass('invalid')).toBeFalsy();

        elementMandatory.validate();
        expect(elementMandatory.hasClass('invalid')).toBeTruthy();

        elementMandatory.val('The textarea content is set now');
        elementMandatory.validate();
        expect(elementMandatory.hasClass('invalid')).toBeFalsy();
    });

    it('should validate an element if it has a max length set', () => {
        expect(elementMaxLength.val()).toBe('');
        expect(elementMaxLength.hasClass('validate')).toBeTruthy();
        expect(elementMaxLength.hasClass('invalid')).toBeFalsy();

        elementMaxLength.validate();
        expect(elementMaxLength.hasClass('invalid')).toBeFalsy();

        elementMaxLength.val('Lorem ipsum dolor sit amet, con');
        elementMaxLength.validate();
        expect(elementMaxLength.hasClass('invalid')).toBeTruthy();

        elementMaxLength.val('Lorem ipsum dolor sit amet, co');
        elementMaxLength.validate();
        expect(elementMaxLength.hasClass('invalid')).toBeFalsy();
    });
});

