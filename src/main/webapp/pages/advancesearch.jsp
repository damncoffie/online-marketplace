<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Edit item</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edititemstyle.css"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/edititem.js"></script>
        <style>
            .error {
                color: red; font-weight: bold;
            }
        </style>
    </head>

    <body>
        <div id="container">
            <div class="content"> <br>
                    <p class="edititemtext" align="left"><strong>Please, enter search criteria: </strong></p>
                    <div id="form">
                        <form:form id="itemchange" commandName="search-item" method="GET" action="${pageContext.request.contextPath}/spring/find" onSubmit="return validate()">
                            <table id="table">
                                <tr>
                                    <td class="edititemtext"><form:label path="title">Title:</form:label></td>
                                    <td><form:input  path="title" /></td>
                                    <td><form:errors path="title" cssClass="error"/></td>
                                </tr>
                                <tr>
                                    <td class="edititemtext"><form:label path="startPrice">Start price:</form:label></td>
                                    <td><form:input  path="startPrice"/></td>
                                    <td><form:errors path="startPrice" cssClass="error"/></td>
                                </tr>
                                <tr>
                                    <td class="edititemtext"><form:label path="sellerID">Seller ID:</form:label></td>
                                    <td><form:input path="sellerID"/></td>
                                    <td><form:errors path="sellerID" cssClass="error"/></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td><input type="submit" value="Search"/><input type="reset" value="Reset"/></td>
                                </tr>
                            </table>
                        </form:form>
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
