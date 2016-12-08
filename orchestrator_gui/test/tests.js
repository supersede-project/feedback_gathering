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