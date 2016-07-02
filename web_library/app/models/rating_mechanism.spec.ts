import {ratingType} from './mechanism';
import {RatingMechanism} from './rating_mechanism';
import {Parameter} from './parameter';


describe('Rating Mechanism', () => {
    let ratingMechanism:RatingMechanism;

    beforeEach(() => {
        ratingMechanism = new RatingMechanism(ratingType, true, 1, false, [new Parameter('maxRating', 10),
            new Parameter('defaultRating', 3)]);
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

