

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
			console.log(response);
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