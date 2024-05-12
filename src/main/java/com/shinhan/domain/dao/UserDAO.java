package com.shinhan.domain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.domain.dto.UserDTO;
import com.shinhan.utils.DBUtil;

public class UserDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	
	// insert
	public int insertUser(UserDTO user) {
		int result = 0;
		String sql = "insert into users (email, nicname, password, is_active) values (?, ?, ?, ?)";
		conn = DBUtil.dbConnection();		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getEmail());
			pst.setString(2, user.getNicname());
			pst.setString(3, user.getPassword());
			pst.setBoolean(4, user.is_active());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	// update
	public int updateUser(UserDTO user) {
		int result = 0;
		String sql = "update users set password = ?, is_active = 1 where email = ?";
		conn = DBUtil.dbConnection();		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getPassword());
			pst.setString(2, user.getEmail());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	// select
	public UserDTO selectAllByEmail(String Email) {
		UserDTO user = null;
		String sql = "select * from users where email = ?";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, Email);
			rs = pst.executeQuery();
			while (rs.next()) {
				user = makeUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return user;
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
	
	// delete
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
	
	public UserDTO makeUser(ResultSet rs) throws SQLException {
		UserDTO user = new UserDTO();
		user.setEmail(rs.getString("email"));
		user.setNicname(rs.getString("nicname"));
		user.setPassword(rs.getString("password"));
		user.set_active(rs.getBoolean("is_active"));
		return user;
	}
	
	public UserDTO makeUser(String email, String nicname, String password) {
		UserDTO user = new UserDTO();
		user.setEmail(email);
		user.setNicname(nicname);
		user.setPassword(password);
		return user;
	}


}
