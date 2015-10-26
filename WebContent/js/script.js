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
	/* Function for transfom Text element to <input type="text"> and update in DB */
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
							$('.alert').remove();
							if (result.indexOf("Warning") > -1) {
								$('.lead').before('<div class="alert alert-warning"><p>' + result + '</p></div>');
							} else {
								$this.text(newText);
								$this.show();
								$input.hide();
							}
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
			
			title = encodeURIComponent(title);
			msg = encodeURIComponent(msg);

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
								swal("Deleted!", "Your " + item + " has been deleted.", "success"); 
							},
							error: function () {
								$button.parent().append($('<span></span>').addClass('span-alert alert-warning').text("Error"));
							}
						});   
					} 
			});
		});
	}
	
	$.fn.randomize = function(selector) {
	    var $elems = selector ? $(this).find(selector) : $(this).children(),
	        $parents = $elems.parent();

	    $parents.each(function(){
	        $(this).children(selector).sort(function(){
	            return Math.round(Math.random()) - 0.5;
	        }).detach().appendTo(this);
    	});
    	return this;
    };
	
	testing();
	
	function testing() {
		
		if (document.URL.indexOf("testing") > -1) {
			
			var testTime = $('#test-time').text();
			var testBegin = getCookie('testBegin');
			
			var questions = $('.question-block').length;
			var completedQuestions = $('.completed').length;
			
			if (testBegin) {
				var now = new Date().getTime() / 1000;
				$('.timer').data('timer', testTime - (now - testBegin));
				
				var questionsId = $('input[name=questions-id-list]').val();
				var cookies = document.cookie.split(";");
				
				setAnswersFromCookie(cookies, questionsId);
				
				$('.uncompleted').first().removeClass('hidden').addClass('current');
				completedQuestions = $('.completed').length || 0;
			} else {
				var testStart = new Date().getTime() / 1000;
				setCookie('testBegin', testStart, {path: "/"});
				$('.uncompleted').first().removeClass('hidden').addClass('current');
				var percents = 3;
			}
			if (completedQuestions === questions) {
				showSubmitButton();
			} else {
				$('ul').randomize();
			}
			setProgressBar(questions, completedQuestions);
			$('[data-toggle="popover"]').popover('show');
			
		}
		
		$('body').on('click', '#next-question', function() {
			var hasAnswer = false;
			var questionId;
			var answersId = "";
			
			$('.current label input').each(function() {
				var $this = $(this);
				if (($this).prop('checked')) {
					hasAnswer = true;
					answersId += $this.val();
					answersId += ",";
				}
			});
			
			if (hasAnswer) {
				$current = $('.current').removeClass('current').removeClass('uncompleted').addClass('completed');
				questionId = $current.find('.question-id').val();
				
				if ($current.next('.uncompleted').length > 0) {
					$current.next().addClass('current');
				} else {
					$('.uncompleted').first().addClass('current');
				}
				completedQuestions++;
				setProgressBar(questions, completedQuestions);
				
				answersId = answersId.substring(0, answersId.length - 1);
				setCookie(questionId, answersId, {path: "/"});
				
				if (completedQuestions === questions) {
					showSubmitButton();
				}
			}
		});
		
		$('body').on('click', '#miss-question', function() {
			if ($('.uncompleted').length > 1) {
				
				$current = $('.current').removeClass('current').addClass('uncompleted');
				
				do {
					if ($current.next('.question-block').length > 0)  {
						$current = $current.next();
					} else {
						$current = $('.uncompleted').first().addClass('current');
					}
				} while ($current.hasClass('completed'));
					 
				$current.addClass('current');
			}
		});
	}
	
	function setAnswersFromCookie(cookies, questionsId) {
		if (cookies.length > 1) {
			for (var i = 0; i < cookies.length; i++) {
				var cookie = cookies[i].trim();
				var eqPos = cookie.indexOf("=");
				var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
				var ids = getCookie(name).trim();
				
				if (questionsId.indexOf(name) > -1 ) {
					$qb = $('.question-id-' + name);
					
					if (ids.indexOf(",") > -1) {
						var idsArray = ids.split(",");
						for (var j = 0; j < idsArray.length; j++) {
							$qb.find('label input[value=' + idsArray[j] + ']').prop('checked', true);
						}
					} else {
						$qb.find('label input[value=' + ids + ']').prop('checked', true);
					}
					$qb.removeClass('current').removeClass('uncompleted').addClass('completed');
				}
			}
		}
	}
	
	function showSubmitButton() {
		$('#next-question').hide();
		$('#miss-question').hide();
		$('.test-complete-info').removeClass('hidden');
		$('input[type=submit]').removeClass('hidden');
	}
	
	function setProgressBar(questions, completedQuestions) {
		var percents = (completedQuestions / questions) * 100;
		if (percents === 0) {
			percents = 3;
		}
		$('#current-number-question').text(completedQuestions);
		$('#all-questions').text(questions);
		$('.progress-bar').attr('aria-valuemax', questions);
		$('.progress-bar').css('width', percents + '%');
	}
	
	turboMode();
	
	function turboMode() {
		$('#turbo-mode').on('click', function() {
			$('.uncompleted label input').prop('checked', true);
			$('.uncompleted').addClass('completed').removeClass('uncompleted');
			$('#testing-form').submit();
		});
	}
	
	deleteCookiesAfterSubmit();
	
	function deleteCookiesAfterSubmit() {
		$('#testing-form').submit(function() {
			deleteCookies();
	    });
	}
	
	function setCookie(name, value, options) {
		  options = options || {};

		  var expires = options.expires;

		  if (typeof expires == "number" && expires) {
		    var d = new Date();
		    d.setTime(d.getTime() + expires * 1000);
		    expires = options.expires = d;
		  }
		  if (expires && expires.toUTCString) {
		    options.expires = expires.toUTCString();
		  }

		 /* value = encodeURIComponent(value);*/

		  var updatedCookie = name + "=" + value;

		  for (var propName in options) {
		    updatedCookie += "; " + propName;
		    var propValue = options[propName];
		    if (propValue !== true) {
		      updatedCookie += "=" + propValue;
		    }
		  }
		  document.cookie = updatedCookie;
		}
	
	function getCookie(name) {
		var matches = document.cookie.match(new RegExp(
			"(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
		));
		return matches ? decodeURIComponent(matches[1]) : undefined;
	}
	
	function deleteCookies() {
		var cookies = document.cookie.split(";");
		for (var i = 0; i < cookies.length; i++) {
			var cookie = cookies[i];
			var eqPos = cookie.indexOf("=");
			var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
			document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
		}
        return true;
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
			var $this = $(this);
			
			var testId = $('input[name=id]').attr('value');
			var $parent = $this.parent();
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
		var $timer = $(".timer").TimeCircles({"direction": "Counter-clockwise",
			"animation": "ticks",
			"bg_width": 1.2,
		    "fg_width": 0.13,
		    "circle_bg_color": "#75756C",
			"time": {
				Days: { "show": false },
			    Hours: { "show": false },
			    Minutes: { "color": "#FFCC66" },
			    Seconds: { "color": "#FF9999" }
			}
		}).addListener(
			function(unit,value,total) { 
				if (total == 0) {
					$timer.stop();
					$('.question-block').removeClass('uncompleted');
					$('#testing-form').submit();
				}	
			} 
		);
	}
	
});
