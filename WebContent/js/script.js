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
		day = checkTime(today.getDay());
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
});
