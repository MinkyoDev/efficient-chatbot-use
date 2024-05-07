package com.shinhan.Services;

import java.util.List;

import com.shinhan.DAO.ModelDAO;
import com.shinhan.DTO.ModelDTO;

public class ModelService {

	ModelDAO modelDAO = new ModelDAO();
	
	public List<ModelDTO> getAllModels() {
		return modelDAO.selectAll();
	}
}
