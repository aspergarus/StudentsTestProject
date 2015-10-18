$(function () {
	if(typeof(Storage) == "undefined") {
		alert("Use modern browser like Chrome or Mozilla");
	}
	$('.table').bootstrapTable();
	
	$('.tooltip-element').tooltip();
	
	setAutocomplete();
	
	function setAutocomplete() {
		$('input.typeahead').each(function() {
			var $this = $(this);
			var autocompleteUrl = $this.data("autocomplete-url");
			
			$this.typeahead({
				ajax: autocompleteUrl
			});
		});
	}
	
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
			var path = $this.data('path');
			
			var parameter = $this.data('parameter');
			
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
						headers: {'id': id, 'parameterName' : parameter, 'data' : encodeURIComponent(newText) },
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
		var $parent = $btn.parent();
		$btn.click(function() {
			var num = this.getAttribute('data-num');
			var groups = $tokenField.eq(num - 1).tokenfield('getTokens').map(function(el) { return el.value; }).join();

			if (groups.length > 0) {
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
			} else {
				$parent.append($('<span></span>').addClass('span-alert alert-warning').text("Please, select at least one group"));
				$('.span-alert').fadeOut(3000);
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
			var $this = $(this);
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
	
	selectStudents();
	
	function selectStudents() {
		$('body').on('click', '#toggle-select', function() {
			if ($(this).prop('checked')) {
				$('.selected-student').prop('checked', true);
			} else {
				$('.selected-student').prop('checked', false);
			}
		});
		
	}
	
	openTest();
	
	function openTest() {
		$('#open-test-to-students').click(function() {
			var studentsId = "";
			var testId = $('input[name=id]').attr('value');

			$('.selected-student').each(function() {
				$this = $(this);
				var i = 0;
				if ($this.prop('checked')) {
					$parent = $this.parent();
					var id = $parent.find('input[type=hidden]').attr('value');
					studentsId += id;
					studentsId += ",";
				}
			});
			
			$.ajax(basePath + "/openTest", {
				headers: {testId : testId, studentsId : studentsId },
				method: "PUT",
				success: function(result) {
					$('.alert').remove();
					if (result.trim().indexOf("Error") > -1) {
						$('.page-header').before('<div class="alert alert-danger"><p>Some troubles were occurred during opening the test</p></div>');
					} else {
						$('.page-header').before('<div class="alert alert-success"><p>Test has been opened</p></div>');
					}
				},
				error: function() {
					$('.alert').remove();
					$('.page-header').before('<div class="alert alert-danger"><p>Some troubles were occurred during opening the test</p></div>');
				}
			});
			
		});
	}
	
	changeAutocomplete();
	
	function changeAutocomplete () {
		$('.radio-user-status').change(function() {
			$radio = $(this);
			var status = parseInt($radio.val());
			
			if (status == 2) {
				$('#group-autocomplete').addClass('hidden').prop('required', false);
				$('#department-autocomplete').addClass('hidden').prop('required', false);
				
			} else if (status == 1) {
				$('#group-autocomplete').addClass('hidden').prop('required', false);
				$('#department-autocomplete').removeClass('hidden').prop('required', true);
			} else {
				$('#group-autocomplete').removeClass('hidden').prop('required', true);
				$('#department-autocomplete').addClass('hidden').prop('required', false);
			}
		});
	}
	
	deleteItem();
	
	function deleteItem() {
		$('.delete-item').on('click', function(e) {
			e.preventDefault();
			var $button = $(this);
			var item = $button.data('item');
			
			swal({	title: "Are you sure?",   
					text: "You will not be able to recover this " + item + "!",   
					type: "warning",   
					showCancelButton: true,   
					confirmButtonColor: "#DD6B55",
					confirmButtonText: "Yes, delete it!",   
					cancelButtonText: "No, cancel!",   
					closeOnConfirm: false   
				}, function(isConfirm) {   
					if (isConfirm) {
						var itemId = $button.data('id');
						var morePath = $button.data('path');
						var $parent = $button.closest('tr');
						
						$.ajax(basePath + morePath, {
							headers: {id : itemId},
							method: "DELETE",
							success: function(result) {
								$parent.remove();
								swal("Deleted!", "Your item has been deleted.", "success"); 
							},
							error: function () {
								$button.parent().append($('<span></span>').addClass('span-alert alert-warning').text("Error"));
							}
						});   
					} 
			});
		});
	}
	
	updateItem();
	
	function updateItem() {
		$('.update-subject').on('click', function(e) {
			e.preventDefault();
			
			var $button = $(this);
			var itemId = $button.data('id');
			
			var subject = $('input[name=subjectName]').val();
			var department = $('input[name=departmentName]').val();
			
			$.ajax(basePath + "/subjects", {
				headers: {id : itemId, subject : encodeURIComponent(subject), department : encodeURIComponent(department)},
				method: "PUT",
				success: function(result) {
					if (result.indexOf('troubles') > -1) {
						$('body > .container').prepend($('<div></div>').addClass('alert alert-warning').text(result));
					} else {
						$('body > .container').prepend($('<div></div>').addClass('alert alert-success').text(result));
					}
				},
				error: function () {
					$('body > .container').prepend($('<div></div>').addClass('alert alert-warning').text("Some errors."));
				}
			});
		});
	}
	
	initializeTest();
	
	function initializeTest() {
		$('.question-block').first().removeClass('hidden').addClass('current');
		
		var questions = $('.question-block').length;
		$('#all-questions').text(questions);
		$('.progress-bar').attr('aria-valuemax', questions);
		
	}
	
	seeNextQuestion();
	
	function seeNextQuestion() {
		var numberQuestions = $('.question-block').length;
		var completed = 0;
		
		$('body').on('click', '#next-question', function() {
			var hasAnswer = false;
			
			$('.current label input').each(function() {
				$this = $(this);
				if (($this).prop('checked')) {
					hasAnswer = true;
				}
			});
			
			if (hasAnswer) {
				$current = $('.current').removeClass('current').removeClass('uncompleted').addClass('completed');
				
				if ($current.next('.uncompleted').length > 0) {
					$current.next().addClass('current');
				} else {
					$('.uncompleted').first().addClass('current').removeClass('uncompleted');
				}
				completed++;
				var currentNumberQuestion = completed + 1;
				$('.progress-bar').attr('aria-valuenow', currentNumberQuestion);
				$('#current-number-question').text(currentNumberQuestion);
				var percents = currentNumberQuestion / numberQuestions * 100;
				$('.progress-bar').css('width', percents + '%');
				
				if (completed === numberQuestions) {
					$('#next-question').hide();
					$('#miss-question').hide();
					$('.progress').hide();
					$('.test-complete-info').removeClass('hidden');
					$('input[type=submit]').removeClass('hidden');
				}
			}
		});
		
		$('body').on('click', '#miss-question', function() {
			$current = $('.current').removeClass('current').addClass('uncompleted');
			
			if ($current.next('.uncompleted').length > 0) {
				$current.next('.uncompleted').addClass('current').removeClass('hidden');
			} else {
				$('.uncompleted').first().addClass('current');
			}
			
		});
	}
	
	removeStudentsFromReady();
	
	function removeStudentsFromReady () {
		$('#clear-ready-students').on('click', function() {
			var testId = $('input[name=id]').attr('value');
			
			$.ajax(basePath + "/openTest", {
				headers: {testId : testId},
				method: "DELETE",
				success: function(result) {
					$('tbody tr').remove();
					$('tbody').append('<tr class="no-records-found"><td colspan="6">No matching records found</td></tr>');
				},
				error: function () {
					$('body > .container').prepend($('<div></div>').addClass('alert alert-warning').text("Some errors."));
				}
			});
		});
	}
	
	removeStudentFromReady();
	
	function removeStudentFromReady () {
		$('.remove-student').on('click', function() {
			$this = $(this);
			
			var testId = $('input[name=id]').attr('value');
			$parent = $this.parent();
			var studentId = $parent.find('input[type=hidden]').attr('value');
			
			$.ajax(basePath + "/openTest", {
				headers: {studentId : studentId, testId : testId},
				method: "DELETE",
				success: function(result) {
					$parent.parent().remove();
				},
				error: function () {
					$('body > .container').prepend($('<div></div>').addClass('alert alert-warning').text("Some errors."));
				}
			});
		});
	}
	
	setCountdown();
	
	function setCountdown() {
		$timer = $(".example").TimeCircles({"direction": "Counter-clockwise",
			"animation": "ticks",
			"bg_width": 1.2,
		    "fg_width": 0.13,
		    "circle_bg_color": "#75756C",
			"time": {
				Days: { show: false },
			    Hours: { show: false },
			    Minutes: { "color": "#FFCC66" },
			    Seconds: { color: "#FF9999" }
			}
		}).addListener(
			function(unit,value,total) { 
				if (total == 0) {
					$timer.stop();
					$('.question-block').removeClass('uncompleted');
					$('input[type=submit]').click();
				}	
			} 
		);
	}
	
});
