import { getAnswer, getModelList, getChatList, getDetails } from "./connections.js";
import { getSideVarDisplay } from "./utils.js";
import { makeBallons } from "./chatting.js";

console.log(getSideVarDisplay());
var side_var_enabled = true;
var icon_path = "images/icons"
var assistantCount = 0;

$(document).ready(function() {
	// component moving
	autoTextareaHeight();
	initSideVar();

	//event
	buttonClicks();

	toggles();

	// load data
	loadData();
	// loadPreviousData();

	// loadChatList();

	// loadDetailIcon();


});

function initSideVar() {
	console.log("initSideVar");
	if (!side_var_enabled) {
		$("#side-bar").css("transform", "translate(var(--side-bar-transform), 0)");
		$("#side-bar").css("left", "0");
		$("#temp").css("width", "var(--side-bar-width)");
		side_var_enabled = true;
	} else {
		$("#side-bar").css("transform", "translate(calc(var(--side-bar-width) * -1), 0)");
		$("#side-bar").css("left", "0");
		$("#temp").css("width", "0");
		side_var_enabled = false;
	}
}

function autoTextareaHeight() {
	$('#prompt-input').on('input', function() {
		var padding = parseFloat($(this).css('padding-top')) + parseFloat($(this).css('padding-bottom'));
		$(this).css('height', 'auto').height((this.scrollHeight - padding) + 'px');
	});
}

function buttonClicks() {
	// side menu button
	$("#side-btn").click(toggleSidebar);

	function toggleSidebar() {
		if (!side_var_enabled) {
			$("#side-bar").css("transform", "translate(var(--side-bar-transform), 0)");
			$("#temp").css("width", "var(--side-bar-width)");
			side_var_enabled = true;
		} else {
			$("#side-bar").css("transform", "translate(calc(var(--side-bar-width) * -1), 0)");
			$("#temp").css("width", "0");
			side_var_enabled = false;
		}
	}

	// log out button
	$("#logout-btn").click(function() {
		window.location.href = "auth/logout";
	});

	// new chat button
	$("#new-chat-btn").click(function() {
		$("#new-chat-modal").addClass("appear");
		$("#new-chat-modal").removeClass("disappear");
	})

	// new chat modal cancel button
	$("#modal-cancel").click(function() {
		modalClose();
	})

	$("#new-chat-modal").click(function(event) {
		if (!$(event.target).closest(".modal-body").length) {
			modalClose();
		}
	})

	// new chat modal create button
	$("#create-btn").click(function() {
		var modelName = $("#model-select").val();
		var currentUrl = window.location.href;
		var newUrl = currentUrl + "/new-chat?modelName=" + encodeURIComponent(modelName);
		window.location.href = newUrl;
	})

	function modalClose() {
		const target = $("#new-chat-modal");
		if (target.hasClass('appear')) {
			target.addClass('disappear');
			setTimeout(function() {
				target.removeClass('appear');
			}, 101);
		}
	}

	// send prompt button
	$('#prompt-button').on('click', function() {
		chatRequest();
	});

	$('#prompt-input').on('keydown', function(event) {
		if (event.keyCode == 13 && !event.shiftKey) {
			event.preventDefault();
			chatRequest();
		}
	});
}

function toggles() {
	var isOpen_detail = false;
	var isOpen_userMenu = false;

	// model detail toggle
	$(document).click(function(event) {
		var $target = $(event.target);
		if ((isOpen_detail && !$target.is('#model-detail-btn')) && $target.closest('#detail-box').length <= 0) {
			$('#detail-box').fadeOut(100);
			isOpen_detail = false;
		}
		event.stopPropagation();
	});

	$('#model-detail-btn').click(function() {
		event.stopPropagation();
		if (!isOpen_detail) {
			$('#detail-box').css({ opacity: 0, display: 'flex' }).animate({
				opacity: 1
			}, 100);
			isOpen_detail = true;
		} else {
			$('#detail-box').fadeOut(100);
			isOpen_detail = false;
		}
	});

	// user menu toggle
	$(document).click(function(event) {
		var $target = $(event.target);
		if ((isOpen_userMenu && !$target.is('#user-btn')) && $target.closest('#user-setting-box').length <= 0) {
			$('#user-setting-box').fadeOut(100);
			isOpen_userMenu = false;
		}
		event.stopPropagation();
	});

	$('#user-btn').click(function() {
		event.stopPropagation();
		if (!isOpen_userMenu) {
			$('#user-setting-box').css({ opacity: 0, display: 'flex' }).animate({
				opacity: 1
			}, 100);
			isOpen_userMenu = true;
		} else {
			$('#user-setting-box').fadeOut(100);
			isOpen_userMenu = false;
		}
	});
}

function loadData() {
	loadModelList();
	loadChatList();
	loadDetails();

	async function loadModelList() {
		var modelList = await getModelList();
		var options = "";

		modelList.models.forEach(function(item) {
			var modelName = item.name;
			options += `<option value='${modelName}'>${modelName}</option>`
		})
		$("#model-select").html(options)
	}

	async function loadChatList() {
		var chatlList = await getChatList();
		console.log(chatlList);

		var chatListHtml = "";
		chatlList.chats.forEach(function(item) {
			var chatName = item.name;
			if (item.name == null) {
				chatName = "시작!";
			}
			chatListHtml += `<li><button class="mgray-hover" data-value="${item.chatId}">${chatName}</button></li>`;
		})
		$("#chat-list").html(chatListHtml);

		// chat list button
		$(document).ready(function() {
			$("#chat-list li button").click(function() {
				var chatId = $(this).data("value");
				var currentUrl = window.location.href;
				var newUrl = currentUrl + "/move-chat?chatId=" + encodeURIComponent(chatId);
				window.location.href = newUrl;
			});
		});

	}

	async function loadDetails() {
		var details = await getDetails();

		// load previous chat
		details.conversations.forEach(function(item) {
			$("#new-chat-text").hide();
			makeBallons("user", item.request);
			makeBallons("assistant", item.response);
		});

		// load chat detail
		var detailHTML = "";
		for (var key in details.settings) {
			var type = "deny";
			if (key !== "modelName") {
				if (details.settings[key]) {
					type = "check";
				}
				detailHTML += `<div id=${key}><img class="detail-icons" id="${key}-icon" src="images/icons/${key}.png"><span>${capitalizeFirstLetter(key)}</span><img class="status-icon" src="images/icons/${type}.png"></div>`
			}
		}
		detailHTML += '<div id="delete"><button id="delete-btn" class="lgray-hover">Delete Chat</button></div>'
		$("#detail-box").html(detailHTML);

		// change model name
		var modelName = details.settings.modelName.replace("gpt", "GPT")
		$("#model-detail-btn span").html(modelName);
	}
}


function chatRequest() {
	$("#new-chat-text").hide();
	var prompt = $('#prompt-input').val().trim();
	console.log(prompt);
	if (prompt == "" || $("#prompt-button").prop("disabled")) {
		return;
	}
	var assistantId = createDivUniqueId();
	makeBallons("user", prompt);
	makeBallons("loding", "", assistantId);

	console.log("start");
	$("#prompt-button").prop("disabled", true);
	answerCallback(prompt, assistantId);

	console.log("end");
	$("html, body").animate({ scrollTop: $(document).height() }, "slow");
	$('#prompt-input').val("");
	$('#prompt-input').css('height', 'auto')
}

function createDivUniqueId() {
	assistantCount++;
	return 'assistant_' + assistantCount;
}

async function answerCallback(prompt, assistantId) {
	var answer = await getAnswer(prompt);
	console.log(answer);
	$("#prompt-button").prop("disabled", false);
	$(`#${assistantId}`).html(answer);
}

function capitalizeFirstLetter(str) {
	if (str.length === 0) {
		return str;
	}
	return str.charAt(0).toUpperCase() + str.slice(1);
}