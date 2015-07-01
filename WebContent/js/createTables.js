$(function () {
	if(typeof(Storage) == "undefined") {
		alert("Use modern browser like Chrome or Mozilla");
	}

createTables();
	
	function createTables() {
		$('.create-tables').click(function(e) {
			e.preventDefault();
			
			$this = $(this);
			$parent = $this.parent();
			
			var name = $('#name').val();
			var password = $('#pass').val();
			
			$.ajax(basePath + "/install", {
				headers: {'name' : name, 'pass' : password},
				method: "PUT",
				success: function(result) {
					window.location.replace(basePath);
				},
				error: function() {
					$parent.append($('<span></span>').addClass('span-alert alert-danger').text("Can't create tables."));
				}
			});
		}); 
	}
});
