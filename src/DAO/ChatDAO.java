package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DTO.ChatDTO;
import utils.DBUtil;

public class ChatDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;

	public int insertChat(ChatDTO chat) {
		int generatedChatId = 0;
		String sql = "INSERT INTO chat (chat_id, user_id, model, stream_enabled, memory_enabled, ceche_enabled) "
				+ "VALUES (chatid_seq.nextVal, ?, ?, ?, ?, ?)";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql, new String[] { "chat_id" });
			pst.setString(1, chat.getUser_id());
			pst.setString(2, chat.getModel());
			pst.setBoolean(3, chat.isStream_enabled());
			pst.setBoolean(4, chat.isMemory_enabled());
			pst.setBoolean(5, chat.isCeche_enabled());
			int result = pst.executeUpdate();

			if (result > 0) {
				rs = pst.getGeneratedKeys();
				if (rs.next()) {
					generatedChatId = rs.getBigDecimal(1).intValueExact();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return generatedChatId;
	}

	// select
	public List<ChatDTO> selectAll(String userid) {
		List<ChatDTO> chatList = new ArrayList<>();
		String sql = "select * from chat where user_id = ?";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			rs = pst.executeQuery();
			while (rs.next()) {
				ChatDTO chat = makeChat(rs);
				chatList.add(chat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return chatList;
	}

	public ChatDTO selectByChatid(int chatid) {
		ChatDTO chat = new ChatDTO();
		String sql = "select * from chat where chat_id = ?";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			if (rs.next()) {
	            chat = makeChat(rs);
	        } 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return chat;
	}

	// update
	public int updateName(int chatid, String name) {
		int result = 0;
		String sql = "update chat set name = ? where chat_id = ?";
		conn = DBUtil.dbConnection();		
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, chatid);
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	// delete
	public int deleteChat(String userid, int chatid) {
		int result = 0;
		String sql = "DELETE FROM chat WHERE chat_id = ? and user_id = ?";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			pst.setString(2, userid);
			result = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, null);
		}
		return result;
	}

	public ChatDTO makeChat(ResultSet rs) throws SQLException {
		ChatDTO chat = new ChatDTO();
		chat.setChat_id(rs.getInt("chat_id"));
		chat.setUser_id(rs.getString("user_id"));
		chat.setModel(rs.getString("model"));
		chat.setName(rs.getString("name"));
		chat.setStream_enabled(rs.getBoolean("stream_enabled"));
		chat.setMemory_enabled(rs.getBoolean("memory_enabled"));
		chat.setCeche_enabled(rs.getBoolean("ceche_enabled"));
		return chat;
	}


}
