<%--
    Document   : showitems
    Created on : Jul 16, 2017, 2:35:50 PM
    Author     : Mikhail_Bobriashov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Show items</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/showitemsstyle.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/js/JQuery/tablesorter/themes/blue/blue/style.css" />
        <script>
            var type = '${type}';
            if (type === "others") {
                var id = '${id}';
            }
            if (type === "search") {
                var searchtype = '${searchtype}';
                var searchkey = '${searchkey}';
            }
            var login = '${login}';
        </script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/JQuery/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/JQuery/tablesorter/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/items.js"></script>
    </head>
    <body>
        </div>
        <div class="container" id="container">
            <div id="header">
                <p id="greetings" >You are logged in as: ${login} ${yo}<br/>
                    <c:choose>
                        <c:when test="${login == 'guest'}">
                            <a href="${pageContext.request.contextPath}/pages/register.jsp">create account</a><br/>
                            <a href="${pageContext.request.contextPath}/index.jsp">login</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/serv/logout">logout</a>
                        </c:otherwise>
                    </c:choose>
                </p>
                <div id="forms">
                    <div id="searchfrom" class="form">
                        <form id="searchform" action="${pageContext.request.contextPath}/serv/search" method="GET">
                            <input type="text" name="searchkey" value="" size="40" placeholder="${type}"/>
                            <select class="searchoption" name="searchtype">
                                <option>Title</option>
                                <option>Seller</option>
                            </select>
                            <input type="submit" value="Search"/>
                        </form>
                    </div>
                    <!-- search div end -->
                    <div id="searchfrom" class="form">
                        <form id="searchform" action="${pageContext.request.contextPath}/spring/advancesearch" method="GET">
                            <input type="submit" value="Advance search"/>
                        </form>
                    </div>
                    <!-- advance search div end -->
                    <div id="refreshform" class="form">
                        <c:if test="${login ne 'guest'}">
                            <form id="myitems" action="${pageContext.request.contextPath}/serv/showmyitems">
                                <button id="refreshbutton">My items</button>
                            </form>
                        </c:if>
                    </div>
                    <!-- refresh div end -->
                    <!-- forms end -->
                </div>
                <!-- header end -->
                <div class="resulttable">
                    <table id="table" class="tablesorter">
                        <thead>
                            <tr>
                                <th>Seller ID</th>
                                <th>Title</th>
                                <th>Description</th>
                                <th>Start price</th>
                                <th>Bid increment</th>
                                <th id="timeleft">Time left</th>
                                <th>Best Bid</th>
                                <th>Bidder id</th>
                                <th>Bid/Buy</th>
                            </tr>
                        </thead>
                        <tbody id="tablebody">
                            <c:if test="${not empty allitems}">
                                <c:forEach var="item" items="${allitems}" varStatus="loop" >
                                    <tr>
                                        <td id="sellerid${item.itemID}">${item.sellerID}</td>
                                        <td id="title${item.itemID}">${item.title}</td>
                                        <td id="description${item.itemID}">${item.description}</td>
                                        <td id="startprice${item.itemID}">${item.startPrice}</td>
                                        <td id="bidincrement${item.itemID}">${item.bidIncrement}</td>
                                        <td id="datediff${item.itemID}" class="datediff">
                                            <c:choose>
                                                <c:when test="${item.finishDate eq '1999-07-07T23:59:59'}">
                                                    buy-it-now item
                                                </c:when>
                                                <c:when test="${item.isSold == 1}">
                                                    item is sold
                                                </c:when>
                                                <c:otherwise>
                                                    ${item.finishDate}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td id="bid${item.itemID}">${allbids[loop.index].bid}</td>
                                        <td id="bidder${item.itemID}">${allbids[loop.index].bidder_id}</td>
                                        <td id="bidbuy${item.itemID}" class="bidbuy">
                                            <c:if test="${login ne 'guest'}">
                                                <c:choose>
                                                    <c:when test="${item.isSold == 0}">
                                                        <c:choose>
                                                            <c:when test="${item.buyItNow == 0}">
                                                                <form action="${pageContext.request.contextPath}/serv/bid" method="POST">
                                                                    <c:choose>
                                                                        <c:when test="${allbids[loop.index] != null }">
                                                                            <input id="bidinput${item.itemID}" class="bid" name="bid" type="number" step="${item.bidIncrement}"
                                                                                   min="${allbids[loop.index].bid + item.bidIncrement}" required="requierd" onchange="return checkMinus()"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input id="bidinput${item.itemID}" class="bid" name="bid" type="number" step="${item.bidIncrement}"
                                                                                   min="${item.startPrice}" required="requierd" onchange="return checkMinus()"/>
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                    <input type="hidden" name="itemid" value="${item.itemID}"/>
                                                                    <input type="submit" value="Bid"/>
                                                                </form>
                                                            </c:when>
                                                            <c:when test="${item.buyItNow == 1}">
                                                                <form action="${pageContext.request.contextPath}/serv/buy" method="POST">
                                                                    <input type="hidden" name="itemid" value="${item.itemID}"/>
                                                                    <input type="submit" value="Buy now!"/>
                                                                </form>
                                                            </c:when>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:when test="${item.isSold == 1}">
                                                        item is sold
                                                    </c:when>
                                                </c:choose>
                                            </c:if>
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
        </div><!-- end container -->
    </body>
</html>