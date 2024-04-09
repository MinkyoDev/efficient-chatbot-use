package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DTO.ChatDTO;
import DTO.HistoryDTO;
import DTO.LogDTO;
import utils.DBUtil;

public class ChatDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;

	public int insertChatLog(ChatDTO chat) {
		int result = 0;
		String sql = "insert into chat_log (log_id, chat_id, model_name, request, response, prompt_tokens, completion_tokens) "
				+ "values (chatid_seq.nextVal, 1, 'gpt-3.5-turbo-1106', ?, ?, ?, ?)";
		conn = DBUtil.dbConnection();		
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setString(1, chat.getRequest());
			pst.setString(2, chat.getResponse());
			pst.setInt(3, chat.getPrompt_tokens());
			pst.setInt(4, chat.getCompletion_tokens());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
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
			System.out.println(totalTokens);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return totalTokens;
	}
	
	public List<LogDTO> selectAllByChatID(int chatid) {
		List<LogDTO> loglist = new ArrayList<>();
		String sql = "select * from chat_log join Chat using (chat_id) where chat_id = ?";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			while (rs.next()) {
				LogDTO log = makeLogPrint(rs);
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
				LogDTO log = makeLogPrint(rs);
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
		String sql = "select logs.user_id, logs.chat_id, logs.request, logs.response "
				+ "from(select * from chat_log join Chat using (chat_id) where chat_id = ? order by create_at desc) logs where rownum<=? order by logs.create_at asc";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			pst.setInt(2, N);
			rs = pst.executeQuery();
			while (rs.next()) {
				LogDTO log = makeLogPrint(rs);
				loglist.add(log);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return loglist;
	}

	private LogDTO makeLogPrint(ResultSet rs) throws SQLException {
		LogDTO log = new LogDTO();
		log.setChat_id(rs.getInt("chat_id"));
		log.setUser_id(rs.getString("user_id"));
		log.setRequest(rs.getString("request"));
		log.setResponse(rs.getString("response"));
		return log;
	}
}
