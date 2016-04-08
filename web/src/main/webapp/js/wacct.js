function captureData(event) {
    $.blockUI();

    $.post('rest/capture', function (data) {
        $.unblockUI();
        displayCoverageList();
    });
}

function resetData(event) {
    $.blockUI();
    $.post('rest/reset', function (data) {
        $.unblockUI();
    });
}

function displayCoverageList() {
    $.post('rest/list', function (data) {
        var out = "<div> <h2>Coverage List</h2> <ul> ";
        $.each(data, function (key, val) {
            var url = "/"+val.context+"/"+val.name;
            out += " <li><a href='"+url+"'> " + getFormattedDate(new Date(parseInt(val.name))) + "</a></li> ";
        });

        out += " </ul> </div>";
        $("#coverageLists").html(out);
        $("#coverageLists").css({display: 'block'});
    });
}

function getFormattedDate(date) {
    var year = date.getFullYear();
    var month = (1 + date.getMonth()).toString();
    month = month.length > 1 ? month : '0' + month;
    var day = date.getDate().toString();
    day = day.length > 1 ? day : '0' + day;
    var hour = date.getHours().toString();
    var min = date.getMinutes().toString();
    var minutes = min.length > 1 ? min : '0' + min;
    var sec = date.getSeconds().toString();
    var seconds = sec.length > 1 ? sec : '0' + sec;
    var time = hour+':'+minutes+':'+seconds;
    return month + '/' + day + '/' + year + ' ' + time;
}