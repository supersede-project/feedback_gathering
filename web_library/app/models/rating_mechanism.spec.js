define(["require", "exports", './mechanism', './rating_mechanism', './parameter'], function (require, exports, mechanism_1, rating_mechanism_1, parameter_1) {
    "use strict";
    describe('Rating Mechanism', function () {
        var ratingMechanism;
        beforeEach(function () {
            ratingMechanism = new rating_mechanism_1.RatingMechanism(mechanism_1.ratingType, true, 1, false, [new parameter_1.Parameter('maxRating', 10),
                new parameter_1.Parameter('defaultRating', 3)]);
        });
        it('should return its configuration for the rating element', function () {
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
});
