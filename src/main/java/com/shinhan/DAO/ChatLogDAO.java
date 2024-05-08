package com.shinhan.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.DTO.ChatLogDTO;
import com.shinhan.DTO.LogDTO;
import com.shinhan.utils.DBUtil;

public class ChatLogDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;

	// insert
	public int insertChatLog(ChatLogDTO chat) {
		int result = 0;
		String sql = "insert into chat_log (log_id, chat_id, request, response, prompt_tokens, completion_tokens) "
				+ "values (logid_seq.nextVal, ?, ?, ?, ?, ?)";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chat.getChat_id());
			pst.setString(2, chat.getRequest());
			pst.setString(3, chat.getResponse());
			pst.setInt(4, chat.getPrompt_tokens());
			pst.setInt(5, chat.getCompletion_tokens());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}

	// select
	public List<LogDTO> selectAllByChatIDUserID(String userid, int chatid) {
		List<LogDTO> loglist = new ArrayList<>();
		String sql = "select * from chat_log join Chat using (chat_id) where chat_id = ? and user_id = ?";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			pst.setString(2, userid);
			rs = pst.executeQuery();
			while (rs.next()) {
				LogDTO log = makeLog(rs);
				loglist.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return loglist;
	}

	public List<LogDTO> selectByChatIDisNull(int chatid) {
		List<LogDTO> loglist = new ArrayList<>();
		String sql = "select * from chat_log join Chat using (chat_id) where chat_id = ? and history_id is null order by create_at";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			while (rs.next()) {
				LogDTO log = makeLog(rs);
				loglist.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return loglist;
	}

	public List<LogDTO> selectTopNByChatID(int chatid, int N) {
		List<LogDTO> loglist = new ArrayList<>();
		String sql = "select * from(select * from chat_log where chat_id = ? order by create_at desc) logs where rownum<=? order by logs.create_at asc";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			pst.setInt(2, N);
			rs = pst.executeQuery();
			while (rs.next()) {
				LogDTO log = makeLog(rs);
				loglist.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return loglist;
	}

	public LogDTO selectByRequest(int chatid, String input) {
		LogDTO chat = null;
		String sql = "select * from chat_log where chat_id = ? and request = ?";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			pst.setString(2, input);
			rs = pst.executeQuery();
			if (rs.next()) {
	            chat = makeLog(rs);
	        } 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return chat;
	}

	// etc
	public int calculateTotalTokens(int chatid) {
		int totalTokens = 0;
		String sql = "select sum(prompt_tokens) + sum(completion_tokens) total_tokens from chat_log where history_id is null and chat_id = ?";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			rs.next();
			totalTokens = rs.getInt("total_tokens");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return totalTokens;
	}

	private LogDTO makeLog(ResultSet rs) throws SQLException {
		LogDTO log = new LogDTO();
		log.setLog_id(rs.getInt("log_id"));
		log.setChat_id(rs.getInt("chat_id"));
		log.setHistory_id(rs.getInt("history_id"));
		log.setRequest(rs.getString("request"));
		log.setResponse(rs.getString("response"));
		log.setPrompt_tokens(rs.getInt("prompt_tokens"));
		log.setCompletion_tokens(rs.getInt("completion_tokens"));
		return log;
	}

}
