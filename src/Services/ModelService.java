package services;

import java.util.List;

import DAO.ModelDAO;
import DTO.ModelDTO;

public class ModelService {

	ModelDAO modelDAO = new ModelDAO();
	
	public List<ModelDTO> getAllModels() {
		return modelDAO.selectAll();
	}
}
