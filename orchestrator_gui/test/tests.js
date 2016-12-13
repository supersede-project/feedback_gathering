(function($) {

    window.simulateClick = function(el, callback)
    {
        $(el).click();

        window.setTimeout(function() {
            callback();
        }, 100);
    };

}(jQuery));

// Test for AUDIO_TYPE
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

// Test for CATEGORY_TYPE
(function($) {
    QUnit.module("component: CATEGORY_TYPE");
    QUnit.test("CATEGORY_TYPE component", function(assert) {
        var done = assert.async();
        $("#categoryType").alpaca({
            "options": {
                "label": "Test Component",
                "helper": "Which option?",
                "optionLabels": ["Option1", "Option2", "Option3"]
            },
            "schema": {
                "enum": ["Option1", "Option2", "Option3", "Option4", "Option5"]
            },
            "postRender": function (renderedField) {
                assert.expect(4);
                var selectElems = $('#categoryType select > option');
                assert.equal(selectElems.length, 6, 'Right number of select options generated.');
                var rightLabelElem0 = $('#categoryType select > option:eq(1)');
                assert.equal(rightLabelElem0.text(), 'Option1', 'First option right label text populated correctly.');
                var rightLabelElem1 = $('#categoryType select > option:eq(2)');
                assert.equal(rightLabelElem1.text(), 'Option2', 'Second option right label text populated correctly.');
                var rightLabelElem2 = $('#categoryType select > option:eq(3)');
                assert.equal(rightLabelElem2.text(), 'Option3', 'Third option right label text populated correctly.');
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

