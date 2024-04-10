package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DTO.UserDTO;
import utils.DBUtil;

public class UserDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	
	public int insertUser(UserDTO user) {
		int result = 0;
		String sql = "insert into users (id, password, is_active) values (?, ?, ?)";
		conn = DBUtil.dbConnection();		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getId());
			pst.setString(2, user.getPassword());
			pst.setBoolean(3, user.isIs_active());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	public int updateUser(UserDTO user) {
		int result = 0;
		String sql = "update users set password = ?, is_active = 1 where id = ?";
		conn = DBUtil.dbConnection();		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getPassword());
			pst.setString(2, user.getId());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	public List<UserDTO> selectAllByID(String id) {
		List<UserDTO> userlist = new ArrayList<>();
		String sql = "select * from users where id = ?";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				UserDTO user = makeUser(rs);
				userlist.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return userlist;
	}

	public List<UserDTO> selectByIDandPassword(String id, String password) {
		List<UserDTO> userlist = new ArrayList<>();
		String sql = "select * from users where id = ? and password = ? and is_active = 1";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.setString(2, password);
			rs = pst.executeQuery();
			while (rs.next()) {
				UserDTO user = makeUser(rs);
				userlist.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return userlist;
	}
	
	public int deleteByID(String id) {
		int result = 0;
		String sql = "update users set is_active = 0 where id = ?";
		conn = DBUtil.dbConnection();		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	private UserDTO makeUser(ResultSet rs) throws SQLException {
		UserDTO user = new UserDTO();
		user.setId(rs.getString("id"));
		user.setPassword(rs.getString("password"));
		user.setIs_active(rs.getBoolean("is_active"));
		return user;
	}


}
