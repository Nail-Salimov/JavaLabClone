<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Document</title>
    <script>
        function sendFile() {
            // данные для отправки
            let formData = new FormData();
            // забрал файл из input
            let files = ($('#file'))[0]['files'];
            // добавляю файл в formData
            [].forEach.call(files, function (file, i, files) {
                formData.append("file", file);
            });

            $.ajax({
                type: "POST",
                url: "http://localhost:8080/files",
                data: formData,
                processData: false,
                contentType: false
            })
                .done(function (response) {
                    alert(response)
                })
                .fail(function () {
                    alert('Error')
                });
        }
    </script>
    <script>
        function list() {
            $(document).ready(function () {
                $.ajax({
                    type: 'POST',
                    url: "http://localhost:8080/file_list",
                    dataType: 'json',
                    success: function (data) {
                        var all = '';

                        data.forEach(function (item, i, arr) {
                            var link = 'http://localhost:8080/files/file-name:' + item.storageName;
                            all += '<p>' + '<a href=' + '"'+ link + '"'+ '>' + item.originalName + '</a>' + '</p>';
                        });

                        $('.results').html(all);
                    }
                });
            });
        }
    </script>
</head>
<body  onload="list()">
<div>
    <input type="file" id="file" name="file" placeholder="Имя файла..."/>
    <button onclick="sendFile(), list()">
       Upload
    </button>
    <input type="hidden" id="file_hidden">
    <div class="filename"></div>

    <table class="results"></table>
</div>
</body>
</html>