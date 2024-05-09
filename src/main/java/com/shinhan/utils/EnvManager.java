package com.shinhan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvManager {
	public static String getProperty(String localPath, String key) {
		Properties property = new Properties();
		File file = new File(localPath + "WEB-INF" + File.separator + "env.properties");
		try (InputStream straem = new FileInputStream(file)) {
			property.load(straem);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return property.getProperty(key);
	}
}
