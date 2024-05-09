package com.shinhan.utils;

import javax.servlet.http.HttpServletRequest;

public class URLUtils {

	public static String getAbsoluteURL(HttpServletRequest req) {
		String absolutePath = req.getRequestURL().toString();
		
		int firstSlashIndex = absolutePath.indexOf("/", absolutePath.indexOf("//") + 2);
        int secondSlashIndex = absolutePath.indexOf("/", firstSlashIndex+1);
        
        String desiredPath = absolutePath.substring(0, secondSlashIndex);
        return desiredPath;
	}
}
