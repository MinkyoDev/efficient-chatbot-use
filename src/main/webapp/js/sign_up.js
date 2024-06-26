import { duplicationCheck } from "./connections.js";

export function signUp() {
	$("#sign-up-form").submit(submitValidation)
}

function submitValidation(event) {
	$("#email-alter").css("display", "none");
	$("#pw-alter").css("display", "none");

	var email = $('#email').val();
	var pw = $('#password').val();

	var message = { email: "", pw: "" };
	var isOk = false;

	try {
		message, isOk = validationCheck(email, pw, message);
	} catch (err) {
		console.log("validationCheck error");
		console.log(err);
	}
	console.log(message);

	if (message.email != "") {
		$("#email-alter").text(message.email);
		$("#email-alter").css("display", "block");
	}
	if (message.pw != "") {
		$("#pw-alter").text(message.pw);
		$("#pw-alter").css("display", "block");
	}

	if (!isOk) {
		event.preventDefault();
		return;
	}
}

function validationCheck(email, pw, message) {
	message = blankCheck(email, pw, message);
	if (message.email !== "" || message.pw !== "") {
		return message, false;
	};
	message = formatCheck(email, pw, message);
	if (message.email !== "" || message.pw !== "") {
		return message, false;
	};
	message = duplicationCheck(email, message);
	if (message.email !== "" || message.pw !== "") {
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