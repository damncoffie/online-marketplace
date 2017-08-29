<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
    <head>
        <meta charset="utf-8">
            <title>Register</title>
            <link rel="stylesheet" href="../css/registerstyle.css"></link>
            <script type="text/javascript" src="../js/registerscript.js"></script>
    </head>

    <body>
        <div id="container">
            <div class="content" id="content"> <br></br>
                <p id="headline" align="center"><strong>Auction (&lt;epam&gt;-task)</strong></p>
                <p class="registerpagetext"><strong>Please, fill the from: </strong></p>
                <div id="form">
                    <form name="loginform" method="POST" action="${pageContext.request.contextPath}/serv/register" onSubmit="return validate()">
                        <table align="center">
                            <tr>
                                <td class="registerpagetext">First Name:</td>
                                <td><input type="text" name="firstname" required="required"></input>
                                    <td><span class="tip" id="firstnametip"></span></td>
                            </tr>
                            <tr>
                                <td class="registerpagetext">Last name:</td>
                                <td><input type="text" name="lastname" required="required"></input>
                                    <td><span class="tip" id="lastnametip"></span></td>
                            </tr>
                            <tr>
                                <td class="registerpagetext">Billing address:</td>
                                <td><input type="text" name="billingaddress" required="required"></input>
                                    <td><span class="tip" id="addresstip"></span></td>
                            </tr>
                            <tr>
                                <td class="registerpagetext">Login:</td>
                                <td><input type="text" name="login" required="required"></input>
                                    <td><span class="tip" id="logintip"></span></td>
                            </tr>
                            <tr>
                                <td class="registerpagetext">Password:</td>
                                <td><input type="password" id="password"  name="password" required="required"></input>
                                    <td><span class="tip" id="passwordtip"></span></td>
                            </tr>
                            <tr>
                                <td class="registerpagetext">Re-enter psw:</td>
                                <td><input type="password" id="repassword"  name="repassword" required="required"></input>
                                    <td><span class="tip" id="repasswordtip"></span></td>
                            </tr>
                            <tr>
                                <td><input type="submit" value="Register"></input></td>
                                <td><input type="reset" value="Reset"></input></td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!-- form end -->
                <!-- content end -->

                <div id="footer">
                    <p class="footertext"> Mikhail Bobryashov, 2017</p>
                </div>
                <!-- footer end -->
            </div>
        </div>
        <!-- container end -->
    </body>
</html>
