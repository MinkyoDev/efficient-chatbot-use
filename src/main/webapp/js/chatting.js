export function chatting() {
    
}

export function makeBallons(type, content, id = "user") {
	if (type == "loding") {
		type = "assistant";
		content = setLodingAnimation();
	}
	var Ballon_format = `<div class="balloon-box balloon-rise">` +
		`<div class="${type}-balloon">` +
		`<div class="pull"><p id=${id}>${content}</p></div>` +
		`</div>` +
		`</div>`;
	$('#conv-box').append(Ballon_format);

	$('#conv-box').children().last().hide().slideDown(500);
}

function setLodingAnimation() {
	var animate_comp = '<span class="loading-box"><span class="circles"><i></i><i></i><i></i></span></span>';
	return animate_comp;
}