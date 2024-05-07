
$(document).ready(function () {
    change_input_style();
    check_submit();
    etc();
});

function check_submit() {
    $("form").submit(function (event) {
        var email = $('#email').val();
        var pw = $('#password').val();
        
        $("#email-alter").css("display", "none");
        $("#pw-alter").css("display", "none");

        if (email == "" && pw == "") {
            $("#email-alter").text("Email을 입력해 주세요.");
            $("#pw-alter").text("비밀번호를 입력해 주세요.");
            $("#email-alter").css("display", "block");
            $("#pw-alter").css("display", "block");
            event.preventDefault();
            return;
        }

        if (email == "") {
            $("#email-alter").text("Email을 입력해 주세요.");
            $("#email-alter").css("display", "block");
            event.preventDefault();
            return;
        }
        
        if (pw == "") {
            $("#pw-alter").text("비밀번호를 입력해 주세요.");
            $("#pw-alter").css("display", "block");
            event.preventDefault();
            return;
        }
    });
}

function change_input_style() {
    $('.input-wrapper input').focus(function () {
        $(this).addClass('input-focus-or-valid');
        $(this).next('label').find('span').addClass('input-focus-or-valid-label');
    });

    $('.input-wrapper input').blur(function () {
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
    $("#google-login").click(function(){
        alert("구글 로그인은 몰라");
    })
}