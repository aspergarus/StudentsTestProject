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
    	
    	if($(this).attr("data-lang") == "en"){
    		$(this).attr("data-lang", "ua");
    		$(this).find('.change-picture').attr('src', 'imgs/gb.png');
    	}
    	else {
    		$(this).attr("data-lang", "en");
    		$(this).find('.change-picture').attr('src', 'imgs/ua.png');
    	}
    	updateTranslate($(this).attr("data-lang"));
    });

    function updateTranslate(lang) {
    	console.log(lang);
		$.getJSON("js/translate.json" , function(translateData) {
			console.log(translateData);
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
