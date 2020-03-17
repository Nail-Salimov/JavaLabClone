
<html>
<head>
    <meta charset="utf-8">
    <title>Тег DIV</title>
    <style type="text/css">
        .round {
            border-radius: 100px; /* Радиус скругления */

        }
        .block1 {
            text-align: center;
            width: 450px;
            height: 250px;
            background: #DCDCDC;
            padding: 5px;
            padding-top: 15px;
            padding-right: 20px;

            border: solid 1px black;
            float: left;
        }
        .button{
            width: 225px;
            height: 50px;
            margin: 10px;
            margin-left: auto;

            background-color: white;
            color: black;
            border: 2px solid black;
        }
        .inside_block{

        }
        .text{
            font-family: Impact;
            font-size: 19px;
        }
    </style>
</head>
<body>

<div class="block1" align="center">
    <img src="https://cs9.pikabu.ru/post_img/big/2016/10/22/6/1477128283192211030.png" width="75" height="75">
    <div class="inside_block">
        <p class="text">Almost done, <font color="#1E90FF">${name}</font> To complete your sign up, we just need to verify your email address: <font color="#1E90FF">${mail}</font></p>
    </div>
    <a href="http://localhost:8080/confirm/check?t=${t}&u=${u}">
        <button class = "button" > Verify email address</button>
    </a>
</div>

</body>
</html>