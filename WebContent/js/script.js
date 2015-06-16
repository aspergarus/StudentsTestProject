$(function () {
	if(typeof(Storage) == "undefined") {
		alert("Use modern browser like Chrome or Mozilla");
	}
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
	var lang = localStorage.getItem("lang");
	lang = lang ? lang : "en";
	switch (lang) {
		case "en":
			$(".change-picture").attr('src', basePath + '/imgs/ua.png');
			break;
		case "ua":
			$(".change-picture").attr('src', basePath + '/imgs/gb.png');
			break;
	}
	updateTranslate(lang);
	
	$(".translate-trigger").click(function(e) {
		e.preventDefault();
		
		var $this = $(this);
		
		if($this.attr("data-lang") == "en"){
			$this.attr("data-lang", "ua");
			localStorage.setItem("lang", "ua");
			$this.find('.change-picture').attr('src', basePath + '/imgs/gb.png');
		}
		else {
			$this.attr("data-lang", "en");
			localStorage.setItem("lang", "en");
			$this.find('.change-picture').attr('src', basePath + '/imgs/ua.png');
		}
		updateTranslate($this.attr("data-lang"));
	});
	
	function updateTranslate(lang) {
		$.getJSON(basePath + "/js/translate.json" , function(translateData) {
			$(".translate").each(function() {
				var $this = $(this);
				var key = $this.data("lang-key");
				if (translateData[key] != undefined) {
					$this.text(translateData[key][lang]);
				}
			});
		});
	}
	
	// Date & Time block.
	function checkTime(i) {
		return (i < 10) ? "0" + i : i;
	}

	function startTime() {
		var today = new Date();
		day = checkTime(today.getDate());
		month = checkTime(today.getMonth() + 1);
		year = checkTime(today.getFullYear());
		hour = checkTime(today.getHours());
		min = checkTime(today.getMinutes());
		sec = checkTime(today.getSeconds());
		
		document.getElementById('time').innerHTML = day + "/" + month + "/" + year + " " + hour + ":" + min + ":" + sec;
		t = setTimeout(function () {
			startTime()
		}, 500);
	}
	startTime();

	multipleFileUploadConfiguration();
	fileDeleteOperation();

	function multipleFileUploadConfiguration() {
		$("#upload").fileinput({
			'showUpload': false,
			'showPreview': false,
			'showCaption': false,
			'showCancel': false,
			'showUploadedThumbs': false,
			'language': 'uk',
			'allowedFileExtensions': ['jpg', 'gif', 'png', 'txt', 'pdf', 'doc', 'docx'],
			'allowedPreviewTypes': ['image', 'html', 'text', 'video', 'audio', 'flash', 'object'],
			'previewFileType': 'text'
		});
	}

	function fileDeleteOperation() {
		$('.delete-file-btn').click(function(e) {
			e.preventDefault();

			var $this = $(this);
			var $parent = $this.parent();

			// Show animation
			var $animationBlock = $('<div></div>').addClass('ajax-loader').append($('<img></img>').attr({ src : "imgs/ajax-loader.gif" }));
			$parent.append($animationBlock);
			$this.remove();

			var fid = $this.data('fid');
			$.ajax(basePath + "/files", {
				headers: {fid : fid },
				method: "DELETE",
				done: function(result) {
					console.log("Success");
					var $fileDiv = $parent.closest(".file");
					$fileDiv.find("div").remove();
					$fileDiv.append($('<span></span>').addClass('span-alert alert-success').text(result));
				},
				error: function() {
					$parent.find('div').remove();
					$parent.append($('<span></span>').addClass('span-alert alert-danger').text("Could not delete this file"));
				}
			});
		});
	}

	departmentDeleteOperation();

	function departmentDeleteOperation() {
		$('.delete-department-btn').click(function(e) {
			e.preventDefault();

			var $this = $(this);
			var $parent = $this.parent();
			
			// Show animation
			var $animationBlock = $('<div></div>').addClass('ajax-loader').append($('<img></img>').attr({ src : "imgs/ajax-loader.gif" }));
			$parent.append($animationBlock);
			$this.remove();

			var id = $this.data('id');
			$.ajax(basePath + "/department", {
				headers: {id : id },
				method: "DELETE",
				success: function(result) {
					$parent.find("div").remove();
					$parent.append($('<span></span>').addClass('span-alert alert-success').text(result));
				},
				error: function() {
					$parent.append($('<span></span>').addClass('span-alert alert-danger').text("Could not delete this departmnet"));
				}
			});
		});
	}

	transformer();

	function transformer() {
		$('.transformer-text').click(function(e) {
			e.preventDefault();
			
			var $this = $(this);
			var text = $this.text();
			var did = $this.data('did');
			console.log(did);
			var $parent = $this.parent();
			var $input = $parent.find('input');
			
			$this.hide();
			$input.show();
			$input.val(text);
			
			$input.focus();
			$input.focusout(function () {
				$this.show();
				$input.hide();
			}); 
				
			$input.change(function() {
				if ($input.val().trim() != "") {
					var newText = $input.val();
					$.ajax(basePath + "/department", {
						headers: {'did': did, 'name' : encodeURIComponent(newText) },
						method: "PUT",
						success: function(result) {
							$this.text(newText);
							$this.show();
							$input.hide();
						},
						error: function(result) {
							$this.show();
							$input.hide();
						}
					});
				}
			});
		});
	}

	commentDeleteOperation();

	function commentDeleteOperation() {
		$('.comment-config').click(function() {
			var $this = $(this);
			var cid = $this.data('comment-id')
			var $comment = $this.parent();
			console.log(cid);
			var $animationBlock = $('<div></div>').addClass('ajax-loader').append($('<img></img>').attr({ src : "imgs/ajax-loader.gif" }));
			$comment.html($animationBlock);

			$.ajax(basePath + "/comments", {
				headers: {'cid': cid },
				method: "DELETE",
				success: function(result) {
					$comment.html($('<span></span>').addClass('span-alert alert-success').text(result));
				},
				error: function(result, status, statusText) {
					$comment.html($('<span></span>').addClass('span-alert alert-danger').text("Some troubles happened with deleting comment: " + statusText));
				}
			});
		});
	}
});
