jQuery(document).ready(function($){
    $('.expand-search').click(function() {
        var searchMenu = $('.search-bar'),
            isHidden = searchMenu.is(':hidden');

        searchMenu.slideToggle('fast');

        if (isHidden){
            $('.search-field-bar').focus();
        }
    });       
});