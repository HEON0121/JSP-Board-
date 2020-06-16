package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	
	public BbsDAO() {
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/BBS?characterEncoding=UTF-8&serverTimezone=UTC"; 
			String id = "root";
			String pwd = "12341234";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	//작성 시간 
	public String getDate() {
		String sql = "select now()"; // 현재 시간 가져오는 sql쿼리 
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} return ""; // 데이터베이스 오류 
	}
	//게시글 목차 번호   
	public int getNext() {
		String sql = "select bbsID from bbs order by bbsID desc";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
			return 1; // 첫번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		} return -1; // 데이터베이스 오류 
	}
	//글 작성 
	public int write(String bbsTitle, String userID, String bbsContent) {
		String sql = "insert into bbs values (?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, getNext());
			ps.setString(2, bbsTitle);
			ps.setString(3, userID);
			ps.setString(4, getDate());
			ps.setString(5, bbsContent);
			ps.setInt(6, 1);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} return -1; // 데이터 베이스 오류 
	}
	
	public ArrayList<BbsBeans> getList(int pageNumber){
		String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		ArrayList<BbsBeans> list = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNext() - (pageNumber - 1) * 10 );
			rs = ps.executeQuery();
			while(rs.next()) {
				BbsBeans bbs = new BbsBeans();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} return list;
	}
	
	public boolean nextPage(int pageNumber) {
		String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getNext() - (pageNumber - 1) * 10 );
			rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} return false;
	}
	
	public BbsBeans  getBbs(int bbsID) {
		String sql = "select * from bbs where bbsID = ? ";
		ArrayList<BbsBeans> list = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, bbsID);
			rs = ps.executeQuery();
			if(rs.next()) {
				BbsBeans bbs = new BbsBeans();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} return null;
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String sql = "update bbs set bbsTitle = ?,  bbsContent = ? where bbsID = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, bbsTitle);
			ps.setString(2, bbsContent);
			ps.setInt(3, bbsID);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} return -1; // 데이터 베이스 오류 
	}
	
	public int delete(int bbsID) {
		String sql = "update bbs set bbsAvailable = 0 where bbsID = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, bbsID);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} return -1; // 데이터 베이스 오류 
	}
	
	
//	private void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
//		try {
//			if(rs!=null) rs.close();
//			if(ps!=null) ps.close();
//			if(conn!=null) conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//	}
	
	
}
