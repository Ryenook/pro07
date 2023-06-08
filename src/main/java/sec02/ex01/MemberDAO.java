package sec02.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;// 이 클래스는 이름 지정 작업을 수행하기 위한 시작 컨텍스트입니다.
import javax.naming.NamingException;
import javax.sql.DataSource;// 쌤 풀이 - 실질적인 어떤 데이터인지를 연결하는 객체 
// 구글 번역 - 이 DataSource 개체가 나타내는 물리적 데이터 소스에 대한 연결을 위한 팩터리

import sec02.ex01.MemberVO;



public class MemberDAO {
	// 필
//	private static final String driver = "oracle.jdbc.driver.OracleDriver";
//	private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";
//	//jdbc: 사용할 JDBC드라이버(oracle,mysql):드라이버타입:@서버이름(이나 ip):포트번호:DB서비스아이디(SID);
//	private static final String user = "scott"; // scott이라는 데이터 베이스에 접속
//	private static final String pwd = "12341234";
	

	private DataSource dataFactory;
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public MemberDAO() {
		// JNDI방식의 연결로 MemberDAO 객체를 초기화
		
		try {
			Context ctx = new InitialContext(); // 컨텍스트 작업을 위한 객체
			Context envContext = (Context)ctx.lookup("java:/comp/env");//오라클인지 mysql인지의 환경을 찾기위한 컨텍스트
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
		} catch (NamingException e) {
			System.out.println("톰캣의 context.xml에 정의되어 있는 이름부분에서 정확하지 않은 에러");
			e.printStackTrace();
		}
	}

	// 생
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		
		try {
			con = dataFactory.getConnection();
			String query = "select * from t_member";
			System.out.println("PreparedStatement: " + query);
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				//String id = rs.getString("id"); //칼럼이름(ID)에 해당하는 값을 가져옴
				String id = rs.getString(1); // 인덱스인데 1부터 읽힘
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				//getString이 ResultSet 객체의 현재 행에서 지정된 열의 값을 Java 프로그래밍 언어의 문자열로 검색합니다.
				System.out.print(id + " " + pwd + " " + name + " " + email + " " + joinDate);
				
				MemberVO vo = new MemberVO();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);
				
				list.add(vo);
				// MemberVO타입의 vo객체생성, add를 사용하여 list에 추가
				System.out.println("================");
			}
			rs.close(); // ResultSet의 사용이 끝났으면 rs.close();메소드를 이용해 ResultSetdmf ekedkwnehfhr gka
			pstmt.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 커넥션 DB이므로 미리 연결객체를 만들어 놨기때문에 필요없음
//	private void connDB() {	}

}