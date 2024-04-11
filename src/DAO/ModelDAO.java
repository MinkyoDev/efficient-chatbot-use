package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DTO.ModelDTO;
import utils.DBUtil;

public class ModelDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	
	public List<ModelDTO> selectAll() {
		List<ModelDTO> modelList = new ArrayList<>();
		String sql = "select * from models";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				ModelDTO user = makeModel(rs);
				modelList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return modelList;
	}

	private ModelDTO makeModel(ResultSet rs) throws SQLException {
		ModelDTO model = new ModelDTO();
		model.setName(rs.getString("name"));
		model.setCompany(rs.getString("company"));
		model.setPrice_per_ptoken(rs.getDouble("price_per_ptoken"));
		model.setPrice_per_ctoken(rs.getDouble("price_per_ctoken"));
		return model;
	}
}
