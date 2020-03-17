<%--
  Created by IntelliJ IDEA.
  User: nail
  Date: 14.12.2019
  Time: 14:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<html>
<head>
    <title>Store</title>
</head>
<body>
<table class="results" style="width:100%">
    <tr>
        <th>Product</th>
        <th>Count</th>
    </tr>

</table>

<p>Добавить товар</p>
<input id="productName" type="text"/>
<input id="productCount" type="text"/>
<button id="btn">Add</button>

<script>
    $(document).ready(function () {
        $.ajax({
            type: 'POST',
            url: 'store',
            dataType: 'json',
            success: function (data) {
                var list =
                    '    <tr>\n' +
                    '        <th>Product</th>\n' +
                    '        <th>Count</th>\n' +
                    '    </tr>\n';
                var products = data.map;
                for (var key in products)
                    list += '<tr><td>' + key + '</td><td>' + products[key] + '</td></tr>';
                $('.results').html(list);
            }
        });
    });
</script>
<script>
    $(document).ready(function () {
        $('#btn').click(function () {
            $.ajax({
                type: 'POST',
                url: 'store',
                dataType: 'json',
                data: {
                    product: $('#productName').val(),
                    count: $('#productCount').val()
                },
                success: function (data) {
                    var list = '<table>\n' +
                        '    <tr>\n' +
                        '        <th>Product</th>\n' +
                        '        <th>Count</th>\n' +
                        '    </tr>\n' +
                        '</table>';
                    var products = data.map;
                    for (var key in products)
                        list += '<tr><td>' + key + '</td><td>' + products[key] + '</td></tr>'
                    $('.results').html(list);
                }
            });
        });
    });
</script>
</body>
</html>
