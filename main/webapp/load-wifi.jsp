<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="db.WIFIService"%>
<%@ page import="db.TbPublicWifiInfo" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>와이파이 정보 구하기</title>
</head>
<body>
<%
  WIFIService wifiService = new WIFIService();
  TbPublicWifiInfo tbPublicWifiInfo = TbPublicWifiInfo.getTbPublicWifiInfo();
  wifiService.loadDB();
%>

<h1 align = "center" >
  <%=tbPublicWifiInfo.getList_total_count()%>
  개의 WIFI 정보를 정상적으로 저장하였습니다.
</h1>
<p align = "center">
  <a href = "index.jsp">
    홈 으로 가기
  </a>
</p>
</body>
</html>
