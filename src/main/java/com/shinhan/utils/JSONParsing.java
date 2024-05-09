package com.shinhan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONParsing {

	public JSONObject JSONParse(HttpServletRequest request) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuilder jsonBody = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			jsonBody.append(line);
		}
		
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsonBody.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return json;
	}
}
