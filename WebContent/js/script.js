$(function () {
    $('.table').bootstrapTable();
    
    var typeaheadSource = ['John', 'Alex', 'Terry'];
    $('input.typeahead').typeahead({
        ajax: 'subjects'
    });
});
