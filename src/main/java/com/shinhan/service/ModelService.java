package com.shinhan.service;

import java.util.List;

import com.shinhan.domain.dao.ModelDAO;
import com.shinhan.domain.dto.ModelDTO;

public class ModelService {

	ModelDAO modelDAO = new ModelDAO();
	
	public List<ModelDTO> getAllModels() {
		return modelDAO.selectAll();
	}
}
