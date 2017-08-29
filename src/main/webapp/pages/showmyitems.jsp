<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta charset="utf-8">
            <title>Show my items</title>
            <link rel="stylesheet" href="../css/showmyitemsstyle.css"/>
            <link rel="stylesheet" href="../js/JQuery/tablesorter/themes/blue/blue/style.css" />
            <script>
                var type = '${type}';
                var login = '${login}';
                if (type === "search") {
                    var searchtype = '${searchtype}';
                    var searchkey = '${searchkey}';
                }
            </script>
            <script type="text/javascript" src="../js/JQuery/jquery-3.2.1.min.js"></script>
            <script type="text/javascript" src="../js/JQuery/tablesorter/jquery.tablesorter.min.js"></script>
            <script type="text/javascript" src="../js/myitems.js"></script>
    </head>

    <body>
        <div class="container" id="container">
            <div id="header">
                <p id="greetings" >You are logged in as: ${sessionScope.login} <br>
                        <a href="${pageContext.request.contextPath}/serv/logout">logout</a></p>
            </div>
            <!-- header end -->
            <div id="buttons">
                <div id="back">
                    <form action="${pageContext.request.contextPath}/serv/showitems" method="GET">
                        <input type="submit" value="Back"/>
                    </form>
                </div>
                <!-- back end -->
                <div id="addnew">
                    <form action="${pageContext.request.contextPath}/pages/additem.jsp" method="GET">
                        <input type='hidden' name='whattodo' value='add' />
                        <input type="submit" value="Add new item"/>
                    </form>
                </div>
                <!-- add new end-->
            </div>
            <!-- end buttons -->
            <div class="resulttable">
                <table id="table" class="tablesorter">
                    <thead>
                        <tr>
                            <th>Item ID</th>
                            <th>Title</th>
                            <th>Description</th>
                            <th>Start price</th>
                            <th>Bid increment</th>
                            <th>Finish date</th>
                            <th>Buy it now</th>
                            <th>Is sold</th>
                            <th>  </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${not empty myitems}">
                            <c:forEach var="item" items="${myitems}">
                                <tr>
                                    <td>${item.itemID}</td>
                                    <td>${item.title}</td>
                                    <td>${item.description}</td>
                                    <td>${item.startPrice}</td>
                                    <td>${item.bidIncrement}</td>
                                    <td>${item.finishDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.buyItNow == 1}">
                                                <c:out value="yes" default="something wrong"></c:out>
                                            </c:when>
                                            <c:when test="${item.buyItNow == 0}">
                                                <c:out value="no" default="something wrong"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="somthing really wrong"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.isSold == 1}">
                                                <c:out value="yes" default="something wrong"></c:out>
                                            </c:when>
                                            <c:when test="${item.isSold == 0}">
                                                <c:out value="no" default="something wrong"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="somthing really wrong"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/pages/additem.jsp">
                                            <input type='hidden' name='whattodo' value='change' />
                                            <input type='hidden' name='itemid' value='${item.itemID}'/>
                                            <input type="submit" value="Change"/>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <!-- end result table -->
            <div id="footer" >
                <p class="footertext"> Mikhail Bobryashov, 2017 </p>
            </div>
            <!-- end footer -->
        </div>
        <!-- end container -->
    </body>
</html>
