<%--
  Created by IntelliJ IDEA.
  User: nail
  Date: 14.12.2019
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<html>
<head>
    <title>Login</title>
    <link href="/css/registration.css" rel="stylesheet">
</head>
<body>
<form action="/login" method="post">
    <div class="container">
        <h1>LOGIN</h1>
        <p>Please fill in this form to log in to your account.</p>
        <hr>

        <label for="email"><b>Name</b></label>
        <div class="status"></div>
        <input id = "name" type="text" placeholder="Enter Name" name="name" required>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <hr>

        <button type="submit" class="registerbtn">Login</button>
    </div>

    <div class="container signin">
        <p>Registered? <a href="http://localhost:8080/reg">Registration</a>.</p>
    </div>
</form>
</body>
</html>
