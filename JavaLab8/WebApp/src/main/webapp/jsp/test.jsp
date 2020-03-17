<%--
  Created by IntelliJ IDEA.
  User: nail
  Date: 15.12.2019
  Time: 13:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<html>
<head>
    <title>Title</title>
</head>
<body>
<p>TEST</p>

<div class="result"></div>
<input id="name" name="name"/>
<button id="btn">Show</button>

<script>
    $(document).ready(function () {
        $('#btn').click(function () {
            $.ajax({
                type: 'POST',
                url: 'test',

                data: {
                    name: $('#name').val(),
                },
                success: function (data) {
                    $('.result').html(data);
                }
            });
        });
    });
</script>
</body>
</html>
