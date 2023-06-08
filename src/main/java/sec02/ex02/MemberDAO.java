package sec02.ex02;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource dataFactory;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Connection con;
	
	
	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext= (Context)ctx.lookup("java:/comp/env"); // 오라클인 mysql인지 환경을 찾기 위한 컨텍스트
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
		} catch (NamingException e) {
			System.out.println("톰캣의 context.xml에 정의되어 있는 이름부분에서 정확하지않은 에러 발생");
			e.printStackTrace();
		}
	} 
	
	public List<MemberVO> listMembers(){
		List<MemberVO> list = new ArrayList<MemberVO>();
		
		try {
			con = dataFactory.getConnection();
			String query = "select * from t_member";
			System.out.println("Preparedtatement: " + query);
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate= rs.getDate("joinDate");
				System.out.print(id + " " + pwd + " " + name + " " + email + " " + joinDate);
				
				MemberVO vo = new MemberVO();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);
				
				list.add(vo);
				System.out.println("==================");
			}
			rs.close();
			pstmt.close();
			con.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return list;
		
	}
	
	public void addMember(MemberVO memberVO) {
		try {
			con =dataFactory.getConnection();
			
			
			String id = memberVO.getId();
			String pwd = memberVO.getPwd();
			String name = memberVO.getName();
			String email = memberVO.getEmail();
			String query = "insert into t_member (id, pwd, name, email) values (?,?,?,?)" ;
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			
			pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("회원 추가시 에러");
//			e.printStackTrace();
		}
	}
	
	void delMember(String id) {
		
		try {
			con =dataFactory.getConnection();
			String query = "delete from t_member where id=?" ;
			System.out.println("prepareStatement : " + query);
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, id);			
			pstmt.executeUpdate();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("회원 삭제시 에러");
//			e.printStackTrace();
		}
		
	}
	
}