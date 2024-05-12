
export function changePw() {
	$("#chane-pw-form").submit(submitValidation)
}

function submitValidation(event) {
	console.log("ddd");
	$("#pw-alter").css("display", "none");

	var pw = $('#password').val();

	var message = { email: "", pw: "" };
	var isOk = false;

	try {
		message, isOk = validationCheck("", pw, message);
	} catch (err) {
		console.log("validationCheck error");
		console.log(err);
	}
	console.log(message);

	if (message.pw != "") {
		$("#pw-change-alter").text(message.pw);
		$("#pw-change-alter").css("display", "block");
		$("#pw-change-alter").css("color", "#FA5252");
	}

	if (!isOk) {
		event.preventDefault();
		return;
	}
}

function validationCheck(email, pw, message) {
	message = blankCheck(email, pw, message);
	if (message.pw !== "") {
		return message, false;
	};
	message = formatCheck(email, pw, message);
	if (message.pw !== "") {
		return message, false;
	};

	return message, true;
}

function blankCheck(email, pw, message) {
	if (email == "") {
		message.email = "Email을 입력해 주세요.";
	}
	if (pw == "") {
		message.pw = "비밀번호를 입력해 주세요.";
	}
	return message;
}

function formatCheck(email, pw, message) {
	let emailPattern = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i
	let passwordPattern = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/
	if (!emailPattern.test(email)) {
		message.email = "Email이 잘못되었습니다.";
	};
	if (!passwordPattern.test(pw)) {
		message.pw = "비밀번호는 영문, 숫자를 포함한 최소 8자리여야 합니다.";
	};
	return message;
}