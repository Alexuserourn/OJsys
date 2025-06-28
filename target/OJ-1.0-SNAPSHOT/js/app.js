/**
 * Online OJ System
 */
$(function () {
    //page scroll
    $('a.page-scroll').bind('click', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top - 100
        }, 1500);
        event.preventDefault();
    });

    //toggle scroll menu
    $(window).scroll(function () {
        var scroll = $(window).scrollTop();
        if (scroll >= 100) {
            $('.sticky-navigation').addClass('scrolled');
        } else {
            $('.sticky-navigation').removeClass('scrolled');
        }
    });
});