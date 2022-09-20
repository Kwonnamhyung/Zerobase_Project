<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="db.WIFIService"%>
<%@ page import="db.WIFIInfo" %>
<%@ page import="db.TbPublicWifiInfo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>와이파이 정보 구하기</title>
    <style>
        #customers {
            font-family: Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        #customers td, #customers th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        #customers tr:nth-child(even){background-color: #f2f2f2;}

        #customers tr:hover {background-color: #ddd;}

        #customers th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #04AA6D;
            color: white;
        }
    </style>
</head>
<body>
<%
    WIFIService wifiService = new WIFIService();
    WIFIInfo wifiInfo = new WIFIInfo();
    TbPublicWifiInfo tbPublicWifiInfo = TbPublicWifiInfo.getTbPublicWifiInfo();
    List<WIFIInfo> list = TbPublicWifiInfo.getRows();
%>
<h1>와이파이 정보 구하기</h1>
<p1>
    <a href="index.jsp">홈</a>
    |
    <a href="history-wifi.jsp">위치 히스토리 목록</a>
    |
    <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
    <br>
    <br>
        <strong>LAT: </strong>
        <input type="text" id = "LAT" size="15" value="0.0">
        <strong>LNT: </strong>
        <input type="text" id = "LNT" size="15" value="0.0">
        <button onclick = "getLocation()">내 위치 가져오기</button>
        <button onclick = "showLocation()">근처 WIPI 정보 보기</button>


<script type="text/javascript">

    //x를 demo랑 연결
    var x = document.getElementById("LAT");
    var y = document.getElementById("LNT");

    function getLocation() {
        //alert("getLocation");

        if(navigator.geolocation){
            navigator.geolocation.getCurrentPosition(showPosition);
        }
        else{
            alert("위치를 얻을 수 없습니다.");
        }

    }
    function showPosition(position) {
        x.value = position.coords.latitude;
        y.value = position.coords.longitude;
    }
    function showLocation() {

        let LAT = document.getElementById("LAT").value;
        let LNT = document.getElementById("LNT").value;
        // 스페이스바 주의 null값 우려
        location.href ="show-wifi.jsp?LAT="+ LAT +"&LNT="+ LNT;
    }

</script>
</p1>
<br>
<br>
<table id="customers">
    <thead>
        <tr>
            <th>거리(Km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>
            <th>도로명주소</th>
            <th>상세주소</th>
            <th>설치위치(층)</th>
            <th>설치유형</th>
            <th>설치기관</th>
            <th>서비스구분</th>
            <th>망종류</th>
            <th>설치년도</th>
            <th>실내외구분</th>
            <th>WIFI접속환경</th>
            <th>X좌표</th>
            <th>Y좌표</th>
            <th>작업일자</th>
        </tr>
    </thead>
    <tbody id = "tbody">
        <tr>
            <td colspan="17" align="center" height="60px">위치 정보를 입력한 후에 조회해 주세요.</td>
        </tr>
    </tbody>
</table>

</body>
</html>