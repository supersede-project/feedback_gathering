(function($) {

    window.simulateClick = function(el, callback)
    {
        $(el).click();

        window.setTimeout(function() {
            callback();
        }, 100);
    };

}(jQuery));

QUnit.test( "hello test", function( assert ) {
    assert.ok( 1 == "1", "Passed!" );
});

(function($) {

    QUnit.module("fields: textarea");

    // Test case 1 : Textarea field with options.
    QUnit.test("Textarea field with options.", function(assert) {
        var done = assert.async();
        var data = "Ice cream or ice-cream is a frozen dessert usually made from dairy products, such as milk and cream, and often combined with fruits or other ingredients and flavours.";
        $("#textarea-1").alpaca({
            "data": data,
            "options": {
                "type": "textarea",
                "label": "Receipt",
                "helper": "Receipt for Best Homemade Ice Cream",
                "rows": 6,
                "cols": 80
            },
            "postRender": function (renderedField) {
                assert.expect(5);
                assert.equal(renderedField.getValue(), data, 'Textarea field getValue() method returns correct value.');
                var inputElem = $('#textarea-1 textarea');
                assert.ok(inputElem.length, 'Textarea input field generated.');
                assert.equal(inputElem.val(), data, "Textarea input field value populated correctly.");
                assert.equal(inputElem.attr('cols'),80, 'Textarea has right number of columns.')
                assert.equal(inputElem.attr('rows'),6, 'Textarea has right number of rows.')
                done();
            }
        });
    });

}(jQuery) );