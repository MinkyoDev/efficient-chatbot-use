

export function duplicationCheck(email, message) {
	$.ajax({
		type: "get",
		url: "../api/duplication_check",
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
