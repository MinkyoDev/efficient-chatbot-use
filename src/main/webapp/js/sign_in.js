import { validCheck } from "./connections.js";

export function signIn() {
	$("#sign-in-form").submit(function(event) {
		submitValidation(event);
	})
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
	console.log(isOk);

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
	message = validCheck(email, pw, message);
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