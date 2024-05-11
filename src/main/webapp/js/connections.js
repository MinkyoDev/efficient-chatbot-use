import { getCookie, getAbsoluteURL } from "./utils.js";

const baseUrl = getAbsoluteURL(window.location.href);

// sign up
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

// sign in
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

// chat load
export async function getUserData() {
	return new Promise((resolve, reject) => {
		$.ajax({
			type: "get",
			url: baseUrl + "/api/v1/user-data",
			async: true,
			success: function(response) {
				resolve(response);
			},
			error: function(error) {
				reject(error);
			}
		});
	});
}

export async function getModelList() {
	return new Promise((resolve, reject) => {
		$.ajax({
			type: "get",
			url: baseUrl + "/api/v1/model-list",
			async: true,
			success: function(response) {
				resolve(response);
			},
			error: function(error) {
				reject(error);
			}
		});
	});
}

export async function getChatList() {
	return new Promise((resolve, reject) => {
		$.ajax({
			type: "get",
			url: baseUrl + "/api/v1/chat-list",
			async: true,
			success: function(response) {
				resolve(response);
			},
			error: function(error) {
				reject(error);
			}
		});
	});
}

export async function getDetails() {
	var chatId = getCookie('chatId');

	return new Promise((resolve, reject) => {
		$.ajax({
			type: "get",
			url: `${baseUrl}/api/v1/chat-details?chatId=${chatId}`,
			async: true,
			xhrFields: {
				withCredentials: true
			},
			success: function(response) {
				resolve(response);
			},
			error: function(error) {
				reject(error);
			}
		});
	});
}

// chatting
export async function getAnswer(content) {
	var chatId = getCookie('chatId');
	var data = { "content": content, "chatId": chatId };

	return new Promise((resolve, reject) => {
		$.ajax({
			type: "post",
			url: baseUrl + "/api/v1/chatting",
			async: true,
			contentType: "application/json",
			data: JSON.stringify(data),
			success: function(response) {
				resolve(response.answer);
			},
			error: function(error) {
				reject(error);
			}
		});
	});
}
