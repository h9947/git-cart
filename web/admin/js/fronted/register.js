var RegisterPanel = RegisterPanel || {};
(function($){

    RegisterPanel.changePhoneLabel = function() {
        $('.common-index:eq(0)').removeClass('step-index').addClass('active-index').html('');
        $('.common-pointer:eq(0)').removeClass('pointer').addClass('active-pointer');
        // $('.common-index')[0].addClass();
        $('p[class="info-text"]:eq(0)').addClass('active');
        $('#phonePanel').hide();
        $('#otherInfoPanel').show();
    }

    RegisterPanel.changeOtherInfoLabel = function() {
        $('.common-index:gt(0)').removeClass('step-index').addClass('active-index').html('');
        $('.common-pointer:gt(0)').removeClass('pointer').addClass('active-pointer');
        $('.info-text:gt(0)').addClass('active');
        $('#otherInfoPanel').hide();
        $('#successPanel').show();
    }

    /**
    $.fn.changePhoneLabel = function() {
        $('.common-index:eq(0)').removeClass('step-index').addClass('active-index').html('');
        $('.common-pointer:eq(0)').removeClass('pointer').addClass('active-pointer');
        // $('.common-index')[0].addClass();
        $('p[class="info-text"]:eq(0)').addClass('active');
        $('#phonePanel').hide();
        $('#otherInfoPanel').show();
    }
     */

    /**
    $.fn.changeOtherInfoLabel = function() {
        $('.common-index:gt(0)').removeClass('step-index').addClass('active-index').html('');
        $('.common-pointer:gt(0)').removeClass('pointer').addClass('active-pointer');
        $('.info-text:gt(0)').addClass('active');
        $('#otherInfoPanel').hide();
        $('#successPanel').show();
    }
     */
})(jQuery);