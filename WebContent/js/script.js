$(function () {
    $('.table').bootstrapTable();
    
    $('input.typeahead').each(function() {
    	var $this = $(this);

    	var autocompleteUrl = $this.data("autocomplete-url");
    	$this.typeahead({
            ajax: autocompleteUrl
        });
    });
    
});
