package com.shinhan.controller.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.shinhan.domain.dto.ModelDTO;
import com.shinhan.service.ModelService;

@WebServlet("/api/v1/model-list")
public class ModelListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<ModelDTO> modelList = new ModelService().getAllModels();
		
		JSONArray modelArray = new JSONArray();
		for (ModelDTO model: modelList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", model.getName());			
			jsonObject.put("company", model.getCompany());			
			modelArray.add(jsonObject);
		}
		
		JSONObject responseData = new JSONObject();
		responseData.put("models", modelArray);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseData.toJSONString());
	}

}
