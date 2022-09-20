package db;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class WIFIService {

	List<WIFIInfo> list = new ArrayList<>();
	private int startNum = 1;
	private int lastNum = 1000;
	int count = 0;
	int num = -1;

	private static String LAT;
	private static String LNT;

	public String getLAT() {
		return LAT;
	}

	public void setLAT(String LAT) {
		this.LAT = LAT;
	}

	public String getLNT() {
		return LNT;
	}

	public void setLNT(String LNT) {
		this.LNT = LNT;
	}

	public static void main(String[] args) throws IOException, ParseException{

	}

	public void loadDB() throws IOException, ParseException{


		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
		urlBuilder.append("/" +  URLEncoder.encode("4879776e76726e6a38304d5a6f726f","UTF-8") ); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
		urlBuilder.append("/" +  URLEncoder.encode("json","UTF-8") ); /*요청파일타입 (xml,xmlf,xls,json) */
		urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
		urlBuilder.append("/" + URLEncoder.encode(String.valueOf(startNum),"UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
		urlBuilder.append("/" + URLEncoder.encode(String.valueOf(lastNum),"UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
		// 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

		// 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요청인자'부분에 자세히 나와 있습니다.

		URL url = new URL(urlBuilder.toString());

		BufferedReader bf;

		bf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));

		String result = bf.readLine();

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
		JSONObject JsonData = (JSONObject)jsonObject.get("TbPublicWifiInfo");

		TbPublicWifiInfo tbPublicWifiInfo = TbPublicWifiInfo.getTbPublicWifiInfo();
		tbPublicWifiInfo.setList_total_count(JsonData.get("list_total_count").toString());

		JSONArray JsonRows = (JSONArray)JsonData.get("row");

		for(int i = 0; i < JsonRows.size(); i++) {

			JSONObject RoswData = (JSONObject)JsonRows.get(i);
			WIFIInfo wifiInfo = new WIFIInfo();
			wifiInfo.setX_SWIFI_MGR_NO(RoswData.get("X_SWIFI_MGR_NO").toString());
			wifiInfo.setX_SWIFI_WRDOFC(RoswData.get("X_SWIFI_WRDOFC").toString());
			wifiInfo.setX_SWIFI_MAIN_NM(RoswData.get("X_SWIFI_MAIN_NM").toString());
			wifiInfo.setX_SWIFI_ADRES1(RoswData.get("X_SWIFI_ADRES1").toString());
			wifiInfo.setX_SWIFI_ADRES2(RoswData.get("X_SWIFI_ADRES2").toString());
			wifiInfo.setX_SWIFI_INSTL_FLOOR(RoswData.get("X_SWIFI_INSTL_FLOOR").toString());
			wifiInfo.setX_SWIFI_INSTL_TY(RoswData.get("X_SWIFI_INSTL_TY").toString());
			wifiInfo.setX_SWIFI_INSTL_MBY(RoswData.get("X_SWIFI_INSTL_MBY").toString());
			wifiInfo.setX_SWIFI_SVC_SE(RoswData.get("X_SWIFI_SVC_SE").toString());
			wifiInfo.setX_SWIFI_CMCWR(RoswData.get("X_SWIFI_CMCWR").toString());
			wifiInfo.setX_SWIFI_CNSTC_YEAR(RoswData.get("X_SWIFI_CNSTC_YEAR").toString());
			wifiInfo.setX_SWIFI_INOUT_DOOR(RoswData.get("X_SWIFI_INOUT_DOOR").toString());
			wifiInfo.setX_SWIFI_REMARS3(RoswData.get("X_SWIFI_REMARS3").toString());
			wifiInfo.setLAT(RoswData.get("LNT").toString());
			wifiInfo.setLNT(RoswData.get("LAT").toString());
			wifiInfo.setWORK_DTTM(RoswData.get("WORK_DTTM").toString());

			list.add(wifiInfo);
			register(wifiInfo);
		}

		bf.close();

		if(count == 0 ) {
			calculatePrice();
		}

		if(count < num) {
			count++;
			startNum += 1000;
			lastNum += 1000;
			loadDB();
		} else {
			TbPublicWifiInfo.setRows(list);
		}

	}

	private void calculatePrice() {

		TbPublicWifiInfo tbPublicWifiInfo = TbPublicWifiInfo.getTbPublicWifiInfo();
		int totalCount = Integer.parseInt(tbPublicWifiInfo.getList_total_count());

		num =  totalCount / 1000;
	}

	public void register(WIFIInfo wifiInfo) {

		String url = "jdbc:mysql://localhost:3306/JSP_Project";
		String dbUSerId = "kwonnamhyung";
		String password = "ko731673ko!@";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			connection = DriverManager.getConnection(url,dbUSerId,password);

			String sql = "insert ignore into WIFIINFO (X_SWIFI_MGR_NO , X_SWIFI_WRDOFC , X_SWIFI_MAIN_NM , X_SWIFI_ADRES1 , X_SWIFI_ADRES2 , X_SWIFI_INSTL_FLOOR , X_SWIFI_INSTL_TY , X_SWIFI_INSTL_MBY , X_SWIFI_SVC_SE , X_SWIFI_CMCWR , X_SWIFI_CNSTC_YEAR , X_SWIFI_INOUT_DOOR , X_SWIFI_REMARS3 , LAT , LNT , WORK_DTTM) " +
					" values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?) ";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setNString(1, wifiInfo.getX_SWIFI_MGR_NO());
			preparedStatement.setNString(2, wifiInfo.getX_SWIFI_WRDOFC());
			preparedStatement.setNString(3, wifiInfo.getX_SWIFI_MAIN_NM());
			preparedStatement.setNString(4, wifiInfo.getX_SWIFI_ADRES1());
			preparedStatement.setNString(5, wifiInfo.getX_SWIFI_ADRES2());
			preparedStatement.setNString(6, wifiInfo.getX_SWIFI_INSTL_FLOOR());
			preparedStatement.setNString(7, wifiInfo.getX_SWIFI_INSTL_TY());
			preparedStatement.setNString(8, wifiInfo.getX_SWIFI_INSTL_MBY());
			preparedStatement.setNString(9, wifiInfo.getX_SWIFI_SVC_SE());
			preparedStatement.setNString(10, wifiInfo.getX_SWIFI_CMCWR());
			preparedStatement.setNString(11, wifiInfo.getX_SWIFI_CNSTC_YEAR());
			preparedStatement.setNString(12, wifiInfo.getX_SWIFI_INOUT_DOOR());
			preparedStatement.setNString(13, wifiInfo.getX_SWIFI_REMARS3());
			preparedStatement.setNString(14, wifiInfo.getLAT());
			preparedStatement.setNString(15, wifiInfo.getLNT());
			preparedStatement.setNString(16, wifiInfo.getWORK_DTTM());


			int affected = preparedStatement.executeUpdate();

			if(affected > 0) {
				System.out.println(" 저장 성공");
			} else {
				System.out.println(" 저장 실패");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public void registerHistory(String LAT , String LNT) {

		String url = "jdbc:mysql://localhost:3306/JSP_Project";
		String dbUSerId = "kwonnamhyung";
		String password = "ko731673ko!@";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			connection = DriverManager.getConnection(url,dbUSerId,password);

			String sql = "insert into WIFIINFO_HISTORY (LAT , LNT , WORK_DTTM) " +
					" values (? , ? , ? ) ";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setNString(1, WIFIService.LAT);
			preparedStatement.setNString(2, WIFIService.LNT);
			preparedStatement.setNString(3, String.valueOf(LocalDateTime.now()));

			int affected = preparedStatement.executeUpdate();

			if(affected > 0) {
				System.out.println(" 저장 성공");
			} else {
				System.out.println(" 저장 실패");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public List<WIFIInfo> getNearWifiInfo() {

		List<WIFIInfo> list = new ArrayList<>();
		//5개 1.IP 2.port 3.계정 4.password 5.인스턴스

		String url = "jdbc:mysql://localhost:3306/JSP_Project";
		String dbUserId = "kwonnamhyung";
		String dbPassword = "ko731673ko!@";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException e){
			System.out.println(e.getMessage());
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;



		try {
			connection = DriverManager.getConnection(url,dbUserId,dbPassword);


			String sql =
					" SELECT *,(6371*acos(cos(radians(?))*cos(radians(LAT))*cos(radians(LNT)-radians(?))+sin(radians(?))*sin(radians(LAT)))) AS distance " +
							" FROM WIFIINFO " +
							" ORDER BY distance " +
							" limit 20 ";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setNString(1,getLAT());
			preparedStatement.setNString(2,getLNT());
			preparedStatement.setNString(3,getLAT());

			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				WIFIInfo wifiInfo = new WIFIInfo();
				wifiInfo.setDistance(rs.getString("distance"));
				wifiInfo.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
				wifiInfo.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
				wifiInfo.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
				wifiInfo.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
				wifiInfo.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
				wifiInfo.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
				wifiInfo.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
				wifiInfo.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
				wifiInfo.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
				wifiInfo.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
				wifiInfo.setX_SWIFI_CNSTC_YEAR(rs.getString("X_SWIFI_CNSTC_YEAR"));
				wifiInfo.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
				wifiInfo.setX_SWIFI_REMARS3(rs.getString("X_SWIFI_REMARS3"));
				wifiInfo.setLAT(rs.getString("LAT"));
				wifiInfo.setLNT(rs.getString("LNT"));
				wifiInfo.setWORK_DTTM(rs.getString("WORK_DTTM"));

				list.add(wifiInfo);

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if(rs != null && !rs.isClosed()){
					rs.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(connection != null && !connection.isClosed()){
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}

		return list;
	}

	public List<WIFIInfoHistory> getWIFIInfoHistory() {

		List<WIFIInfoHistory> list = new ArrayList<>();
		//5개 1.IP 2.port 3.계정 4.password 5.인스턴스

		String url = "jdbc:mysql://localhost:3306/JSP_Project";
		String dbUserId = "kwonnamhyung";
		String dbPassword = "ko731673ko!@";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException e){
			System.out.println(e.getMessage());
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;



		try {
			connection = DriverManager.getConnection(url,dbUserId,dbPassword);


			String sql =
					" select * , rank() over (order by WORK_DTTM) as ranking " +
							" from WIFIINFO_HISTORY " +
							" order by ranking desc ";

			preparedStatement = connection.prepareStatement(sql);

			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				WIFIInfoHistory wifiInfoHistory = new WIFIInfoHistory();
				wifiInfoHistory.setLAT(rs.getString("LAT"));
				wifiInfoHistory.setLNT(rs.getString("LNT"));
				wifiInfoHistory.setWORK_DTTM(rs.getString("WORK_DTTM"));
				wifiInfoHistory.setRANK(rs.getString("ranking"));

				list.add(wifiInfoHistory);

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if(rs != null && !rs.isClosed()){
					rs.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(connection != null && !connection.isClosed()){
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}

		return list;
	}

	public void deleteWIFIInfoHistory(String WORK_DTTM) {

		System.out.println(WORK_DTTM);

		String url = "jdbc:mysql://localhost:3306/JSP_Project";
		String userId = "kwonnamhyung";
		String password = "ko731673ko!@";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Connection connection= null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DriverManager.getConnection(url,userId,password);

			String sql =
					" delete from WIFIINFO_HISTORY " +
							" where WORK_DTTM = ? ";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,WORK_DTTM);

			int affected = preparedStatement.executeUpdate();

			if(affected > 0) {
				System.out.println(" 삭제 성공 ");
			}else{
				System.out.println(" 삭제 실패 ");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {

			try {
				if (preparedStatement != null && !preparedStatement.isClosed()) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

			try {
				if(connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}


	}


}
