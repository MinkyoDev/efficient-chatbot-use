package com.shinhan.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

public class CookieUtils {

	public static Map<String, String> cookiesToMap(Cookie[] cookies) {
		Map<String, String> cookieMap = new HashMap<>();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				cookieMap.put(name, value);
			}
		}

		return cookieMap;
	}
}
