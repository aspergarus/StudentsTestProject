$(function () {
    $('.table').bootstrapTable();
    
    $('input.typeahead').each(function() {
    	var $this = $(this);

    	var autocompleteUrl = $this.data("autocomplete-url");
    	$this.typeahead({
            ajax: autocompleteUrl
        });
    });

    var $menu_li = $(".top-menu li");
    $menu_li.removeClass("active");
    $menu_li.each(function() {
    	var $this = $(this);
    	var href = $this.find("a").attr("href");

    	if (document.location.pathname == href || document.location.pathname == (href + "/")) {
    		$this.addClass("active");
    	}
    });

    // Translate block.
    updateTranslate("en");

    $(".translate-trigger").click(function(e) {
    	e.preventDefault();

    	updateTranslate($(this).data("lang"));
    });

    function updateTranslate(lang) {
		$.getJSON("js/translate.json" , function(translateData) {
        	$(".translate").each(function() {
            	var $this = $(this);
            	var key = $this.data("lang-key");
            	if (translateData[key] != undefined) {
            		$this.text(translateData[key][lang]);
            	}
            });
        });
    }
    // End of translate block.

});
