function setTranslate() {
	var lang = localStorage.getItem("lang");
	lang = lang ? lang : "en";
	switch (lang) {
		case "en":
			$('.translate-trigger').attr('data-lang', 'en');
			$(".change-picture").attr('src', basePath + '/imgs/ua.png');
			break;
		case "ua":
			$('.translate-trigger').attr('data-lang', 'ua');
			$(".change-picture").attr('src', basePath + '/imgs/gb.png');
			break;
	}
	updateTranslate(lang);
}

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
