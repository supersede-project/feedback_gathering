(function($) {

    window.simulateClick = function(el, callback)
    {
        $(el).click();

        window.setTimeout(function() {
            callback();
        }, 100);
    };

}(jQuery));

// Test for Audio
(function($) {
    QUnit.module("component: AUDIO_TYPE");
    QUnit.test("component: AUDIO_TYPE", function(assert) {
        var done = assert.async();
        $("#audioType").alpaca({
            "options": {
                "type": 'AUDIO_TYPE',
                "label": "Audio Component"
            },
            "postRender": function (renderedField) {
                assert.expect(2);
                var audioType = $('#audioType legend');
                assert.ok(audioType.length, 'Placeholder for audio generated.');
                assert.equal(audioType.text().trim(), "Audio Component", 'Placeholder has right label.');
                done();
            }
        });
    });
}(jQuery) );

// Test for the TEXT_TYPE
(function($) {
    // module for grouping our tests
    QUnit.module("component: TEXT_TYPE");
    QUnit.test("TEXT_TYPE component", function(assert) {
        var done = assert.async();
        $("#textType").alpaca({
            "options": {
                "type": "TEXT_TYPE",
                "label": "Test Component",
                "hint": "Receipt for Best Homemade Ice Cream",
                "rows": 6,
                "cols": 80
            },
            "postRender": function (renderedField) {
                assert.expect(3);
                var textType = $('#textType textarea');
                assert.ok(textType.length, 'Textarea input field generated.');
                assert.equal(textType.attr('cols'),80, 'Textarea has right number of columns.');
                assert.equal(textType.attr('rows'),6, 'Textarea has right number of rows.');
                done();
            }
        });
    });
}(jQuery) );

