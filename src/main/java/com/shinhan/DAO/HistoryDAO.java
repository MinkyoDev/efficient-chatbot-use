package com.shinhan.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.DTO.HistoryDTO;
import com.shinhan.utils.DBUtil;

public class HistoryDAO {

	Connection conn;
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	
	public int insertHistory(HistoryDTO history, int chatid) {
		int result = 0;
		String sql ="insert into history (history_id, chat_id, deps, summary, prompt_tokens, completion_tokens) "
				+ "values (hisid_seq.nextVal, ?, 0, ?, ?, ?)";
		conn = DBUtil.dbConnection();		
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			pst.setString(2, history.getSummary());
			pst.setInt(3, history.getPrompt_tokens());
			pst.setInt(4, history.getCompletion_tokens());
			result = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		return result;
	}
	
	public List<String> selectByChatID(int chatid) {
		List<String> summaryList = new ArrayList<>();
		String sql = "select summary from history where chat_id = ?";
		conn = DBUtil.dbConnection();
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chatid);
			rs = pst.executeQuery();
			while (rs.next()) {
				summaryList.add(rs.getString("summary"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return summaryList;
	}
}
