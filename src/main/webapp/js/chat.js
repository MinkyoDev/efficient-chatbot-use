import { getAnswer, getModelList, getChatList, getDetails, getUserData } from "./connections.js";
import { getSideVarDisplay, setSideVarDisplay, getCookie, getParentPath } from "./utils.js";
import { makeBallons } from "./chatting.js";


var side_var_enabled = getSideVarDisplay();


$(document).ready(function() {
	// component moving
	autoTextareaHeight();
	initSideVar();

	//event
	buttonClicks();

	toggles();

	// load data
	loadData();

});

function initSideVar() {
	if (side_var_enabled) {
		$("#side-bar").css("left", "var(--side-bar-transform)");
		$("#temp").css("width", "var(--side-bar-width)");
		side_var_enabled = true;
	} else {
		$("#side-bar").css("left", "calc(var(--side-bar-width) * -1)");
		$("#temp").css("width", "0");
		side_var_enabled = false;
	}
	$("#side-bar").css("transition", "left .2s");
	$("#temp").css("transition", "left .2s");
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
			$("#side-bar").css("left", "var(--side-bar-transform)");
			$("#temp").css("width", "var(--side-bar-width)");
			side_var_enabled = true;
			setSideVarDisplay(true);
		} else {
			$("#side-bar").css("left", "calc(var(--side-bar-width) * -1)");
			$("#temp").css("width", "0");
			side_var_enabled = false;
			setSideVarDisplay(false);
		}
	}

	// log out button
	$("#logout-btn").click(function() {
		window.location.href = getParentPath(window.location.href, 2) + "/auth/logout";
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
		window.location.href = getParentPath(window.location.href, 1) + "/new-chat?modelName=" + encodeURIComponent(modelName);
	})
	
	// setting modal create button
	$("#setting-btn").click(function() {
		$("#setting-modal").addClass("appear");
		$("#setting-modal").removeClass("disappear");
	})

	// new chat modal cancel button
	$(".modal-cancel").click(function() {
		modalClose();
	})

	$("#setting-modal").click(function(event) {
		if (!$(event.target).closest(".modal-body").length) {
			modalClose();
		}
	})

	function modalClose() {
		const target = $(".modal");
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
	
	// change password button
	$("#change-pw-btn").click(function(){
		console.log("change-pw-btn");
		console.log(getParentPath(window.location.href, 2) + "/auth/change-password");
		window.location.href = getParentPath(window.location.href, 2) + "/auth/change-password";
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
	loadUserData();
	loadModelList();
	loadChatList();
	loadDetails();

	async function loadUserData() {
		var userData = await getUserData();
		$("#user-btn span").html(userData.userNicname);
	}

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

		var chatListHtml = "";
		chatlList.chats.forEach(function(item) {
			var chatName = item.name;
			if (item.name == null) {
				chatName = "시작!";
			}
			chatListHtml += `<li><button class="mgray-hover" data-value="${item.chatId}">${chatName}</button></li>`;
		})
		$("#chat-list").html(chatListHtml);

		$("#chat-list li button").each(function() {
			var chatId = getCookie('chatId');
			var value = $(this).data("value")
			if (value == chatId) {
				$(this).prop("disabled", true);
			}
		});

		// chat list button
		$(document).ready(function() {
			$("#chat-list li button").click(function() {
				var chatId = $(this).data("value");
				window.location.href = getParentPath(window.location.href, 1) + "/move-chat?chatId=" + encodeURIComponent(chatId);
			});
		});

	}

	async function loadDetails() {
		var details = await getDetails();

		// load previous chat
		details.conversations.forEach(function(item) {
			$("#new-chat-text").hide();
			makeBallons("user", item.request, false);
			makeBallons("assistant", item.response, false);

			// scroll down
			$("html, body").animate({ scrollTop: $(document).height() }, 0);
		});

		// load chat detail
		var detailHTML = "";
		for (var key in details.settings) {
			var type = "deny";
			if (key !== "modelName") {
				if (details.settings[key]) {
					type = "check";
				}
				detailHTML += `<div id=${key}><img class="detail-icons" id="${key}-icon" src="../images/icons/${key}.png"><span>${capitalizeFirstLetter(key)}</span><img class="status-icon" src="../images/icons/${type}.png"></div>`
			}
		}
		detailHTML += '<div id="delete"><button id="delete-btn" class="lgray-hover">Delete Chat</button></div>'
		$("#detail-box").html(detailHTML);

		// delete chat button
		$(document).ready(function() {
			$("#delete-btn").click(function() {
				var chatId = getCookie('chatId');
				window.location.href = getParentPath(window.location.href, 1) + "/delete-chat?chatId=" + encodeURIComponent(chatId);
			});
		});

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
	makeBallons("user", prompt);
	var ballonId = makeBallons("loding", "");

	console.log("start");
	$("#prompt-button").prop("disabled", true);
	answerCallback(prompt, ballonId);

	console.log("end");

	// scroll down
	$("html, body").animate({ scrollTop: $(document).height() }, "slow");

	$('#prompt-input').val("");
	$('#prompt-input').css('height', 'auto')
}



async function answerCallback(prompt, assistantId) {
	var answer = await getAnswer(prompt);
	console.log(answer);
	$("#prompt-button").prop("disabled", false);
	$(`#${assistantId}`).html(answer);

	// scroll down
	$("html, body").animate({ scrollTop: $(document).height() }, "slow");
}

function capitalizeFirstLetter(str) {
	if (str.length === 0) {
		return str;
	}
	return str.charAt(0).toUpperCase() + str.slice(1);
}