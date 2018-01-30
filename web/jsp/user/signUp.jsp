<%--
  Created by IntelliJ IDEA.
  User: nastya
  Date: 29.01.2018
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtag"%>

<html lang="en">
<head>
    <title>Sign up</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">

    <style><%@include file="../../css/style.css"%></style>
</head>
<body>
<ctg:role></ctg:role>

<div class="container-fluid">
    <div class="container">
        <div class="row">
            <div class="col-1"></div>
            <div class="col-10">
                <div class="sign-up card">
                    <h1 class="title" id="registerTitle">Register Form</h1>
                    <div class="error-sign-up">${errorSignUp}</div>
                    <div class="row">
                        <div class="col-12">
                            <form name="loginForm" method="POST" action="controller">
                                <input type="hidden" name="command" value="sign_up"/>

                                <!-- Text input-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="lastName">Last Name</label>
                                    <div class="col-sm-9">
                                        <input id="lastName" name="lastName" type="text" placeholder="LastName"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="firstName">First Name</label>
                                    <div class="col-sm-9">
                                        <input id="firstName" name="firstName" type="text" placeholder="FirstName"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>

                                <!--Login -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="registerLogin">Login</label>
                                    <div class="col-sm-9">
                                        <input id="registerLogin" name="login" type="text" placeholder="Login"
                                               class="form-control input-md" required=""
                                               pattern="^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){6,18}[a-zA-Z0-9]$"
                                               title="Login must be between 6 and 18 characters, contain only digit,
                                               alphabetic and '.' or '_' characters. Characters '.' and '_' can't be
                                               matching at the begin or at the end. Also combinations ._ or __ or _.
                                               are not valid.">
                                    </div>
                                </div>

                                <!--Password -->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="password">Password</label>
                                    <div class="col-sm-9">
                                        <input id="password" name="password" type="password" placeholder="Password"
                                               class="form-control input-md" required=""
                                               pattern="(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,15})$"
                                               title="Password must be between 8 and 10 characters, contain at least
                                               one digit and one alphabetic character, and must not contain special
                                               characters.">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="confirmPassword">Repeat password</label>
                                    <div class="col-sm-9">
                                        <input id="confirmPassword" name="confirmPassword" type="password"
                                               placeholder="Repeat password" class="form-control input-md" required=""
                                               pattern="(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,15})$">
                                    </div>
                                </div>

                                <!-- Select Date Of Birth-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="month">Month</label>

                                    <div class="col-sm-3">
                                        <select id="month" name="month" class="form-control">
                                            <c:forEach begin="1" end="12" varStatus="loop">
                                                <option value="${loop.index}">${loop.index}</option>
                                            </c:forEach>
                                        </select>
                                    </div>


                                    <label class="col-sm-2 control-label" for="day">Day</label>
                                    <div class="col-sm-3">
                                        <select id="day" name="day" class="form-control">
                                            <c:forEach begin="1" end="31" varStatus="loop">
                                                <option value="${loop.index}">${loop.index}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <label class="col-sm-3 control-label" for="year">Year</label>
                                    <div class="col-sm-3">
                                        <select id="year" name="year" class="form-control">
                                            <c:forEach begin="1930" end="2010" varStatus="loop">
                                                <option value="${loop.index}">${loop.index}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <!-- Address input-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="address">Address</label>
                                    <div class="col-sm-9">
                                        <input id="address" name="address" type="text" placeholder="Address"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="city">City</label>
                                    <div class="col-sm-9">
                                        <input id="city" name="city" type="text" placeholder="City"
                                               class="form-control input-md" required="">
                                    </div>
                                </div>

                                <!-- Postal index input-->
                                <div class="form-group row">
                                    <label class="col-sm-3 control-label" for="postalIndex">Postal Index</label>
                                    <div class="col-sm-9">
                                        <input id="postalIndex" name="postalIndex" type="text" placeholder="XXXXXX"
                                               class="form-control input-md" required="" pattern="[0-9]{6}">
                                    </div>
                                </div>

                                <button class="btn btn-primary">Sign Up</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-1"></div>
        </div>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous">
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
        integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous">
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
        integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous">
</script>
</body>
</html>
