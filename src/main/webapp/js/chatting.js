var assistantCount = 0;

export function makeBallons(type, content, move = true) {
	var animation = "balloon-rise-l";
	if (type == "user") {
		var animation = "balloon-rise-r";
	} else if (type == "assistant") {
		var id = createDivUniqueId();
	} else if (type == "loding") {
		var id = createDivUniqueId();
		type = "assistant";
		content = setLodingAnimation();
	}
	
	animation = (!move) ? "" : animation;
	var Ballon_format = `<div class="balloon-box ${animation}">` +
		`<div class="${type}-balloon">` +
		`<div class="pull"><p id=${id}>${content}</p></div>` +
		`</div>` +
		`</div>`;
	$('#conv-box').append(Ballon_format);

	return id;
}

function setLodingAnimation() {
	var animate_comp = '<span class="loading-box"><span class="circles"><i></i><i></i><i></i></span></span>';
	return animate_comp;
}

function createDivUniqueId() {
	assistantCount++;
	return 'assistant_' + assistantCount;
}