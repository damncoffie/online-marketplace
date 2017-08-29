<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html>
    <head>
        <meta charset="utf-8">
            <title>Edit item</title>
            <link rel="stylesheet" href="../css/edititemstyle.css"></link>
            <script type="text/javascript" src="../js/edititem.js"></script>
    </head>

    <body>
        <div id="container">
            <div class="content"> <br>
                    <p class="edititemtext" align="left"><strong>Please, fill the fields: </strong></p>
                    <div id="form">
                        <form id="itemchange" name="itemchange" method="POST" action="${pageContext.request.contextPath}/serv/additem" onSubmit="return validate()">
                            <table id="table">
                                <tr>
                                    <td class="edititemtext">Title:</td>
                                    <td><input type="text" name="title" placeholder="title" required/>
                                        <td><span id="titletip"></span></td>
                                </tr>
                                <tr>
                                    <td class="edititemtext">Description</td>
                                    <td><textarea form ="itemchange" name="description" id="textarea" cols="35" rows="6" maxlength="70"></textarea></td>
                                    <td><span id="desctip"></span></td>
                                </tr>
                                <tr>
                                    <td class="edititemtext">Start price:</td>
                                    <td><input type="number" name="startprice" placeholder="0.00" min="0" required/>
                                        <td><span id="pricetip"></span></td>
                                </tr>
                                <tr>
                                    <td class="edititemtext">Buy it now:</td>
                                    <td><input type="checkbox"  name="buyitnow" onclick="check(this)"/>
                                </tr>
                                <tr>
                                    <td class="edititemtext">Finish date:</td>
                                    <td><input  id="datefield" type="datetime-local" value="YYYY-MM-DDThh:mm" name="finishdate" class="timeleft" required max="2020-12-12T12:12"/>
                                        <td><span id="timetip"></span></td>
                                </tr>
                                <tr>
                                    <td class="edititemtext">Bid increment:</td>
                                    <td><input type="number" name="increment"  id="increment" min="0" placeholder="0" required/>
                                        <td><span id="incrementtip"></span></td>
                                </tr>
                                <tr>
                                    <td><input type="submit" value="Register/Change"></td>
                                    <td><input type="reset" value="Reset"></td>
                                </tr>
                            </table>
                            <c:choose>
                                <c:when test="${param.whattodo eq 'add'}">
                                    <input type='hidden' name='whattodo' value='add' />
                                </c:when>
                                <c:when test="${param.whattodo eq 'change'}">
                                    <input type='hidden' name='whattodo' value='change' />
                                    <input type='hidden' name='itemid' value='${param.itemid}' />
                                </c:when>
                                <c:otherwise>
                                    <c:out value="something wrong"></c:out>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </div>
                    <!-- form div end -->
            </div>
            <!-- content end -->

            <div id="footer">
                <p class="footertext"> Mikhail Bobryashov, 2017</p>
            </div>
            <!-- footer end -->
        </div>
        <!-- container end -->
    </body>
</html>
