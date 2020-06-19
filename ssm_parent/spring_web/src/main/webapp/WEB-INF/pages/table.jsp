<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Pyrrha
  Date: 2020/6/17
  Time: 18:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户明细</title>
</head>
<body>
<table>
    <tr>
        <td>ID</td>
        <td>NAME</td>
        <td>PRICE</td>
        <td>PIC</td>
        <td>CREATETIME</td>
        <td>DETAIL</td>
    </tr>
    <c:forEach items="${list}" var="detail">
        <tr>
            <td>${detail.id}</td>
            <td>${detail.name}</td>
            <td>${detail.price}</td>
            <td>${detail.pic}</td>
            <td>${detail.createtime}</td>
            <td>${detail.detail}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
