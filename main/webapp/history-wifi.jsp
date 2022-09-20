<%@ page import="db.WIFIService" %>
<%@ page import="java.util.List" %>
<%@ page import="db.WIFIInfoHistory" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
            text-align: center;
        }

        #customers tr:nth-child(even){background-color: #f2f2f2;}

        #customers tr:hover {background-color: #ddd;}

        #customers th {
            padding-top: 12px;
            padding-bottom: 12px;
            background-color: #04AA6D;
            color: white;
        }
    </style>
</head>
<body>
<%
    String WORK_DTTM = request.getParameter("WORK_DTTM");
    WIFIService wifiService = new WIFIService();
    wifiService.deleteWIFIInfoHistory(WORK_DTTM);
    List<WIFIInfoHistory> list = wifiService.getWIFIInfoHistory();
%>
<h1>와이파이 정보 구하기</h1>
<p1>
    <a href="index.jsp">홈</a>
    |
    <a href="history-wifi.jsp">위치 히스토리 목록</a>
    |
    <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
</p1>
<br>
<br>
<table id="customers">
    <thead>
    <tr>
        <th>ID</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>조회일자</th>
        <th>비고</th>
    </tr>
    </thead>
    <tbody id = "tbody">
        <%
            for(WIFIInfoHistory wifiInfoHistory : list) {
        %>
            <tr id = "ID">
                <td><%=wifiInfoHistory.getRANK()%></td>
                <td><%=wifiInfoHistory.getLAT()%></td>
                <td><%=wifiInfoHistory.getLNT()%></td>
                <td><%=wifiInfoHistory.getWORK_DTTM()%></td>
                <td>
                    <button onclick="deleteHistory()">삭제</button>
                </td>
            </tr>
        <%
            }
        %>

        <script type="text/javascript">

            function deleteHistory() {

                var table =document.getElementById('customers');
                var rowList = table.rows;

                for (i=1; i<rowList.length; i++) {//thead부분 제외.

                    var row = rowList[i];
                    var str = "";

                    row.onclick = function(){
                        return function(){

                            //개별적으로 값 가져오기

                            var WORK_DTTM =this.cells[3].innerHTML; //번호
                            location.href ="history-wifi.jsp?WORK_DTTM="+ WORK_DTTM;

                        };//return
                    }(row);//onclick
                }//for

            }

        </script>
    </tbody>
</table>

</body>
</html>