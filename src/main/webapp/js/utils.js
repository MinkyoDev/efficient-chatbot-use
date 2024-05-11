export function getCookie(name) {
	var cookies = document.cookie.split(';');
	for (var i = 0; i < cookies.length; i++) {
		var cookie = cookies[i].trim();
		if (cookie.startsWith(name + '=')) {
			return cookie.substring(name.length + 1);
		}
	}
	return null;
}

export function getAbsoluteURL(currentUrl) {
	var firstSlashIndex = currentUrl.indexOf('/', currentUrl.indexOf('//') + 2);
	var secondSlashIndex = currentUrl.indexOf('/', firstSlashIndex + 1);
	var baseUrl = currentUrl.substring(0, secondSlashIndex);
	return baseUrl;
}

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

export function setSideVarDisplay(bool) {
	document.cookie = "svDisplay=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

	var expirationDate = new Date();
	expirationDate.setTime(expirationDate.getTime() + (24 * 60 * 60 * 1000));
	var expires = "expires=" + expirationDate.toUTCString();

	console.log(`svDisplay=${bool}; ` + expires);
	if (bool) {
		document.cookie = "svDisplay=true; " + expires;
	} else {
		document.cookie = "svDisplay=false; " + expires;
	}
}

export function getParentPath(currentUrl, num) {
	var urlArray = currentUrl.split('/');
	for (let i = 0; i < num; i++) {
		urlArray.pop();
	}
	return urlArray.join('/');
}