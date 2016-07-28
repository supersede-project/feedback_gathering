import {ratingType} from '../js/config';
import {RatingMechanism} from './rating_mechanism';


describe('Rating Mechanism', () => {
    let ratingMechanism:RatingMechanism;

    beforeEach(() => {
        ratingMechanism = new RatingMechanism(1, ratingType, true, 1, false, [
            {key: 'maxRating', value: 10},
            {key: 'defaultRating', value: 3}
        ]);
    });

    it('should return its configuration for the rating element', () => {
        var options = ratingMechanism.getRatingElementOptions();

        var expectedOtions = {
            starSize: 25,
            totalStars: 10,
            initialRating: 3,
            useFullStars: true,
            disableAfterRate: false
        };

        expect(options['starSize']).toEqual(expectedOtions.starSize);
        expect(options['totalStars']).toEqual(expectedOtions.totalStars);
        expect(options['initialRating']).toEqual(expectedOtions.initialRating);
        expect(options['useFullStars']).toEqual(expectedOtions.useFullStars);
        expect(options['disableAfterRate']).toEqual(expectedOtions.disableAfterRate);
    });

});

