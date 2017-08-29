<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta charset="utf-8"></meta>
        <title>Login</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginstyle.css"></link>
    </head>

    <body>
        <div id="container">
            <div class="content"> <br></br>
                <p id="headline" align="center"><strong>Auction (&lt;epam&gt;-task)</strong></p>
                <p id="loginpagetext"> <strong>Welcome</strong><br></br>
                    If you already have an account, please login:</p>
                <div id="form">
                    <form name="loginform" method="POST" action="${pageContext.request.contextPath}/spring/login">
                        <table align="center">
                            <tr>
                                <td class="field">Login:</td>
                                <td><input type="text" name="login" id="login" required="required"></input></td><!--  -->
                                <td><span id="logintip"></span></td>
                            </tr>
                            <tr>
                                <td class="field">Password:</td>
                                <td><input type="password" class="hellotext" name="password" required="required"></input></td><!--  -->
                                <td><span id="passwordtip"></span></td>
                            </tr>
                            <tr>
                                <td><input type="submit" value="Enter"></input></td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!-- form end -->
                <div class="options">
                    <p id="loginpagetext">You may <a href="${pageContext.request.contextPath}/pages/register.jsp">create new account</a> <br>
                            Or <a href="${pageContext.request.contextPath}/serv/guest">enter as a guest</a>
                    </p>
                </div>
                <!-- options end -->
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
