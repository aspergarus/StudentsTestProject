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
			
			$.ajax(basePath + "/install", {
				headers: {},
				method: "PUT",
				success: function(result) {
					swal({
						title: "Sweet!",   
						text: "Tables created! Admin registered!",   
						type: "success",   
						}, function(isConfirm) {   
							if (isConfirm) {     
								window.location.replace(basePath);
							}
						});
				},
				error: function() {
					swal("Oops...", "Something went wrong!", "error");
				}
			});
		}); 
	}
});
