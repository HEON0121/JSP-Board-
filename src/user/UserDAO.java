package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
	public UserDAO() {
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
	//로그인 정보 일치 불일치 가려내는 메소드 
	public int login(String userID, String userPassword) {
		String sql = "select userPassword from user where userID = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, userID);
			rs = ps.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; //로그인 성공 
				}
				else {  
					return 0; //비밀번호 불일치 
				}
					
			}
			return -1; // 아이디가 없음 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn, ps, rs);
		}
		return -2; // 데이터 베이스 오류
	}
	//회원 가입 
	public int join(User user) {
		String sql =  "insert into user values (?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserID());
			ps.setString(2, user.getUserPassword());
			ps.setString(3, user.getUserName());
			ps.setString(4, user.getUserGender());
			ps.setString(5, user.getUserEmail());
			return ps.executeUpdate(); // 'insert' 쿼리를 하면 반드시 0이상의 수가 반환 되므로 -1이 안나오면 회원가입 성공  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn, ps, null);
		}
		return -1; //데이터베이스 오류 
	}
	
	private void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if(conn!=null) conn.close();
			if(ps!=null) ps.close();
			if(rs!=null) rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
