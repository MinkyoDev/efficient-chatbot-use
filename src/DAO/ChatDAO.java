package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DTO.ChatDTO;
import DTO.LogDTO;
import utils.DBUtil;

public class ChatDAO {
	
	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	
	public int insertChat(ChatDTO chat) {
	    int generatedChatId = 0;
	    String sql = "INSERT INTO chat (chat_id, user_id, memory_enabled, ceche_enabled) "
	            + "VALUES (chatid_seq.nextVal, ?, ?, ?)";
	    conn = DBUtil.dbConnection();
	    try {
	        pst = conn.prepareStatement(sql, new String[] { "chat_id" });
	        pst.setString(1, chat.getUser_id());
	        pst.setBoolean(2, chat.isMemory_enabled());
	        pst.setBoolean(3, chat.isCeche_enabled());
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
	
	public List<ChatDTO> selectByChatid(String chatid) {
		List<ChatDTO> chatLogList = new ArrayList<>();
		String sql = "select * from chat_log where chat_id = ?";
		conn = DBUtil.dbConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, chatid);
			rs = pst.executeQuery();
			while (rs.next()) {
				ChatDTO chat = makeChat(rs);
				chatLogList.add(chat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return chatLogList;
	}

	public int deleteChat(int chatid) {
		int result = 0;
	    String sql = "DELETE FROM chat WHERE chat_id = ?";
	    conn = DBUtil.dbConnection();
	    try {
	        pst = conn.prepareStatement(sql);
	        pst.setInt(1, chatid);
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
		chat.setName(rs.getString("name"));
		chat.setMemory_enabled(rs.getBoolean("memory_enabled"));
		chat.setCeche_enabled(rs.getBoolean("ceche_enabled"));
		return chat;
	}
	
	private LogDTO makeLog(ResultSet rs) throws SQLException {
		LogDTO log = new LogDTO();
		log.setChat_id(rs.getInt("chat_id"));
		log.setUser_id(rs.getString("user_id"));
		log.setRequest(rs.getString("request"));
		log.setResponse(rs.getString("response"));
		return log;
	}


}
