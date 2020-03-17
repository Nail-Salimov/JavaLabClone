<%--
  Created by IntelliJ IDEA.
  User: nail
  Date: 23.10.2019
  Time: 20:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<html>
<head>
    <link href="/css/registration.css" rel="stylesheet">
    <script href="/js/registration.js"></script>
</head>
<body>
<form action="/reg" method="post">
    <div class="container">
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>

        <label for="email"><b>Name</b></label>
        <div class="status"></div>
        <input id = "name" type="text" placeholder="Enter Name" onblur="f()" name="name" required>

        <script>
            function f() {
                $.ajax({
                    type: 'POST',
                    url: 'checkName',
                    dataType: 'json',
                    data: {
                        name: $('#name').val()
                    },
                    success: function (data) {
                        var name = data.status;
                        $('.status').html(name);
                    }
                });
            }
        </script>

        <label for="psw"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>

        <hr>

        <button type="submit" class="registerbtn">Register</button>
    </div>

    <div class="container signin">
        <p>Already have an account? <a href="http://localhost:8080/login">Sign in</a>.</p>
    </div>
</form>
</body>
</html>
</html>
