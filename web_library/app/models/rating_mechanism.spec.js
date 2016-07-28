define(["require", "exports", '../js/config', './rating_mechanism'], function (require, exports, config_1, rating_mechanism_1) {
    "use strict";
    describe('Rating Mechanism', function () {
        var ratingMechanism;
        beforeEach(function () {
            ratingMechanism = new rating_mechanism_1.RatingMechanism(1, config_1.ratingType, true, 1, false, [
                { key: 'maxRating', value: 10 },
                { key: 'defaultRating', value: 3 }
            ]);
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
//# sourceMappingURL=rating_mechanism.spec.js.map