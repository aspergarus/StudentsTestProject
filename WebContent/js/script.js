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
		var day = checkTime(today.getDate());
		var month = checkTime(today.getMonth() + 1);
		var year = checkTime(today.getFullYear());
		var hour = checkTime(today.getHours());
		var min = checkTime(today.getMinutes());
		var sec = checkTime(today.getSeconds());
		
		document.getElementById('time').innerHTML = day + "/" + month + "/" + year + " " + hour + ":" + min + ":" + sec;
		var t = setTimeout(function () {
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
	/* Function for transfom Text element to <input type="text"> */
	transformer();
	
	function transformer() {
		$('.transformer-text').click(function(e) {
			e.preventDefault();
			
			var $this = $(this);
			var text = $this.text().trim();
			var id = $this.data('id');
			var path = $this.attr('data-path');
			
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
					$.ajax(basePath + "/" + path, {
						headers: {'id': id, 'name' : encodeURIComponent(newText) },
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
	
	groupDeleteOperation();
	
	function groupDeleteOperation() {
		$('.delete-group-btn').click(function(e) {
			e.preventDefault();

			var $this = $(this);
			var $parent = $this.parent();
			
			// Show animation
			var $animationBlock = $('<div></div>').addClass('ajax-loader').append($('<img></img>').attr({ src : "imgs/ajax-loader.gif" }));
			$parent.append($animationBlock);
			$this.remove();

			var id = $this.data('id');
			$.ajax(basePath + "/groups", {
				headers: {id : id },
				method: "DELETE",
				success: function(result) {
					$parent.find("div").remove();
					$parent.append($('<span></span>').addClass('span-alert alert-success').text(result));
				},
				error: function() {
					$parent.append($('<span></span>').addClass('span-alert alert-danger').text("Could not delete this group"));
				}
			});
		});
	}
	
	

	commentDeleteOperation();

	function commentDeleteOperation() {
		$('.comment-config .glyphicon-remove').click(function() {
			var $this = $(this);
			var $comment = $this.closest('.comment');
			var cid = $comment.data('comment-id');
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

	commentEditOperation();

	function commentEditOperation() {
		var $editBtns = $('.comment[data-author="' + userId + '"]').find('.glyphicon-cog').show();

		$editBtns.click(function() {
			var $this = $(this);
			$this.hide();
			var $comment = $this.closest('.comment');

			var commentTitle = $comment.find('.comment-title');
			commentTitle.html($('<input type="text" class="form-control" name="title" id="title" required />').val(commentTitle.text()));

			var commentBody = $comment.find('.comment-msg');
			commentBody.html($('<textarea class="form-control" rows="7" cols="150" name="body" id="body" required></textarea>').val(commentBody.text()));

			$comment.find('.comment-entry').append(
					$('<input type="submit" />')
						.attr("value", "Save changes")
						.addClass('btn btn-primary btn-lg edit-comment-btn')
						.data('comment-id', $comment.data('comment-id'))
			);

			$comment.addClass('comment-has-form');
		});

		$('.comment').on('click', '.edit-comment-btn', function(e) {
			e.preventDefault();
			var $this = $(this);
			var $parent = $this.parent();
			var cid = $this.data('comment-id');
			var title = $parent.find('input[type=text]').val();
			var msg = $parent.find('textarea').val();

			if (cid && title.length != 0 && msg.length != 0) {
				$.ajax(basePath + "/comments", {
					headers: {'cid': cid, 'title': title, 'msg': msg },
					method: "PUT",
					success: function(result) {
						// $comment.html($('<span></span>').addClass('span-alert alert-success').text(result));
						var commentTitle = $parent.find('.comment-title');
						commentTitle.html(commentTitle.find('input').val());

						var commentBody = $parent.find('.comment-msg');
						commentBody.html(commentBody.find('textarea').val());

						var $comment = $this.closest('.comment');
						$comment.removeClass('comment-has-form');
						$comment.find('.glyphicon-cog').show();
						$comment.find('span.error-msg').remove();
						$this.hide();
					},
					error: function(result, status, statusText) {
						console.log(statusText);
					}
				});
			}
			else {
				if ($parent.find('span.error-msg').length == 0) {
					$parent.append($('<span></span>').addClass('error-msg').text('Title and message should not be empty!'));
				}
			}
		});
	}

	assigningGroups();

	function assigningGroups() {
		var $tokenField = $('.tokenfield');
		$tokenField.tokenfield({
			autocomplete: {
				source: "autocomplete/groups",
				delay: 100
			}
		});

		var $btn = $('.assign-subject-group');
		$btn.click(function() {
			var num = this.getAttribute('data-num');
			var groups = $tokenField.eq(num - 1).tokenfield('getTokens').map(function(el) { return el.value; }).join();

			if (groups.length >= 0) {
				var subject = this.getAttribute('data-subject');

				$.ajax(basePath + "/groups", {
					headers: {'subject': encodeURIComponent(subject), 'groups' : encodeURIComponent(groups) },
					method: "POST",
					success: function(result) {
						BootstrapDialog.show({
							title: 'Success!',
							message: 'Subject <' + subject + '> was shared to groups: ' + groups,
							onshow: function (dialogRef){
								setTimeout(function(){
									dialogRef.close();
								}, 4000);
							}
						});
					},
					error: function() {
						$parent.html($('<span></span>').addClass('span-alert alert-danger').text("Subject already shared or something else troubles."));
					}
				});
			}
		});
	}
	
	addAnswer();
	
	function addAnswer() {
		$('#add-answer').click(function(e) {
			e.preventDefault();
			
			var count = $('.answer').length + 1;
			
			var $newAnswerArea = $('.answer:first').clone().appendTo(".answers");
			var $newAnswer = $newAnswerArea.find('input[type=text]');
			$newAnswer.val('');
			var $newCorrectAnswer = $newAnswerArea.find('input[type=checkbox]');
			$newCorrectAnswer.attr('checked', false);

			$newAnswer.attr('name', 'answer-' + count);
			$newCorrectAnswer.attr('name', 'correct-answer-' + count);
			
			$('#count').attr('value', count);
		});
	}
	
	deleteAnswer();
	
	function deleteAnswer() {
		$('#question-form').on('click', '.delete-answer', function(e) {
			e.preventDefault();
			$this = $(this);
			var count = $('.answer').length;
			if (count > 1) {
				var $answer = $this.closest('.answer');
				$answer.remove();
			}
		});
	}
	
	submitForm();
	
	function submitForm() {
		$('.delete-question').click(function() {
			$(this).parent().find('.submit-delete-question').click();
		});
	}

	validateQuestionsForm();
	
	function validateQuestionsForm() {
		$('#question-form').on('change', '.true-answer', function() {
			var trueQuestions = $('#question-form .true-answer').toArray().filter(function(el) {
			  return el.checked;
			});
			if (trueQuestions.length < 1) {
				$('#question-form #add-question').prop('disabled', true);
			}
			else {
				$('#question-form #add-question').prop('disabled', false);
			}
		});

		$('#question-form .true-answer').trigger('change');
	}
	
});
