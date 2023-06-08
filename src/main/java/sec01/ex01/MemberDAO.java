package sec01.ex01;

import java.sql.Connection; // 특정 데이터베이스와의 연결(세션)
import java.sql.Date;
import java.sql.DriverManager; // JDBC 드라이버 집합을 관리하기 위한 기본 서비스입니다.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement; // SQL문을 미리 컴파일하기위한 클래스
import java.sql.Statement; // 정적 SQL 문을 실행하고 생성된 결과를 반환하는데 사용되는 개체입니다.
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
	// 필
	private static final String driver = "oracle.jdbc.driver.OracleDriver";
	private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	//jdbc: 사용할 JDBC드라이버(oracle,mysql):드라이버타입:@서버이름(이나 ip):포트번호:DB서비스아이디(SID);
	private static final String user = "scott"; // scott이라는 데이터 베이스에 접속
	private static final String pwd = "12341234";
	
//	Statement stmt; // Statement 클래스는 데이터베이스 연결로부터 SQL문을 수행할수 있도록 해주는 클래스
	PreparedStatement pstmt; // SQL문을 미리 만들어 두는 방식
	Connection conn;
	ResultSet rs;

	// 생
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		//필드가 더 늘어날수도 있어 list사용, MemberVO타입 list 객체생성
		connDB();

		String query = "select * from t_member";
		// 조회문(select, show 등)을 실행할 목적으로 사용한다.
		// sql에서 t_member 라는 테이블 조회
//		String query = "select * from t_member where id=?"; // id가 hong인 자료만 조회, 누가 올지 모를땐 ?로 대체, 물음표에선 따움표 없음	
		System.out.println(query);

		try {
			// rs =pstmt.executeQuery(query);
			 pstmt = conn.prepareStatement(query);
			 rs = pstmt.executeQuery(); 
			 // SELECT문을 수행할때 사용. 반환값은 ResultSet클래스의 인스턴스로,
			 // 해당 SELECT문의 결과에 해당하는 데이터에 접근할수 있는 방법을 제공

			 // ResultSet은 객체이므로 다시 풀어주는 과정을 코딩
			 
			 // ResultSet은 데이터베이스 내부적으로 수행된 SQL문의 처리 결과를 JDBC에서 쉽게 관리할수 있도록 해줌
			 // ResultSet은 next() 메서드를 이용해서 다음 로우(row, 행)로 이동할수 있다
			 // 커서를 최초 데이터 위치로 이동시키려면  ResultSet을 사용하기전에 rs.next()메소드를 한번 호출해줄 필요가 있다.
			 // 대부분의 경우 executeQuery(실행쿼리) 메서드를 수행한 후 while(rs.next())와 같이 더 이상 로우가 없을때까지 루프를 돌면서 데이터를 처리하는 방법을 이용함

			 // rs.next() //한줄씩 커서(결과가 여러줄으므로 맨 위에 줄부터 접근하게 하는 포인터)를 옮김
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
			conn.close(); // 데이터베이스 연결이 부족한 상황이 발생할수 있다. 
			//따라서 사용자가 많은 시스템일수록 커넥션 관리가 중요한데, 이를 위해 저넥션 풀(Connection Pool)을 이용함.
			//커넥션 풀의 사용여부와는 상관없이 데이터베이스 연결은 해당 연결을 통한 작업이 모두 끝나는 시점에서 close()메서드를 이용해 해제하는것이 좋다.


		} catch (Exception e) {
			System.out.println("sql 문장을 돌리는데 문제가 생김");
			e.printStackTrace();
		}

		return list;
	}

	// 메
	private void connDB() {
		try {
			Class.forName(driver);
			// 선생님 풀이 forName - 해당 드라이브 클래스를 불러옴
			// 구글 번역 forName - 주어진 문자열 이름을 가진 클래스 또는 인터페이스와 관련된 Class 객체를 반환합니다.
			System.out.println("Oracle 드라이버 로딩 성공");

			conn = DriverManager.getConnection(url, user, pwd);
			// getConnection - 지정된 데이터베이스 URL에 대한 연결 설정을 시도합니다.
			System.out.println("Connection 생성 성공");
			// pstmt = (PreparedStatement)conn.createStatement(); //해당 문법을 통해  Statement객체를 생성할 수 있습니다
			// createStatement - SQL 문을 데이터베이스로 보내기 위한 Statement 객체를 생성합니다.

		} catch (Exception e) {
			System.out.println("DB연결에 문제가 생김");
			e.printStackTrace();
		}
	}

}