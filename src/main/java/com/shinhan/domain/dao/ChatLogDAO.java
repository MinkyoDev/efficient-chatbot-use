package com.shinhan.domain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.domain.dto.ChatLogDTO;
import com.shinhan.utils.DBUtil;

public class ChatLogDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;

	// insert
	public int insertChatLog(ChatLogDTO chatLog) {
		int result = 0;
		String sql = "insert into chat_log (log_id, chat_id, request, response, prompt_tokens, completion_tokens) "
				+ "values (logid_seq.nextVal, ?, ?, ?, ?, ?)";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatLog.getChat_id());
			pst.setString(2, chatLog.getRequest());
			pst.setString(3, chatLog.getResponse());
			pst.setInt(4, chatLog.getPrompt_tokens());
			pst.setInt(5, chatLog.getCompletion_tokens());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}

	// select
//	public List<LogDTO> selectAllByChatIDUserID(String userid, int chatid) {
//		List<LogDTO> loglist = new ArrayList<>();
//		String sql = "select * from chat_log join Chat using (chat_id) where chat_id = ? and user_id = ?";
//		conn = DBUtil.dbConnection();
//		try {
//			conn.setAutoCommit(false);
//			pst = conn.prepareStatement(sql);
//			pst.setInt(1, chatid);
//			pst.setString(2, userid);
//			rs = pst.executeQuery();
//			while (rs.next()) {
//				LogDTO log = makeLog(rs);
//				loglist.add(log);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBUtil.dbDisconnect(conn, st, rs);
//		}
//		return loglist;
//	}
//
	public List<ChatLogDTO> selectByChatIDisNull(int chatid) {
		List<ChatLogDTO> loglist = new ArrayList<>();
		String sql = "select * from chat_log join Chat using (chat_id) where chat_id = ? and history_id is null order by create_at";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			while (rs.next()) {
				ChatLogDTO log = makeChatLog(rs);
				loglist.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return loglist;
	}
	
	public List<ChatLogDTO> selectLogByChatIDandUserEmail(int chatId, String userEmail){
		List<ChatLogDTO> loglist = new ArrayList<>();
		String sql = "SELECT * FROM Chat_log WHERE chat_id = ? AND EXISTS (SELECT 1 FROM Chat WHERE user_email = ? AND Chat.chat_id = Chat_log.chat_id)";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatId);
			pst.setString(2, userEmail);
			rs = pst.executeQuery();
			while (rs.next()) {
				ChatLogDTO log = makeChatLog(rs);
				loglist.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return loglist;
	}

//	public List<ChatLogDTO> selectTopNByChatID(int chatid, int N) {
//		List<ChatLogDTO> loglist = new ArrayList<>();
//		String sql = "select * from(select * from chat_log where chat_id = ? order by create_at desc) logs where rownum<=? order by logs.create_at asc";
//		conn = DBUtil.dbConnection();
//		try {
//			conn.setAutoCommit(false);
//			pst = conn.prepareStatement(sql);
//			pst.setInt(1, chatid);
//			pst.setInt(2, N);
//			rs = pst.executeQuery();
//			while (rs.next()) {
//				ChatLogDTO log = makeChatLog(rs);
//				loglist.add(log);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBUtil.dbDisconnect(conn, st, rs);
//		}
//		return loglist;
//	}
//
//	public LogDTO selectByRequest(int chatid, String input) {
//		LogDTO chat = null;
//		String sql = "select * from chat_log where chat_id = ? and request = ?";
//		conn = DBUtil.dbConnection();
//		try {
//			conn.setAutoCommit(false);
//			pst = conn.prepareStatement(sql);
//			pst.setInt(1, chatid);
//			pst.setString(2, input);
//			rs = pst.executeQuery();
//			if (rs.next()) {
//	            chat = makeLog(rs);
//	        } 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBUtil.dbDisconnect(conn, st, rs);
//		}
//		return chat;
//	}

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
	
	public int countChatLogByChatId(int chatid) {
		int count = 0;
		String sql = "SELECT COUNT(*) count FROM Chat_log WHERE chat_id = ?";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			rs.next();
			count = rs.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return count;
	}

	private ChatLogDTO makeChatLog(ResultSet rs) throws SQLException {
		ChatLogDTO chatLog = new ChatLogDTO();
		chatLog.setLog_id(rs.getInt("log_id"));
		chatLog.setChat_id(rs.getInt("chat_id"));
		chatLog.setHistory_id(rs.getInt("history_id"));
		chatLog.setRequest(rs.getString("request"));
		chatLog.setResponse(rs.getString("response"));
		chatLog.setPrompt_tokens(rs.getInt("prompt_tokens"));
		chatLog.setCompletion_tokens(rs.getInt("completion_tokens"));
		return chatLog;
	}
	
	public ChatLogDTO makeChatLog(int chatId, String request, String response, int promptTokens, int completionTokens) {
		ChatLogDTO chatLog = new ChatLogDTO();
		chatLog.setChat_id(chatId);
		chatLog.setRequest(request);
		chatLog.setResponse(response);
		chatLog.setPrompt_tokens(promptTokens);
		chatLog.setCompletion_tokens(completionTokens);
		return chatLog;
	}

}
