

export function duplicationCheck(email, message) {
	$.ajax({
		type: "post",
		url: "../api/v1/duplication-check",
		async: false,
		data: { "email": email },
		success: function(response) {
			if (response.message === "unavailable") {
				message.email = "중복된 이메일입니다."
			}
		},
		error: function(error) {
			console.log(error);
		}
	})
	return message;
}

export function validCheck(email, pw, message) {
	var data = { "email": email, "password": pw };
	$.ajax({
		type: "post",
		url: "../api/v1/user-valid-check",
		async: false,
		data: JSON.stringify(data),
		success: function(response) {
			if (response.code === "1") {
				message.email = "잘못된 이메일입니다."
			} else if (response.code === "2") {
				message.pw = "비밀번호를 다시 입력해 주세요."
			}
		},
		error: function(error) {
			console.log(error);
		}
	})
	return message;
}

export function getAnswer(content) {
	var baseUrl = getAbsoluteURL(window.location.href);
	console.log(baseUrl);

	var data = { "content": content };
	$.ajax({
		type: "post",
		url: baseUrl + "/api/v1/chatting",
		async: false,
		data: JSON.stringify(data),
		success: function(response) {
			console.log(response);
		},
		error: function(error) {
			console.log(error);
		}
	})
}

function getAbsoluteURL(currentUrl) {
	var firstSlashIndex = currentUrl.indexOf('/', currentUrl.indexOf('//') + 2);
	var secondSlashIndex = currentUrl.indexOf('/', firstSlashIndex + 1);
	var baseUrl = currentUrl.substring(0, secondSlashIndex);
	return baseUrl;
}