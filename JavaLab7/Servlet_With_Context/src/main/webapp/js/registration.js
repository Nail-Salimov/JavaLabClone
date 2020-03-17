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