
import { signUp } from './sign_up.js';
import { signIn } from './sign_in.js';
import { changePw } from './change_pw.js';


$(document).ready(function() {
	signUp();
	signIn();
	changePw();
	change_input_style();
	etc();
});

function change_input_style() {
	$('.input-wrapper input').focus(function() {
		$(this).addClass('input-focus-or-valid');
		$(this).next('label').find('span').addClass('input-focus-or-valid-label');
	});

	$('.input-wrapper input').blur(function() {
		if ($(this).val().trim() !== '') {
			$(this).addClass('input-focus-or-valid');
			$(this).next('label').find('span').addClass("input-focus-or-valid-label")
		} else {
			$(this).removeClass('input-focus-or-valid');
			$(this).next('label').find('span').removeClass("input-focus-or-valid-label")
		}
	});
}

function etc() {
	$("#google-login").click(function() {
		alert("구글 로그인은 아직 지원하지 않습니다.");
	})
}