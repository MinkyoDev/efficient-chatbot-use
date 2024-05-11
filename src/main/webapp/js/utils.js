
export function getCookieValue(cookieName) {
	var name = cookieName + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var cookieArray = decodedCookie.split(';');
	for (var i = 0; i < cookieArray.length; i++) {
		var cookie = cookieArray[i].trim();
		if (cookie.indexOf(name) == 0) {
			return cookie.substring(name.length, cookie.length);
		}
	}
	return "";
}

export function getSideVarDisplay() {
	var name = "svDisplay=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var cookieArray = decodedCookie.split(';');
	for (var i = 0; i < cookieArray.length; i++) {
		var cookie = cookieArray[i].trim();
		if (cookie.indexOf(name) == 0) {
			return cookie.substring(name.length, cookie.length) === "true";
		}
	}
	var expirationDate = new Date();
	expirationDate.setTime(expirationDate.getTime() + (24 * 60 * 60 * 1000));
	var expires = "expires=" + expirationDate.toUTCString();

	document.cookie = "svDisplay=false; " + expires;
	return false;
}

// 쿠키에서 myCookie 값을 가져옴
var cookieValue = getCookieValue("myCookie");

// 문자열 "true"나 "false"를 실제 boolean 값으로 변환
var myBooleanValue = cookieValue === "true";

// myBooleanValue를 사용하여 작업 수행
if (myBooleanValue) {
	// true일 때 실행할 코드
} else {
	// false일 때 실행할 코드
}
