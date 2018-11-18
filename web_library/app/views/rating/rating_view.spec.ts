import { RatingView } from './rating_view';
import { RatingMechanism } from '../../models/mechanisms/rating_mechanism';
import { Parameter } from '../../models/parameters/parameter';
import { RatingFeedback } from '../../models/feedbacks/rating_feedback';


describe('Rating View', () => {
    let ratingView:RatingView;

    beforeEach(() => {
        let ratingElement = $j('<div id="dialogId"><section id="ratingMechanism99"\n' +
            '             class="feedback-mechanism feedback-col rating-type mandatory"\n' +
            '             style="width: {{boxWidth}}; padding-left: {{boxPaddingLeft}}; padding-right: {{boxPaddingRight}};">\n' +
            '        <p style="" class="rating-text col col-left">\n' +
            '            Title\n' +
            '        </p>\n' +
            '        <p class="rating-input col col-right" data-mandatory-message="Mandatory Reminder">\n' +
            '\n' +
            '        </p>\n' +
            '        <div class="clearfix"></div>\n' +
            '    </section></div>');
        $j('body').append(ratingElement);
        let ratingMechanismId = 99;

        let parameters:Parameter[] = [];
        parameters.push(new Parameter(1, "defaultRating", 0.0));
        parameters.push(new Parameter(1, "maxRating", 5.0));
        parameters.push(new Parameter(1, "title", "Rating title"));
        let ratingMechanism:RatingMechanism = new RatingMechanism(ratingMechanismId, "RATING_TYPE", true, 1, true, parameters);

        ratingView = new RatingView(ratingMechanism, "dialogId");
    });

    it('should have 5 stars', () => {
        let numberOfStars = $j('#ratingMechanism99').find('.jq-star').length;
        expect(numberOfStars).toEqual(5);
    });

    it('should return the rating feedback', () => {
        let ratingFeedback:RatingFeedback = ratingView.getFeedback();
        expect(ratingFeedback.mechanismId).toEqual(99);
        expect(ratingFeedback.rating).toEqual(0);
        expect(ratingFeedback.title).toEqual("Rating title");
    });

    it('should round rating value', () => {
        let floatNumber = new Number(0.0).toFixed(1);
        expect(floatNumber).toEqual('0.0');
        expect(Math.round(floatNumber)).toEqual(0);
    });
});

