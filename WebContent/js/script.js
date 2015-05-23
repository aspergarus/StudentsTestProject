$(function () {
    $('.table').bootstrapTable();
    
    $('input.typeahead').each(function() {
    	$this = $(this);

    	var autocompleteUrl = $this.data("autocomplete-url");
    	$this.typeahead({
            ajax: autocompleteUrl
        });
    });
    
});
